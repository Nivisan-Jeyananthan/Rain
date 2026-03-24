package ch.nivisan.raincloud.network.client;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

public class MicRecorderToggle extends JFrame {

    private static class DeviceInfo {
        public final Mixer.Info mixerInfo;
        public final AudioFormat format;

        DeviceInfo(Mixer.Info mixer, AudioFormat fmt) {
            this.mixerInfo = mixer;
            this.format = fmt;
        }

        @Override
        public String toString() {
            return mixerInfo.getName() + " (" +
                    (int) format.getSampleRate() + " Hz, " +
                    format.getChannels() + "-Kanal)";
        }
    }

    private final JComboBox<DeviceInfo> combo;
    private RecordingThread recordingThread = null;

    public MicRecorderToggle() {
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
                Line.Info[] targetLines = mixer.getTargetLineInfo();
                for (Line.Info lineInfo : targetLines) {
                    if (!(lineInfo instanceof DataLine.Info))
                        continue;

                    DataLine.Info dataLineInfo = (DataLine.Info) lineInfo;
                    AudioFormat[] supportedFormats = dataLineInfo.getFormats();
                    for (AudioFormat currentFormat : supportedFormats) {
                        if (currentFormat.getSampleRate() > -1 && currentFormat.isBigEndian())
                            list.add(new DeviceInfo(mixerInfo, currentFormat));
                    }

                }
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
            JOptionPane.showMessageDialog(this,
                    "Gerät nicht verfügbar: " + dev.mixerInfo.getName(),
                    "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class RecordingThread extends Thread {
        private final TargetDataLine line;

        RecordingThread(TargetDataLine line) {
            this.line = line;
        }

        @Override
        public void run() {
            File wavFile = new File("aufnahme.wav");
            try (AudioInputStream ais = new AudioInputStream(line)) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void stopRecording() {
            line.stop();
            line.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MicRecorderToggle::new);
    }
}