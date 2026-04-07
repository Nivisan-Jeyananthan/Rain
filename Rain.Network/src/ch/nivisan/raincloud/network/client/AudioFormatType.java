package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.AudioFormat;
import ch.nivisan.raincloud.network.utilities.Audio;

/**
 * Enum for available audio formats.
 * Allows users to choose between standard, fallback, and legacy audio formats.
 */
public enum AudioFormatType {
	STANDARD("Standard (48 kHz, 16-bit, Mono)", Audio.defaultFormat, false, false, false),
	FALLBACK("Fallback (44.1 kHz, 16-bit, Mono)", Audio.fallbackFormat, false, true, false),
	LEGACY("Legacy (16 kHz, 8-bit, Stereo)", Audio.oldFormat, true, false, false),
	HIGHER("Higher 96 kHz, 16-bit Mono", Audio.higherFormat, false, false, true);

	private final String displayName;
	private final AudioFormat format;
	private final boolean isLegacy;
	private final boolean isFallback;
	private final boolean isHigher;

	AudioFormatType(String displayName, AudioFormat format, boolean isLegacy, boolean isFallback, boolean isHigher) {
		this.displayName = displayName;
		this.format = format;
		this.isLegacy = isLegacy;
		this.isFallback = isFallback;
		this.isHigher = isHigher;
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

	public boolean isHigher(){
		return isHigher;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
