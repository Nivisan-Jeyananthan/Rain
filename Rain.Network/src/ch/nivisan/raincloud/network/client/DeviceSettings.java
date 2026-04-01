package ch.nivisan.raincloud.network.client;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
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

	public static TargetDataLine geTargetDataLine() {
		if (microphone == null)
			return null;
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

}
