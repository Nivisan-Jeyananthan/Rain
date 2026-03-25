package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

public class DeviceInfo {
	public final Mixer.Info mixerInfo;
	public final AudioFormat format;

	public DeviceInfo(Mixer.Info mixer, AudioFormat fmt) {
		this.mixerInfo = mixer;
		this.format = fmt;
	}

	@Override
	public String toString() {
		return mixerInfo.getName() + " (" + (int) format.getSampleRate() + " Hz, " + format.getChannels()
				+ "-Kanal)";
	}
}