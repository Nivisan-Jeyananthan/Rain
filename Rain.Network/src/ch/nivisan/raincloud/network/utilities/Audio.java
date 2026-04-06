package ch.nivisan.raincloud.network.utilities;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import ch.nivisan.raincloud.network.client.DeviceInfo;
import ch.nivisan.raincloud.network.client.DeviceSettings;

public class Audio {
	public final static float sampleRate = 48000.0f;
	public final static int sampleSizeInBits = 16;
	public final static int channels = 1;
	public final static int frameSize = sampleSizeInBits / 8;
	public final static int sendRateInMs = 15;
	public final static int secondsInMs = 1000;
	public final static int bufferSize = (int) ((sampleRate * frameSize * sendRateInMs) / secondsInMs);

	public final static AudioFormat defaultFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate,
			sampleSizeInBits, channels, frameSize, sampleRate, false);
	
	public final static AudioFormat fallbackFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f,
			sampleSizeInBits, channels, frameSize, 44100.0f, false);

	public final static AudioFormat oldFormat = new AudioFormat(16000, 8, 2, true, true);
	
	/**
	 * Flag to indicate if the client should use legacy audio format.
	 * When true, audio will be sent/received in oldFormat and resampled accordingly.
	 * 
	 * @deprecated Use DeviceSettings.getAudioFormat() instead for dynamic format selection.
	 */
	@Deprecated
	public static boolean useLegacyFormat = false;

	/**
	 * Flag to indicate if the client should use fallback audio format (44.1kHz).
	 * When true, audio will be sent/received in fallbackFormat and resampled accordingly.
	 * 
	 * @deprecated Use DeviceSettings.getAudioFormat() instead for dynamic format selection.
	 */
	@Deprecated
	public static boolean useFallbackFormat = false;

	/**
	 * Helper method to determine if legacy format should be used based on DeviceSettings.
	 * @return true if legacy format is selected
	 */
	public static boolean shouldUseLegacyFormat() {
		try {
			return DeviceSettings.getAudioFormat().isLegacy();
		} catch (Exception e) {
			return useLegacyFormat; // Fallback to deprecated flag
		}
	}

	/**
	 * Helper method to determine if fallback format should be used based on DeviceSettings.
	 * @return true if fallback format is selected
	 */
	public static boolean shouldUseFallbackFormat() {
		try {
			return DeviceSettings.getAudioFormat().isFallback();
		} catch (Exception e) {
			return useFallbackFormat; // Fallback to deprecated flag
		}
	}

	/**
	 * Resamples audio data from the default format to the currently selected format.
	 * Automatically handles resampling based on DeviceSettings.getAudioFormat().
	 * @param audioData the audio data in defaultFormat
	 * @return resampled audio data in the selected format
	 */
	public static byte[] resampleToSelectedFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		try {
			ch.nivisan.raincloud.network.client.AudioFormatType format = DeviceSettings.getAudioFormat();
			if (format.isLegacy()) {
				return resampleToOldFormat(audioData);
			} else if (format.isFallback()) {
				return resampleToFallbackFormat(audioData);
			}
		} catch (Exception e) {
			// Fallback to deprecated flags
			if (shouldUseLegacyFormat()) {
				return resampleToOldFormat(audioData);
			} else if (shouldUseFallbackFormat()) {
				return resampleToFallbackFormat(audioData);
			}
		}

		// Return as-is for default format
		return audioData;
	}

	/**
	 * Resamples audio data from the currently selected format to the default format.
	 * Automatically handles resampling based on DeviceSettings.getAudioFormat().
	 * @param audioData the audio data in the selected format
	 * @return resampled audio data in defaultFormat
	 */
	public static byte[] resampleFromSelectedFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		try {
			ch.nivisan.raincloud.network.client.AudioFormatType format = DeviceSettings.getAudioFormat();
			if (format.isLegacy()) {
				return resampleFromOldFormat(audioData);
			} else if (format.isFallback()) {
				return resampleFromFallbackFormat(audioData);
			}
		} catch (Exception e) {
			// Fallback to deprecated flags
			if (shouldUseLegacyFormat()) {
				return resampleFromOldFormat(audioData);
			} else if (shouldUseFallbackFormat()) {
				return resampleFromFallbackFormat(audioData);
			}
		}

		// Return as-is for default format
		return audioData;
	}

	/**
	 * Resamples audio data from the old legacy format (16kHz, 8-bit, stereo) to the default format (48kHz, 16-bit, mono).
	 * @param audioData the audio data in oldFormat
	 * @return resampled audio data in defaultFormat
	 */
	public static byte[] resampleFromOldFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}
		
		AudioResampler resampler = new AudioResampler(oldFormat, defaultFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the default format (48kHz, 16-bit, mono) to the old legacy format (16kHz, 8-bit, stereo).
	 * @param audioData the audio data in defaultFormat
	 * @return resampled audio data in oldFormat
	 */
	public static byte[] resampleToOldFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}
		
		AudioResampler resampler = new AudioResampler(defaultFormat, oldFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the fallback format (44.1kHz, 16-bit, mono) to the default format (48kHz, 16-bit, mono).
	 * @param audioData the audio data in fallbackFormat
	 * @return resampled audio data in defaultFormat
	 */
	public static byte[] resampleFromFallbackFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}
		
		AudioResampler resampler = new AudioResampler(fallbackFormat, defaultFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the default format (48kHz, 16-bit, mono) to the fallback format (44.1kHz, 16-bit, mono).
	 * @param audioData the audio data in defaultFormat
	 * @return resampled audio data in fallbackFormat
	 */
	public static byte[] resampleToFallbackFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}
		
		AudioResampler resampler = new AudioResampler(defaultFormat, fallbackFormat);
		return resampler.resample(audioData);
	}

	public static void recordAudio(TargetDataLine line, String filepath) {
		if (line == null)
			return;

		if (!line.isOpen()) {
			try {
				line.open(defaultFormat);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}

		if (!line.isRunning())
			line.start();

		if (!filepath.isEmpty()) {
			File wavFile = new File(filepath);
			try (AudioInputStream ais = new AudioInputStream(line)) {
				AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

			clearLine(line);
		}
	}

	/**
	 * Plays the given audio data on the SourceDataLine.
	 * Does not close the line after playing, so it can be used continuously.
	 * @param line
	 * @param data
	 */
	public static void playAudio(SourceDataLine line, byte[] data) {
		writeAudio(line, data);
	}

	public static void writeAudio(SourceDataLine line, byte[] data) {
		if (line == null || data == null || data.length == 0)
			return;

		try {
			if (!line.isOpen()) {
				line.open();
			}

		if (!line.isRunning())
			line.start();

		line.write(data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playAudio(SourceDataLine line, String filepath) {
		try {
			File file = new File(filepath);
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);

			if (line != null && !line.isOpen()) {
				line.open();
				if (!line.isRunning())
					line.start();

				byte[] buffer = new byte[NetUtils.MAX_PACKET_SIZE];
				int bytesRead = 0;

				while ((bytesRead = ais.read(buffer, 0, buffer.length)) != -1) {
					line.write(buffer, 0, bytesRead);
				}

				clearLine(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void clearLine(DataLine line) {
		line.drain();
		line.stop();
		line.close();
	}

	/**
	 * Gets the sourcedataline also known as speaker device from given deviceinfo
	 * 
	 * @param speaker
	 * @return the SourceDataLine which allows playing back audio data from a device
	 */
	public static SourceDataLine getSourceDataLine(DeviceInfo speaker) {
		if (speaker == null) {
			return getDefaultSourceDataLine();
		}

		try {
			Mixer mixer = AudioSystem.getMixer(speaker.mixerInfo);
			DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, speaker.format);

			return (SourceDataLine) mixer.getLine(lineInfo);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SourceDataLine getDefaultSourceDataLine() {
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			try {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, defaultFormat);
				if (!mixer.isLineSupported(info)) {
					continue;
				}
				return (SourceDataLine) mixer.getLine(info);
			} catch (Exception e) {
				// ignore mixer if unavailable
			}
		}

		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, defaultFormat);
			return (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the sourcedataline also known as speaker device from the DeviceSettings
	 * This is set when the MicRecorderToggle Window appears.
	 * 
	 * @param speaker
	 * @return the SourceDataLine which allows playing back audio data from a device
	 */
	public static SourceDataLine getSourceDataLine() {
		return getSourceDataLine(DeviceSettings.getSpeaker());
	}

	/**
	 * Gets the target dataline form the DeviceSettings
	 * 
	 * @return
	 */
	public static TargetDataLine getTargetDataLine() {
		return getTargetDataLine(DeviceSettings.getMicrophone());
	}

	/**
	 * Gets the target dataline form the given DeviceInfo.
	 * 
	 * @return a TargetDataLine which is a InputDevice interface, where the one can
	 *         process AudioInput.
	 */
	public static TargetDataLine getTargetDataLine(DeviceInfo microphone) {
		if (microphone == null) {
			return getDefaultTargetDataLine();
		}

		try {
			Mixer mixer = AudioSystem.getMixer(microphone.mixerInfo);
			DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, microphone.format);

			return (TargetDataLine) mixer.getLine(lineInfo);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

	public static TargetDataLine getDefaultTargetDataLine() {
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			try {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, defaultFormat);
				if (!mixer.isLineSupported(info)) {
					continue;
				}
				return (TargetDataLine) mixer.getLine(info);
			} catch (Exception e) {
				// ignore mixer if unavailable
			}
		}

		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, defaultFormat);
			return (TargetDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			return null;
		}
	}
}
