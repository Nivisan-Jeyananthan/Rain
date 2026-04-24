package ch.nivisan.raincloud.network.utilities;

import javax.sound.sampled.AudioFormat;

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
	 * Handles different sample sizes (8-bit/16-bit) and channel counts
	 * (mono/stereo).
	 */
	public byte[] resample(byte[] audioData) {
		if (audioData == null || audioData.length == 0) {
			return audioData;
		}

		// If all audio format properties match exactly, nothing has to be converted.
		if (matchesExactly(sourceFormat, targetFormat)) {
			return audioData;
		}

		int sourceSampleSizeInBytes = sourceFormat.getSampleSizeInBits() / 8;
		int targetSampleSizeInBytes = targetFormat.getSampleSizeInBits() / 8;
		int sourceChannels = sourceFormat.getChannels();
		int targetChannels = targetFormat.getChannels();

		// Calculate sample counts (per channel)
		int sourceFrameSize = sourceSampleSizeInBytes * sourceChannels;
		int targetFrameSize = targetSampleSizeInBytes * targetChannels;
		int sourceFrameCount = audioData.length / sourceFrameSize;
		int targetFrameCount = (int) (sourceFrameCount * ratio);

		byte[] resampled = new byte[targetFrameCount * targetFrameSize];

		// Resampling with channel handling
		for (int i = 0; i < targetFrameCount; i++) {
			float sourcePosition = i / ratio;
			int sourceFrameIndex = (int) sourcePosition;
			float fraction = sourcePosition - sourceFrameIndex;

			if (sourceFrameIndex >= sourceFrameCount - 1) {
				// Use last frame
				for (int ch = 0; ch < targetChannels; ch++) {
					int sourceCh = Math.min(ch, sourceChannels - 1); // Handle mono->stereo or stereo->mono
					short sample = getSample(audioData, (sourceFrameCount - 1) * sourceChannels + sourceCh,
							sourceSampleSizeInBytes);
					putSample(resampled, i * targetChannels + ch, sample, targetSampleSizeInBytes);
				}
			} else {
				// Linear interpolation between two frames
				for (int ch = 0; ch < targetChannels; ch++) {
					int sourceCh = Math.min(ch, sourceChannels - 1); // Handle mono->stereo or stereo->mono

					short sample1 = getSample(audioData, sourceFrameIndex * sourceChannels + sourceCh,
							sourceSampleSizeInBytes);
					short sample2 = getSample(audioData, (sourceFrameIndex + 1) * sourceChannels + sourceCh,
							sourceSampleSizeInBytes);
					short interpolated = (short) (sample1 * (1.0f - fraction) + sample2 * fraction);
					putSample(resampled, i * targetChannels + ch, interpolated, targetSampleSizeInBytes);
				}
			}
		}

		return resampled;
	}

	private short getSample(byte[] data, int sampleIndex, int sampleSizeInBytes) {
		int byteIndex = sampleIndex * sampleSizeInBytes;
		if (sampleSizeInBytes == 2) {
			// 16-bit samples
			if (sourceFormat.isBigEndian()) {
				return (short) ((data[byteIndex] & 0xFF) << 8 | (data[byteIndex + 1] & 0xFF));
			} else {
				return (short) ((data[byteIndex + 1] & 0xFF) << 8 | (data[byteIndex] & 0xFF));
			}
		} else if (sampleSizeInBytes == 1) {
			// 8-bit samples (convert to 16-bit)
			byte sample8 = data[byteIndex];
			return (short) (sample8 * 256); // Scale 8-bit to 16-bit range
		}
		return 0;
	}

	private void putSample(byte[] data, int sampleIndex, short sample, int sampleSizeInBytes) {
		int byteIndex = sampleIndex * sampleSizeInBytes;
		if (sampleSizeInBytes == 2) {
			// 16-bit samples
			if (targetFormat.isBigEndian()) {
				data[byteIndex] = (byte) ((sample >> 8) & 0xFF);
				data[byteIndex + 1] = (byte) (sample & 0xFF);
			} else {
				data[byteIndex] = (byte) (sample & 0xFF);
				data[byteIndex + 1] = (byte) ((sample >> 8) & 0xFF);
			}
		} else if (sampleSizeInBytes == 1) {
			// 8-bit samples (convert from 16-bit)
			data[byteIndex] = (byte) (sample / 256); // Scale 16-bit to 8-bit range
		}
	}

	/**
	 * Check if resampling / format conversion is needed
	 */
	public boolean needsResampling() {
		return !matchesExactly(sourceFormat, targetFormat);
	}

	private boolean matchesExactly(AudioFormat a, AudioFormat b) {
		return a.getSampleRate() == b.getSampleRate()
				&& a.getSampleSizeInBits() == b.getSampleSizeInBits()
				&& a.getChannels() == b.getChannels()
				&& a.isBigEndian() == b.isBigEndian()
				&& a.getEncoding().equals(b.getEncoding());
	}
}
