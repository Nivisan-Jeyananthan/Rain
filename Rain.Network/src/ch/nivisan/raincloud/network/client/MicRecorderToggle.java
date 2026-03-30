package ch.nivisan.raincloud.network.client;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ch.nivisan.raincloud.network.utilities.Audio;
import ch.nivisan.raincloud.network.utilities.NetDriver;

import java.awt.Dimension;
import java.awt.FlowLayout;

class MicRecorderToggle extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JComboBox<DeviceInfo> combo;
	private RecordingThread recordingThread = null;

	MicRecorderToggle() {
		super("Mikrofon‑Aufnahme");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());

		List<DeviceInfo> devices = findAllMicrophones();

		combo = new JComboBox<>(devices.toArray(new DeviceInfo[0]));
		JButton toggleBtn = new JButton("Start");
		toggleBtn.setPreferredSize(new Dimension(120, 30));

		add(combo);
		add(toggleBtn);

		combo.addActionListener(e -> {
			if (recordingThread != null) {
				switchToDevice((DeviceInfo) combo.getSelectedItem());
			}
		});

		toggleBtn.addActionListener(e -> {
			if (recordingThread == null) {
				switchToDevice((DeviceInfo) combo.getSelectedItem());
				toggleBtn.setText("Stop");
			} else {
				recordingThread.stopRecording();
				recordingThread = null;
				toggleBtn.setText("Start");
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
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
				AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000.0f, 16, 1, 2, 48000.0f,
						false);

				targetLine.open(format);
				targetLine.close();
				list.add(new DeviceInfo(mixerInfo, format));
			} catch (Exception e) {
			}
		}

		return list;
	}

	private void switchToDevice(DeviceInfo dev) {
		if (recordingThread != null) {
			recordingThread.stopRecording();
			recordingThread = null;
		}

		try {
			Mixer mixer = AudioSystem.getMixer(dev.mixerInfo);
			DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, dev.format);

			TargetDataLine line = (TargetDataLine) mixer.getLine(lineInfo);
			line.open(dev.format);
			line.start();

			recordingThread = new RecordingThread(line);
			recordingThread.start();

			System.out.println("Aufnahme gestartet: " + dev.mixerInfo.getName());
		} catch (LineUnavailableException ex) {
			JOptionPane.showMessageDialog(this, "Gerät nicht verfügbar: " + dev.mixerInfo.getName(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static class RecordingThread extends Thread {
		private final NetDriver netDriver;
		private final TargetDataLine line;


		RecordingThread(TargetDataLine line) {
			this.line = line;
			this.netDriver = new NetDriver("localhost", 50005);
		}

		@Override
		public void run() {
			byte[] micBuffer = new byte[Audio.bufferSize];

			line.read(micBuffer, 0, micBuffer.length);

			netDriver.send(micBuffer);

			// File wavFile = new File("aufnahme.wav");
			// try (AudioInputStream ais = new AudioInputStream(line)) {
			// AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}

		void stopRecording() {
			this.netDriver.close();
			line.stop();
			line.close();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MicRecorderToggle::new);
	}
}