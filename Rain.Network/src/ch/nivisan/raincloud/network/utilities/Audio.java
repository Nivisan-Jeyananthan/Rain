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
	public final static int stereoChannels = 2;
	public final static int frameSize = sampleSizeInBits / 8;
	public final static int sendRateInMs = 15;
	public final static int secondsInMs = 1000;
	public final static int bufferSize = (int) ((sampleRate * frameSize * sendRateInMs) / secondsInMs);

	// Network Primary Format (48kHz, 16-bit, Mono, Little-Endian) used for
	// transmission over network (optimal bandwidth)
	public final static AudioFormat defaultFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate,
			sampleSizeInBits, channels, frameSize, sampleRate, false);

	// Device Recording/Playback Formats (Stereo for better hardware compatibility)
	// These are optimized for recording/playback on various devices
	public final static AudioFormat defaultFormatStereo = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate,
			sampleSizeInBits, stereoChannels, frameSize * stereoChannels, sampleRate, false);

	// Standard Fallback Formats 44.1kHz: CD-quality, widely supported
	public final static AudioFormat fallbackFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f,
			sampleSizeInBits, channels, frameSize, 44100.0f, false);

	public final static AudioFormat fallbackFormatStereo = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f,
			sampleSizeInBits, stereoChannels, frameSize * stereoChannels, 44100.0f, false);

	// 32kHz: Best compatibility for older Windows and embedded devices
	public final static AudioFormat compatFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 32000.0f,
			sampleSizeInBits, channels, frameSize, 32000.0f, false);

	public final static AudioFormat compatFormatStereo = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 32000.0f,
			sampleSizeInBits, stereoChannels, frameSize * stereoChannels, 32000.0f, false);

	// Higher Quality Format 96kHz: HiFi audio for high-end devices
	public final static AudioFormat higherFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 96000.0f,
			sampleSizeInBits, channels, frameSize, 96000.0f, false);

	public final static AudioFormat higherFormatStereo = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 96000.0f,
			sampleSizeInBits, stereoChannels, frameSize * stereoChannels, 96000.0f, false);

	// Legacy Format (Very Old Hardware) 16kHz, 8-bit, Stereo, Little-Endian
	// (improved: now little-endian by default)
	@Deprecated
	public final static AudioFormat oldFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000,
			8, 2, 2, 16000, false);

	/**
	 * Detects the best compatible audio format for the current device.
	 * Tries formats in order of compatibility, falling back to more compatible
	 * ones.
	 * 
	 * @return an AudioFormat that the device is likely to support
	 */
	public static AudioFormat detectBestFormat() {
		// Try to use stereo on recording/playback devices for better compatibility
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			try {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);

				// Check for stereo 48kHz support
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, defaultFormatStereo);
				if (mixer.isLineSupported(info)) {
					return defaultFormatStereo; // Best: 48kHz Stereo
				}

				// Check for 32kHz stereo (good for older hardware)
				info = new DataLine.Info(TargetDataLine.class, compatFormatStereo);
				if (mixer.isLineSupported(info)) {
					return compatFormatStereo; // Good: 32kHz Stereo
				}

				// Check for 44.1kHz stereo
				info = new DataLine.Info(TargetDataLine.class, fallbackFormatStereo);
				if (mixer.isLineSupported(info)) {
					return fallbackFormatStereo; // Fallback: 44.1kHz Stereo
				}
			} catch (Exception e) {
				// Ignore unsupported mixer
			}
		}

		// If no device-specific format works, return sensible defaults
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, compatFormat);
			if (AudioSystem.getLine(info) != null) {
				return compatFormat; // 32kHz Mono is most universally supported
			}
		} catch (Exception e) {
			// Continue to final fallback
		}

		return defaultFormat; // Final fallback: 48kHz Mono (standard)
	}

	/**
	 * Flag to indicate if the client should use legacy audio format.
	 * When true, audio will be sent/received in oldFormat and resampled
	 * accordingly.
	 * 
	 * @deprecated Use DeviceSettings.getAudioFormat() instead for dynamic format
	 *             selection.
	 */
	public static boolean useLegacyFormat = false;

	/**
	 * Flag to indicate if the client should use fallback audio format (44.1kHz).
	 * When true, audio will be sent/received in fallbackFormat and resampled
	 * accordingly.
	 * 
	 * @deprecated Use DeviceSettings.getAudioFormat() instead for dynamic format
	 *             selection.
	 */
	public static boolean useFallbackFormat = false;

	/**
	 * Flag to indicate if the client should use higher audio format (96kHz).
	 * When true, audio will be sent in higher format and resampled to defaultFormat
	 * (48kHz) for transmission.
	 * Received audio will be resampled back to higher format for playback.
	 */
	public static boolean useHigherSampleRate = false;

	/**
	 * Helper method to determine if fallback format should be used based on
	 * DeviceSettings.
	 * 
	 * @return true if fallback format is selected
	 */
	public static boolean shouldUseFallbackFormat() {
		try {
			return DeviceSettings.getInputFormat().isFallback();
		} catch (Exception e) {
			return useFallbackFormat;
		}
	}

	/**
	 * Helper method to determine if fallback format should be used based on
	 * DeviceSettings.
	 * 
	 * @return true if fallback format is selected
	 */
	public static boolean shouldUseLegacyFormat() {
		try {
			return DeviceSettings.getInputFormat().isLegacy();
		} catch (Exception e) {
			return useLegacyFormat;
		}
	}

	/**
	 * Resamples audio data from the default format to the currently selected
	 * format.
	 * Automatically handles resampling based on DeviceSettings.getAudioFormat().
	 * 
	 * @param audioData the audio data in defaultFormat
	 * @return resampled audio data in the selected format
	 */
	public static byte[] resampleToSelectedFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		try {
			ch.nivisan.raincloud.network.client.AudioFormatType format = DeviceSettings.getInputFormat();
			if (format.isLegacy()) {
				return resampleToOldFormat(audioData);
			} else if (format.isFallback()) {
				return resampleToFallbackFormat(audioData);
			} else if (format.isCompat()) {
				return resampleToCompatFormat(audioData);
			} else if (format.isHigher()) {
				return resampleToHigherSampleRate(audioData);
			}
		} catch (Exception e) {
			// Fallback to deprecated flags
			if (shouldUseLegacyFormat()) {
				return resampleToOldFormat(audioData);
			} else if (shouldUseFallbackFormat()) {
				return resampleToFallbackFormat(audioData);
			}
		}

		return audioData;
	}

	/**
	 * Resamples audio data from the currently selected format to the default
	 * format.
	 * Automatically handles resampling based on DeviceSettings.getAudioFormat().
	 * 
	 * @param audioData the audio data in the selected format
	 * @return resampled audio data in defaultFormat
	 */
	public static byte[] resampleFromSelectedFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		try {
			ch.nivisan.raincloud.network.client.AudioFormatType format = DeviceSettings.getInputFormat();
			if (format.isLegacy()) {
				return resampleFromOldFormat(audioData);
			} else if (format.isFallback()) {
				return resampleFromFallbackFormat(audioData);
			} else if (format.isCompat()) {
				return resampleFromCompatFormat(audioData);
			} else if (format.isHigher()) {
				return resampleFromHigherSampleRate(audioData);
			}
		} catch (Exception e) {
			// Fallback to deprecated flags
			if (shouldUseLegacyFormat()) {
				return resampleFromOldFormat(audioData);
			} else if (shouldUseFallbackFormat()) {
				return resampleFromFallbackFormat(audioData);
			}
		}

		return audioData;
	}

	public static byte[] resampleToOutputFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		try {
			ch.nivisan.raincloud.network.client.AudioFormatType format = DeviceSettings.getOutputFormat();
			if (format.isLegacy()) {
				return resampleToOldFormat(audioData);
			} else if (format.isFallback()) {
				return resampleToFallbackFormat(audioData);
			} else if (format.isCompat()) {
				return resampleToCompatFormat(audioData);
			} else if (format.isHigher()) {
				return resampleToHigherSampleRate(audioData);
			}
		} catch (Exception e) {
			// No output format configured, fallback to default
		}

		return audioData;
	}

	public static byte[] resampleToFormat(byte[] audioData, AudioFormat targetFormat) {
		if (audioData == null || audioData.length == 0 || targetFormat == null) {
			return audioData;
		}

		AudioResampler resampler = new AudioResampler(defaultFormat, targetFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the old legacy format (16kHz, 8-bit, stereo) to the
	 * default format (48kHz, 16-bit, mono).
	 * 
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
	 * Resamples audio data from the default format (48kHz, 16-bit, mono) to the old
	 * legacy format (16kHz, 8-bit, stereo).
	 * 
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
	 * Resamples audio data from the fallback format (44.1kHz, 16-bit, mono) to the
	 * default format (48kHz, 16-bit, mono).
	 * 
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
	 * Resamples audio data from the default format (48kHz, 16-bit, mono) to the
	 * fallback format (44.1kHz, 16-bit, mono).
	 * 
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

	/**
	 * Resamples audio data from the higher format (96kHz, 16-bit, mono) to the
	 * default format (48kHz, 16-bit, mono).
	 * 
	 * @param audioData the audio data in higherFormat
	 * @return resampled audio data in defaultFormat
	 */
	public static byte[] resampleFromHigherSampleRate(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		AudioResampler resampler = new AudioResampler(higherFormat, defaultFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the default format (48kHz, 16-bit, mono) to the
	 * higher format (96kHz, 16-bit, mono).
	 * 
	 * @param audioData the audio data in defaultFormat
	 * @return resampled audio data in higherFormat
	 */
	public static byte[] resampleToHigherSampleRate(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		AudioResampler resampler = new AudioResampler(defaultFormat, higherFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the compatibility format (32kHz, 16-bit, mono) to
	 * the default format (48kHz, 16-bit, mono).
	 * Used for older Windows systems and embedded devices.
	 * 
	 * @param audioData the audio data in compatFormat
	 * @return resampled audio data in defaultFormat
	 */
	public static byte[] resampleFromCompatFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		AudioResampler resampler = new AudioResampler(compatFormat, defaultFormat);
		return resampler.resample(audioData);
	}

	/**
	 * Resamples audio data from the default format (48kHz, 16-bit, mono) to the
	 * compatibility format (32kHz, 16-bit, mono).
	 * Used for older Windows systems and embedded devices.
	 * 
	 * @param audioData the audio data in defaultFormat
	 * @return resampled audio data in compatFormat
	 */
	public static byte[] resampleToCompatFormat(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		AudioResampler resampler = new AudioResampler(defaultFormat, compatFormat);
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
	 * 
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
		// Try formats in order of preference: Stereo formats first (better device
		// compatibility), then Mono
		AudioFormat[] formatsTryOrder = new AudioFormat[] {
				defaultFormatStereo, // 48kHz Stereo (primary)
				compatFormatStereo, // 32kHz Stereo (better for old hardware)
				fallbackFormatStereo, // 44.1kHz Stereo (CD quality)
				higherFormatStereo, // 96kHz Stereo (HiFi)
				defaultFormat, // 48kHz Mono (fallback)
				fallbackFormat, // 44.1kHz Mono (fallback)
				compatFormat // 32kHz Mono (final fallback)
		};

		for (AudioFormat format : formatsTryOrder) {
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				try {
					Mixer mixer = AudioSystem.getMixer(mixerInfo);
					DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
					if (mixer.isLineSupported(info)) {
						return (SourceDataLine) mixer.getLine(info);
					}
				} catch (Exception e) {
					// ignore mixer if unavailable
				}
			}
		}

		// Fallback: Try SystemAudioInputStream with all formats
		for (AudioFormat format : formatsTryOrder) {
			try {
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
				return (SourceDataLine) AudioSystem.getLine(info);
			} catch (Exception e) {
				// continue to next format
			}
		}

		return null; // No compatible device found
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
		// Try formats in order of preference: Stereo formats first (better device
		// compatibility), then Mono
		AudioFormat[] formatsTryOrder = new AudioFormat[] {
				defaultFormatStereo, // 48kHz Stereo (primary)
				higherFormatStereo, // 96kHz Stereo (HiFi)
				compatFormatStereo, // 32kHz Stereo (better for old hardware)
				fallbackFormatStereo, // 44.1kHz Stereo (CD quality)
				defaultFormat, // 48kHz Mono (fallback)
				fallbackFormat, // 44.1kHz Mono (fallback)
				compatFormat // 32kHz Mono (final fallback)
		};

		for (AudioFormat format : formatsTryOrder) {
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				try {
					Mixer mixer = AudioSystem.getMixer(mixerInfo);
					DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
					if (mixer.isLineSupported(info)) {
						return (TargetDataLine) mixer.getLine(info);
					}
				} catch (Exception e) {
					// ignore mixer if unavailable
				}
			}
		}

		// Fallback: Try SystemAudioInputStream with all formats
		for (AudioFormat format : formatsTryOrder) {
			try {
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
				return (TargetDataLine) AudioSystem.getLine(info);
			} catch (Exception e) {
				// continue to next format
			}
		}

		return null; // No compatible device found
	}
}
