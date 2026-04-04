package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class DeviceSettings {
	private static DeviceInfo microphone;
	private static DeviceInfo speaker;

	private DeviceSettings() {
	}

	public static DeviceInfo getMicrophone() {
		return microphone;
	}

	public static void setMicrophone(DeviceInfo microphone) {
		if (microphone != null)
			DeviceSettings.microphone = microphone;
	}

	public static DeviceInfo getSpeaker() {
		return speaker;
	}

	public static void setSpeaker(DeviceInfo speaker) {
		DeviceSettings.speaker = speaker;
	}
}
