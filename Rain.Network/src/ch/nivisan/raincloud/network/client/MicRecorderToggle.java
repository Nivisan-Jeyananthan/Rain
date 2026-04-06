package ch.nivisan.raincloud.network.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.nivisan.raincloud.network.utilities.Audio;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MicRecorderToggle extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JButton btnRecordInput;
	private final JButton btnPlaybackAudio;
	private final JComboBox<DeviceInfo> comboInputs;
	private JComboBox<DeviceInfo> comboOutputs;

	private RecordingThread recordingThread = null;
	private PlaybackThread playbackThread = null;
	private final File tempAudioFile;

	MicRecorderToggle() {
		super("Mikrofon‑Aufnahme");
		try {
			tempAudioFile = File.createTempFile("rain-audio-test-", ".wav");
			tempAudioFile.deleteOnExit();
		} catch (IOException e) {
			throw new RuntimeException("Konnte temporäre Audio-Datei nicht erstellen", e);
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setResizable(false);

		List<DeviceInfo> inputDevices = findDevices(TargetDataLine.class);

		comboInputs = new JComboBox<>(inputDevices.toArray(new DeviceInfo[0]));
		if (comboInputs.getItemCount() > 0) {
			DeviceSettings.setMicrophone(comboInputs.getItemAt(0));
		}
		btnRecordInput = new JButton("Start test");
		btnRecordInput.setPreferredSize(new Dimension(120, 30));

		add(comboInputs);
		add(btnRecordInput);

		comboInputs.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (recordingThread == null)
						DeviceSettings.setMicrophone((DeviceInfo) comboInputs.getSelectedItem());
					else {
						comboInputs.setSelectedItem(DeviceSettings.getMicrophone());
					}
				}

			}
		});

		btnRecordInput.addActionListener(e -> {
			DeviceInfo mic = DeviceSettings.getMicrophone();
			if (mic == null) {
				JOptionPane.showMessageDialog(this, "Kein Mikrofon ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
				return;
			}
			System.out.println("Aufnahme gestartet: " + mic.mixerInfo.getName());

			startFromDevice(mic, TargetDataLine.class,
					() -> new RecordingThread(Audio.getTargetDataLine(mic)), btnRecordInput, "Start test",
					"Stop test");
		});

		btnPlaybackAudio = new JButton("Start playback");
		List<DeviceInfo> outputDevices = findDevices(SourceDataLine.class);
		comboOutputs = new JComboBox<>(outputDevices.toArray(new DeviceInfo[0]));
		if (comboOutputs.getItemCount() > 0) {
			DeviceSettings.setSpeaker(comboOutputs.getItemAt(0));
		}

		comboOutputs.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (playbackThread == null)
						DeviceSettings.setSpeaker((DeviceInfo) comboOutputs.getSelectedItem());
					else {
						comboOutputs.setSelectedItem(DeviceSettings.getSpeaker());
					}
				}

			}
		});

		btnPlaybackAudio.addActionListener(e -> {
			DeviceInfo speaker = DeviceSettings.getSpeaker();
			if (speaker == null) {
				JOptionPane.showMessageDialog(this, "Keinen Lautsprecher ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
				return;
			}
			System.out.println("Playback gestartet: " + speaker.mixerInfo.getName());

			startFromDevice(speaker, SourceDataLine.class,
					() -> new PlaybackThread(Audio.getSourceDataLine(speaker)), btnPlaybackAudio, "Start playback",
					"Stop playback");
		});

		add(comboOutputs);
		add(btnPlaybackAudio);

		pack();
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopRunningThread();
				deleteTempAudioFile();
			}
		});
		setVisible(true);
	}

	private <T extends DataLine> List<DeviceInfo> findDevices(Class<T> lineClass) {
		List<DeviceInfo> list = new ArrayList<>();

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

		for (int i = 0; i < mixerInfos.length; i++) {
			Mixer.Info mixerInfo = mixerInfos[i];
			Mixer mixer = AudioSystem.getMixer(mixerInfo);

			DataLine.Info info = new DataLine.Info(lineClass, Audio.defaultFormat);
			if (!mixer.isLineSupported(info)) {
				continue;
			}

			if (!info.isFormatSupported(Audio.defaultFormat))
				continue;

			try {
				T line = (T) mixer.getLine(info);

				if (line instanceof TargetDataLine lf)
					lf.open(Audio.defaultFormat);
				else {
					line.open();
				}

				line.close();
				list.add(new DeviceInfo(mixerInfo, Audio.defaultFormat));
			} catch (Exception e) {
			}
		}

		return list;
	}

	private Thread runningThread;

	private void deleteTempAudioFile() {
		if (tempAudioFile != null && tempAudioFile.exists()) {
			tempAudioFile.delete();
		}
	}

	private void stopRunningThread() {
		Thread thread = getRunningThread();
		if (thread != null) {
			if (thread instanceof RecordingThread) {
				((RecordingThread) thread).stopRecording();
			} else if (thread instanceof PlaybackThread) {
				((PlaybackThread) thread).stopPlayback();
			}
			runningThread = null;
		}
	}

	private Thread getRunningThread() {
		return runningThread;
	}

	private void storeRunningThread(Thread t) {
		runningThread = t;
	}

	private void stopThread(Thread thread, JButton btn, String startText) {
		if (thread instanceof RecordingThread) {
			((RecordingThread) thread).stopRecording();
		} else if (thread instanceof PlaybackThread) {
			((PlaybackThread) thread).stopPlayback();
		}
		runningThread = null;
		btn.setText(startText);
	}

	private <T extends DataLine> void startFromDevice(DeviceInfo device, Class<T> lineClass,
			Supplier<Thread> threadFactory, JButton btn, String startText, String stopText) {

		Thread existing = getRunningThread();
		if (existing != null) {
			stopThread(existing, btn, startText);
			return;
		}

		if (device == null) {
			btn.setText(startText);
			JOptionPane.showMessageDialog(this, "Gerät nicht verfügbar: ", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Mixer mixer = AudioSystem.getMixer(device.mixerInfo);
			DataLine.Info lineInfo = new DataLine.Info(lineClass, device.format);

			T line = (T) mixer.getLine(lineInfo);
			if (line instanceof TargetDataLine lf)
				lf.open(device.format);
			else {
				line.open();
			}

			line.start();

			Thread thread = threadFactory.get();
			storeRunningThread(thread);

			thread.start();
			btn.setText(stopText);

		} catch (LineUnavailableException ex) {
			JOptionPane.showMessageDialog(this, "Gerät nicht verfügbar: " + device.mixerInfo.getName(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class PlaybackThread extends Thread {
		private final SourceDataLine line;

		public PlaybackThread(SourceDataLine line) {
			this.line = line;
		}

		@Override
		public void run() {
			Audio.playAudio(line, tempAudioFile.getAbsolutePath());
		}

		void stopPlayback() {
			line.drain();
			line.stop();
			line.close();
		}
	}

	private class RecordingThread extends Thread {
		private final TargetDataLine line;

		RecordingThread(TargetDataLine line) {
			this.line = line;
		}

		@Override
		public void run() {
			Audio.recordAudio(line, tempAudioFile.getAbsolutePath());
		}

		void stopRecording() {
			line.stop();
			line.close();
		}
	}
}