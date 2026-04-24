package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

public class DeviceInfo {
	public final Mixer.Info mixerInfo;
	public final AudioFormat format;

	DeviceInfo(Mixer.Info mixer, AudioFormat fmt) {
		this.mixerInfo = mixer;
		this.format = fmt;
	}

	@Override
	public String toString() {
		return mixerInfo.getName() + " (" + (int) format.getSampleRate() + " Hz, " + format.getChannels()
				+ "-Kanal)";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		DeviceInfo other = (DeviceInfo) obj;
		return mixerInfo.getName().equals(other.mixerInfo.getName())
				&& format.getSampleRate() == other.format.getSampleRate()
				&& format.getSampleSizeInBits() == other.format.getSampleSizeInBits()
				&& format.getChannels() == other.format.getChannels()
				&& format.isBigEndian() == other.format.isBigEndian();
	}

	@Override
	public int hashCode() {
		int result = mixerInfo.getName().hashCode();
		result = 31 * result + Float.hashCode(format.getSampleRate());
		result = 31 * result + format.getSampleSizeInBits();
		result = 31 * result + format.getChannels();
		result = 31 * result + Boolean.hashCode(format.isBigEndian());
		return result;
	}
}
