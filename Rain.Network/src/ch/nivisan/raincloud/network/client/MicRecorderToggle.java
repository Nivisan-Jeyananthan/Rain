package ch.nivisan.raincloud.network.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
	private final JComboBox<DeviceInfo> comboOutputs;
	private final JComboBox<AudioFormatType> comboInputFormats = new JComboBox<>(AudioFormatType.values());
	private final JComboBox<AudioFormatType> comboOutputFormats = new JComboBox<>(AudioFormatType.values());

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

		// Add format selector
		JLabel formatLabel = new JLabel("Input-Audio-Format:");
		add(formatLabel);

		comboInputFormats.setSelectedItem(DeviceSettings.getInputFormat());
		add(comboInputFormats);

		// Initialize device lists for the selected formats
		List<DeviceInfo> inputDevices = findDevicesForFormat(TargetDataLine.class,
				(AudioFormatType) comboInputFormats.getSelectedItem());
		List<DeviceInfo> outputDevices = findDevicesForFormat(SourceDataLine.class,
				(AudioFormatType) comboOutputFormats.getSelectedItem());

		comboInputs = new JComboBox<>(inputDevices.toArray(new DeviceInfo[0]));
		if (comboInputs.getItemCount() > 0) {
			DeviceSettings.setMicrophone(comboInputs.getItemAt(0));
		}
		btnRecordInput = new JButton("Start test");
		btnRecordInput.setPreferredSize(new Dimension(120, 30));

		add(comboInputs);
		add(btnRecordInput);

		comboInputFormats.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AudioFormatType selectedFormat = (AudioFormatType) comboInputFormats.getSelectedItem();
					DeviceSettings.setInputFormat(selectedFormat);

					if (recordingThread != null || playbackThread != null) {
						stopRunningThread();
						btnRecordInput.setText("Start test");
						btnPlaybackAudio.setText("Start playback");
					}

					List<DeviceInfo> newInputDevices = findDevicesForFormat(TargetDataLine.class, selectedFormat);
					comboInputs.removeAllItems();
					for (DeviceInfo device : newInputDevices) {
						comboInputs.addItem(device);
					}
					if (comboInputs.getItemCount() > 0) {
						comboInputs.setSelectedIndex(0);
						DeviceSettings.setMicrophone(comboInputs.getItemAt(0));
					}

					System.out.println("Audio-Format geändert zu: " + selectedFormat.getDisplayName());
				}
			}
		});

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
					line -> new RecordingThread((TargetDataLine) line), btnRecordInput, "Start test",
					"Stop test");
		});

		btnPlaybackAudio = new JButton("Start playback");
		JLabel outputFormatLabel = new JLabel("Output-Audio-Format:");
		add(outputFormatLabel);

		comboOutputFormats.setSelectedItem(DeviceSettings.getOutputFormat());
		add(comboOutputFormats);
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
				JOptionPane.showMessageDialog(this, "Keinen Lautsprecher ausgewählt.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			System.out.println("Playback gestartet: " + speaker.mixerInfo.getName());

			startFromDevice(speaker, SourceDataLine.class,
					line -> new PlaybackThread((SourceDataLine) line), btnPlaybackAudio, "Start playback",
					"Stop playback");
		});

		add(comboOutputs);
		add(btnPlaybackAudio);

		comboOutputFormats.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (recordingThread != null || playbackThread != null) {
						stopRunningThread();
						btnRecordInput.setText("Start test");
						btnPlaybackAudio.setText("Start playback");
					}

					AudioFormatType selectedFormat = (AudioFormatType) comboOutputFormats.getSelectedItem();
					DeviceSettings.setOutputFormat(selectedFormat);
					List<DeviceInfo> newOutputDevices = findDevicesForFormat(SourceDataLine.class,
							selectedFormat);
					comboOutputs.removeAllItems();
					for (DeviceInfo device : newOutputDevices) {
						comboOutputs.addItem(device);
					}
					if (comboOutputs.getItemCount() > 0) {
						comboOutputs.setSelectedIndex(0);
						DeviceSettings.setSpeaker(comboOutputs.getItemAt(0));
					}
				}
			}
		});

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

	/**
	 * Finds audio devices that support the specified format. Tries the format in
	 * this order: 1. The specified format 2. Fallback format (44.1kHz) if the
	 * specified format fails 3. Legacy format (16kHz) as last resort
	 * 
	 * This ensures the user always has a working audio device available.
	 */
	@SuppressWarnings("unchecked")
	private <T extends DataLine> List<DeviceInfo> findDevicesForFormat(Class<T> lineClass, AudioFormatType formatType) {
		List<DeviceInfo> primaryList = new ArrayList<>();
		AudioFormat[] candidateFormats = getCandidateFormats(formatType);

		for (AudioFormat candidate : candidateFormats) {
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				String mixerName = mixerInfo.getName();

				if (mixerName.contains("plughw")) {
					System.out.println("Skipping problematic mixer: " + mixerName);
					continue;
				}

				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				DataLine.Info info = new DataLine.Info(lineClass, candidate);

				if (!mixer.isLineSupported(info) || !info.isFormatSupported(candidate)) {
					continue;
				}

				try {
					T line = (T) mixer.getLine(info);
					if (line instanceof TargetDataLine) {
						((TargetDataLine) line).open(candidate);
					} else {
						line.open();
					}
					line.close();

					DeviceInfo deviceInfo = new DeviceInfo(mixerInfo, candidate);
					if (!primaryList.contains(deviceInfo)) {
						if (mixerName.contains("default") || mixerName.contains("[hw:")) {
							primaryList.add(0, deviceInfo);
						} else {
							primaryList.add(deviceInfo);
						}
					}
				} catch (Exception e) {
					System.err.println("Failed with format " + formatType.getDisplayName() + ": " + mixerName
							+ " - " + e.getMessage());
				}
			}
			if (!primaryList.isEmpty()) {
				break; // use first supported candidate set
			}
		}

		if (primaryList.isEmpty()) {
			System.err.println("CRITICAL: No audio devices found for format " + formatType.getDisplayName());
		}
		return primaryList;
	}

	private AudioFormat[] getCandidateFormats(AudioFormatType selected) {
		if (selected == null) {
			return new AudioFormat[] { Audio.defaultFormatStereo, Audio.compatFormatStereo, Audio.fallbackFormatStereo,
					Audio.defaultFormat, Audio.compatFormat, Audio.fallbackFormat, Audio.oldFormat };
		}

		if (selected.isLegacy()) {
			return new AudioFormat[] { Audio.oldFormat, Audio.compatFormatStereo, Audio.fallbackFormatStereo,
					Audio.defaultFormatStereo, Audio.compatFormat, Audio.fallbackFormat, Audio.defaultFormat };
		} else if (selected.isFallback()) {
			return new AudioFormat[] { Audio.fallbackFormat, Audio.fallbackFormatStereo, Audio.defaultFormatStereo,
					Audio.compatFormatStereo, Audio.defaultFormat, Audio.compatFormat, Audio.oldFormat };
		} else if (selected.isCompat()) {
			return new AudioFormat[] { Audio.compatFormat, Audio.compatFormatStereo, Audio.defaultFormatStereo,
					Audio.fallbackFormatStereo, Audio.defaultFormat, Audio.fallbackFormat, Audio.oldFormat };
		} else if (selected.isHigher()) {
			return new AudioFormat[] { Audio.higherFormat, Audio.higherFormatStereo, Audio.defaultFormatStereo,
					Audio.compatFormatStereo, Audio.fallbackFormatStereo, Audio.defaultFormat, Audio.compatFormat };
		}

		// Standard
		return new AudioFormat[] { Audio.defaultFormat, Audio.defaultFormatStereo, Audio.compatFormatStereo,
				Audio.fallbackFormatStereo, Audio.compatFormat, Audio.fallbackFormat, Audio.oldFormat };
	}

	private void deleteTempAudioFile() {
		if (tempAudioFile != null && tempAudioFile.exists()) {
			tempAudioFile.delete();
		}
	}

	private Thread runningThread;

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

	@SuppressWarnings("unchecked")
	private <T extends DataLine> void startFromDevice(DeviceInfo device, Class<T> lineClass,
			Function<T, Thread> threadFactory, JButton btn, String startText, String stopText) {

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

		btn.setEnabled(false);
		btn.setText("Wird gestartet...");

		Thread initThread = new Thread(() -> {
			try {
				Mixer mixer = AudioSystem.getMixer(device.mixerInfo);
				DataLine.Info lineInfo = new DataLine.Info(lineClass, device.format);

				T line = (T) mixer.getLine(lineInfo);
				if (line instanceof TargetDataLine) {
					((TargetDataLine) line).open(device.format);
				} else {
					line.open();
				}

				line.start();

				Thread thread = threadFactory.apply(line);
				storeRunningThread(thread);
				thread.start();

				SwingUtilities.invokeLater(() -> {
					btn.setEnabled(true);
					btn.setText(stopText);
				});
			} catch (LineUnavailableException ex) {
				SwingUtilities.invokeLater(() -> {
					btn.setEnabled(true);
					btn.setText(startText);
					JOptionPane.showMessageDialog(MicRecorderToggle.this,
							"Gerät nicht verfügbar: " + device.mixerInfo.getName(), "Fehler",
							JOptionPane.ERROR_MESSAGE);
				});
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtilities.invokeLater(() -> {
					btn.setEnabled(true);
					btn.setText(startText);
				});
			}
		});
		initThread.setDaemon(true);
		initThread.setName("AudioDeviceInit-" + device.mixerInfo.getName());
		initThread.setPriority(Thread.NORM_PRIORITY + 1);
		initThread.start();
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