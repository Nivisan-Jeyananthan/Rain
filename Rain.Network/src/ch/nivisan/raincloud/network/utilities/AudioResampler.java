package ch.nivisan.raincloud.network.utilities;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;

/**
 * Handles audio resampling between different sample rates.
 * Converts audio data to a target sample rate using linear interpolation.
 */
public class AudioResampler {

	private final AudioFormat sourceFormat;
	private final AudioFormat targetFormat;
	private float ratio;

	public AudioResampler(AudioFormat sourceFormat, AudioFormat targetFormat) {
		this.sourceFormat = sourceFormat;
		this.targetFormat = targetFormat;
		this.ratio = targetFormat.getSampleRate() / sourceFormat.getSampleRate();
	}

	/**
	 * Resample audio data from source format to target format.
	 * Uses linear interpolation for smooth resampling.
	 */
	public byte[] resample(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		// If sample rates are the same, no resampling needed
		if (Math.abs(sourceFormat.getSampleRate() - targetFormat.getSampleRate()) < 1.0f) {
			return audioData;
		}

		int sampleSizeInBytes = sourceFormat.getSampleSizeInBits() / 8;
		int sourceSampleCount = audioData.length / sampleSizeInBytes;
		int targetSampleCount = (int) (sourceSampleCount * ratio);

		byte[] resampled = new byte[targetSampleCount * sampleSizeInBytes];

		// Simple linear interpolation resampling
		for (int i = 0; i < targetSampleCount; i++) {
			float sourcePosition = i / ratio;
			int sourceSampleIndex = (int) sourcePosition;
			float fraction = sourcePosition - sourceSampleIndex;

			if (sourceSampleIndex >= sourceSampleCount - 1) {
				// Use last sample
				int sourceByteIndex = (sourceSampleCount - 1) * sampleSizeInBytes;
				System.arraycopy(audioData, sourceByteIndex, resampled, i * sampleSizeInBytes, sampleSizeInBytes);
			} else {
				// Linear interpolation between two samples
				short sample1 = getSample(audioData, sourceSampleIndex, sampleSizeInBytes);
				short sample2 = getSample(audioData, sourceSampleIndex + 1, sampleSizeInBytes);
				short interpolated = (short) (sample1 * (1.0f - fraction) + sample2 * fraction);
				putSample(resampled, i, interpolated, sampleSizeInBytes);
			}
		}

		return resampled;
	}

	private short getSample(byte[] data, int sampleIndex, int sampleSizeInBytes) {
		int byteIndex = sampleIndex * sampleSizeInBytes;
		if (sampleSizeInBytes == 2) {
			return (short) ((data[byteIndex + 1] & 0xFF) << 8 | (data[byteIndex] & 0xFF));
		}
		return 0;
	}

	private void putSample(byte[] data, int sampleIndex, short sample, int sampleSizeInBytes) {
		int byteIndex = sampleIndex * sampleSizeInBytes;
		if (sampleSizeInBytes == 2) {
			data[byteIndex] = (byte) (sample & 0xFF);
			data[byteIndex + 1] = (byte) ((sample >> 8) & 0xFF);
		}
	}

	/**
	 * Check if resampling is needed
	 */
	public boolean needsResampling() {
		return Math.abs(sourceFormat.getSampleRate() - targetFormat.getSampleRate()) > 1.0f;
	}
}
