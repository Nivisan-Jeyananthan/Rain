package ch.nivisan.raincloud.network.client;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
import ch.nivisan.raincloud.network.utilities.FileService;
import ch.nivisan.raincloud.network.utilities.NetDriver;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

class MicRecorderToggle extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JButton btnRecordInput;
	private final JComboBox<DeviceInfo> comboInputs;
	private final JComboBox<DeviceInfo> comboOutputs;

	private RecordingThread recordingThread = null;
	private PlaybackThread playbackThread = null;
	private final NetDriver netDriver;

	MicRecorderToggle(NetDriver netDriver) {
		super("Mikrofon‑Aufnahme");
		this.netDriver = netDriver;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setResizable(false);

		List<DeviceInfo> inputDevices = findAllMicrophones();

		comboInputs = new JComboBox<>(inputDevices.toArray(new DeviceInfo[0]));
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
			switchInputDevice();
		});

		List<DeviceInfo> outputDevices = findOutputDevices();
		comboOutputs = new JComboBox<>(outputDevices.toArray(new DeviceInfo[0]));

		comboOutputs.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (playbackThread == null)
						DeviceSettings.setSpeaker((DeviceInfo) comboInputs.getSelectedItem());
					else {
						comboOutputs.setSelectedItem(DeviceSettings.getSpeaker());
					}
				}

			}
		});

		btnRecordInput.addActionListener(e -> {
			switchInputDevice();
		});

		add(comboOutputs);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				netDriver.close();
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private List<DeviceInfo> findOutputDevices() {

		List<DeviceInfo> list = new ArrayList<>();

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

		for (int i = 0; i < mixerInfos.length; i++) {
			Mixer.Info mixerInfo = mixerInfos[i];
			Mixer mixer = AudioSystem.getMixer(mixerInfo);

			DataLine.Info info = new DataLine.Info(Clip.class, Audio.defaultFormat);
			if (!mixer.isLineSupported(info)) {
				continue;
			}

			try {
				Clip sourceLine = (Clip) mixer.getLine(info);

				var data = FileService.getFromFile("aufnahme.wav");
				sourceLine.open(Audio.defaultFormat, data, 0, data.length);
				sourceLine.start();
				sourceLine.close();
				list.add(new DeviceInfo(mixerInfo, Audio.defaultFormat));
			} catch (Exception e) {
			}
		}
		return list;
	}

	private List<DeviceInfo> findAllMicrophones() {
		List<DeviceInfo> list = new ArrayList<>();

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

		for (int i = 0; i < mixerInfos.length; i++) {
			Mixer.Info mixerInfo = mixerInfos[i];
			Mixer mixer = AudioSystem.getMixer(mixerInfo);

			DataLine.Info info = new DataLine.Info(TargetDataLine.class, null);
			if (!mixer.isLineSupported(info)) {
				continue;
			}

			try {
				TargetDataLine targetLine = (TargetDataLine) mixer.getLine(info);

				targetLine.open(Audio.defaultFormat);
				targetLine.close();
				list.add(new DeviceInfo(mixerInfo, Audio.defaultFormat));
			} catch (Exception e) {
			}
		}

		return list;
	}

	private void switchInputDevice() {
		if (recordingThread != null) {
			recordingThread.stopRecording();
			recordingThread = null;
			btnRecordInput.setText("Start test");
			return;
		}

		DeviceInfo device = DeviceSettings.getMicrophone();
		if (device == null) {
			btnRecordInput.setText("Start test");
			JOptionPane.showMessageDialog(this, "Gerät nicht verfügbar: ", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;

		}

		try {
			Mixer mixer = AudioSystem.getMixer(device.mixerInfo);
			DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, device.format);

			TargetDataLine line = (TargetDataLine) mixer.getLine(lineInfo);
			line.open(device.format);
			line.start();

			recordingThread = new RecordingThread(line);
			recordingThread.start();
			btnRecordInput.setText("Stop test");

			System.out.println("Aufnahme gestartet: " + device.mixerInfo.getName());
		} catch (LineUnavailableException ex) {
			JOptionPane.showMessageDialog(this, "Gerät nicht verfügbar: " + device.mixerInfo.getName(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {

		}
	}

	private class PlaybackThread extends Thread {
		private final SourceDataLine line;
		static final String filepath = "aufnahme.wav";

		public PlaybackThread(SourceDataLine line) {
			this.line = line;
		}

		@Override
		public void run() {
			try {
				URL url = this.getClass().getResource(filepath);
				AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void stopRecording() {
			netDriver.close();
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
			byte[] micBuffer = new byte[Audio.bufferSize];

			line.read(micBuffer, 0, micBuffer.length);

			// netDriver.send(micBuffer);

			File wavFile = new File("aufnahme.wav");
			try (AudioInputStream ais = new AudioInputStream(line)) {
				AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void stopRecording() {
			netDriver.close();
			line.stop();
			line.close();
		}
	}
}