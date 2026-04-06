package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.AudioFormat;
import ch.nivisan.raincloud.network.utilities.Audio;

/**
 * Enum for available audio formats.
 * Allows users to choose between standard, fallback, and legacy audio formats.
 */
public enum AudioFormatType {
	STANDARD("Standard (48 kHz, 16-bit, Mono)", Audio.defaultFormat, false, false),
	FALLBACK("Fallback (44.1 kHz, 16-bit, Mono)", Audio.fallbackFormat, false, true),
	LEGACY("Legacy (16 kHz, 8-bit, Stereo)", Audio.oldFormat, true, false);

	private final String displayName;
	private final AudioFormat format;
	private final boolean isLegacy;
	private final boolean isFallback;

	AudioFormatType(String displayName, AudioFormat format, boolean isLegacy, boolean isFallback) {
		this.displayName = displayName;
		this.format = format;
		this.isLegacy = isLegacy;
		this.isFallback = isFallback;
	}

	public String getDisplayName() {
		return displayName;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public boolean isLegacy() {
		return isLegacy;
	}

	public boolean isFallback() {
		return isFallback;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
