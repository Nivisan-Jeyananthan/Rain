package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class SimplePlayer extends JFrame {
	private JPanel contentPanel;
	private Clip clip;

	public SimplePlayer(File wavFile) throws Exception {

		setTitle("Player");
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 380);
		setLocationRelativeTo(null);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile);
		clip = AudioSystem.getClip();
		clip.open(ais);

		JButton playBtn = new JButton("Play");
		playBtn.setBounds(20, 20, 40, 10);
		playBtn.addActionListener(e -> play(new File("mikrofon.wav")));

		JButton stopBtn = new JButton("Stop");
		stopBtn.addActionListener(e -> {
			clip.stop();
			clip.setFramePosition(0); // zurücksetzen
		});
		stopBtn.setBounds(20, 20, 40, 10);

		contentPanel.add(playBtn);
		contentPanel.add(stopBtn);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(() -> {
			try {
				new SimplePlayer(new File("mikrofon.wav")).setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	private void play(File wavFile) {

		new Thread() {
			public void run() {
				AudioInputStream ais;
				try {
					ais = AudioSystem.getAudioInputStream(wavFile);

					AudioFormat format = ais.getFormat(); 
					DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
					SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

					line.open(format); 
					line.start(); 

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = ais.read(buffer, 0, buffer.length)) != -1) {
						line.write(buffer, 0, bytesRead); 
					}

					line.drain(); 
					line.close();
					ais.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.run();
	}
}