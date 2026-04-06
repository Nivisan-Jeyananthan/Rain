package ch.nivisan.raincloud.network.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
import javax.swing.JLabel;

class MicRecorderToggle extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JButton btnRecordInput;
	private final JButton btnPlaybackAudio;
	private final JComboBox<DeviceInfo> comboInputs;
	private JComboBox<DeviceInfo> comboOutputs;
	private JComboBox<AudioFormatType> comboFormats;

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
		JLabel formatLabel = new JLabel("Audio-Format:");
		add(formatLabel);

		comboFormats = new JComboBox<>(AudioFormatType.values());
		comboFormats.setSelectedItem(DeviceSettings.getAudioFormat());
		add(comboFormats);

		// Initialize device lists for the selected format
		List<DeviceInfo> inputDevices = findDevicesForFormat(TargetDataLine.class, (AudioFormatType) comboFormats.getSelectedItem());
		List<DeviceInfo> outputDevices = findDevicesForFormat(SourceDataLine.class, (AudioFormatType) comboFormats.getSelectedItem());

		comboInputs = new JComboBox<>(inputDevices.toArray(new DeviceInfo[0]));
		if (comboInputs.getItemCount() > 0) {
			DeviceSettings.setMicrophone(comboInputs.getItemAt(0));
		}
		btnRecordInput = new JButton("Start test");
		btnRecordInput.setPreferredSize(new Dimension(120, 30));

		add(comboInputs);
		add(btnRecordInput);

		// Format change listener - updates device lists
		comboFormats.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AudioFormatType selectedFormat = (AudioFormatType) comboFormats.getSelectedItem();
					DeviceSettings.setAudioFormat(selectedFormat);

					// Stop any running recording/playback before changing format
					if (recordingThread != null || playbackThread != null) {
						stopRunningThread();
						btnRecordInput.setText("Start test");
						btnPlaybackAudio.setText("Start playback");
					}

					// Update input devices
					List<DeviceInfo> newInputDevices = findDevicesForFormat(TargetDataLine.class, selectedFormat);
					comboInputs.removeAllItems();
					for (DeviceInfo device : newInputDevices) {
						comboInputs.addItem(device);
					}
					if (comboInputs.getItemCount() > 0) {
						comboInputs.setSelectedIndex(0);
						DeviceSettings.setMicrophone(comboInputs.getItemAt(0));
					}

					// Update output devices
					List<DeviceInfo> newOutputDevices = findDevicesForFormat(SourceDataLine.class, selectedFormat);
					comboOutputs.removeAllItems();
					for (DeviceInfo device : newOutputDevices) {
						comboOutputs.addItem(device);
					}
					if (comboOutputs.getItemCount() > 0) {
						comboOutputs.setSelectedIndex(0);
						DeviceSettings.setSpeaker(comboOutputs.getItemAt(0));
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
					() -> new RecordingThread(Audio.getTargetDataLine(mic)), btnRecordInput, "Start test",
					"Stop test");
		});

		btnPlaybackAudio = new JButton("Start playback");
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

	/**
	 * Finds audio devices that support the specified format.
	 * Tries the format in this order:
	 * 1. The specified format
	 * 2. Fallback format (44.1kHz) if the specified format fails
	 * 3. Legacy format (16kHz) as last resort
	 * 
	 * This ensures the user always has a working audio device available.
	 */
	@SuppressWarnings("unchecked")
	private <T extends DataLine> List<DeviceInfo> findDevicesForFormat(Class<T> lineClass, AudioFormatType formatType) {
		List<DeviceInfo> primaryList = new ArrayList<>();
		List<DeviceInfo> fallbackList = new ArrayList<>();
		List<DeviceInfo> legacyList = new ArrayList<>();

		AudioFormat primaryFormat = formatType.getFormat();
		AudioFormat secondaryFormat = Audio.fallbackFormat;
		AudioFormat tertiaryFormat = Audio.oldFormat;

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

		for (int i = 0; i < mixerInfos.length; i++) {
			Mixer.Info mixerInfo = mixerInfos[i];
			String mixerName = mixerInfo.getName();
			
			// Skip problematic plughw devices on Linux - they often don't support the exact format
			if (mixerName.contains("plughw")) {
				System.out.println("Skipping problematic mixer: " + mixerName);
				continue;
			}

			Mixer mixer = AudioSystem.getMixer(mixerInfo);

			// Try with primary format first
			DataLine.Info primaryInfo = new DataLine.Info(lineClass, primaryFormat);
			if (mixer.isLineSupported(primaryInfo) && primaryInfo.isFormatSupported(primaryFormat)) {
				try {
					T line = (T) mixer.getLine(primaryInfo);

					if (line instanceof TargetDataLine) {
						((TargetDataLine) line).open(primaryFormat);
					} else {
						line.open();
					}

					line.close();
					// Prefer default devices
					if (mixerName.contains("default") || mixerName.contains("[hw:")) {
						primaryList.add(0, new DeviceInfo(mixerInfo, primaryFormat));
					} else {
						primaryList.add(new DeviceInfo(mixerInfo, primaryFormat));
					}
					continue;
				} catch (Exception e) {
					System.err.println("Failed with format " + formatType.getDisplayName() + ": " + mixerName + " - " + e.getMessage());
				}
			}

			// Try with fallback format (44.1kHz) if primary format fails
			DataLine.Info fallbackInfo = new DataLine.Info(lineClass, secondaryFormat);
			if (mixer.isLineSupported(fallbackInfo) && fallbackInfo.isFormatSupported(secondaryFormat)) {
				try {
					T line = (T) mixer.getLine(fallbackInfo);

					if (line instanceof TargetDataLine) {
						((TargetDataLine) line).open(secondaryFormat);
					} else {
						line.open();
					}

					line.close();
					System.out.println("Using fallback format (44.1 kHz) for: " + mixerName + " (requested: " + formatType.getDisplayName() + ")");
					fallbackList.add(new DeviceInfo(mixerInfo, secondaryFormat));
					continue;
				} catch (Exception e) {
					System.err.println("Failed with fallback format: " + mixerName + " - " + e.getMessage());
				}
			}

			// Try with legacy format (16kHz, 8-bit, stereo) as last resort
			DataLine.Info legacyInfo = new DataLine.Info(lineClass, tertiaryFormat);
			if (mixer.isLineSupported(legacyInfo) && legacyInfo.isFormatSupported(tertiaryFormat)) {
				try {
					T line = (T) mixer.getLine(legacyInfo);

					if (line instanceof TargetDataLine) {
						((TargetDataLine) line).open(tertiaryFormat);
					} else {
						line.open();
					}

					line.close();
					System.out.println("Using legacy format (16 kHz) for: " + mixerName + " (requested: " + formatType.getDisplayName() + ")");
					legacyList.add(new DeviceInfo(mixerInfo, tertiaryFormat));
				} catch (Exception e) {
					System.err.println("Failed with legacy format: " + mixerName + " - " + e.getMessage());
				}
			}
		}

		// Return in order of preference: primary > fallback > legacy
		// Always return at least one option to ensure user can always record/playback
		if (!primaryList.isEmpty()) {
			return primaryList;
		}
		if (!fallbackList.isEmpty()) {
			System.out.println("No devices found for primary format. Using fallback format devices.");
			return fallbackList;
		}
		if (!legacyList.isEmpty()) {
			System.out.println("No devices found for fallback format. Using legacy format devices.");
			return legacyList;
		}

		// If no devices found with any format, log error and return empty list
		System.err.println("CRITICAL: No audio devices found for any format!");
		return new ArrayList<>();
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

	@SuppressWarnings("unchecked")
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
			if (line instanceof TargetDataLine) {
				((TargetDataLine) line).open(device.format);
			} else {
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