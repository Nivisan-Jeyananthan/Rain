package ch.nivisan.raincloud.network.client;

public class DeviceSettings {
	private static DeviceInfo microphone;
	private static DeviceInfo speaker;
	private static AudioFormatType audioFormat = AudioFormatType.STANDARD;

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

	public static AudioFormatType getAudioFormat() {
		return audioFormat;
	}

	public static void setAudioFormat(AudioFormatType format) {
		if (format != null) {
			DeviceSettings.audioFormat = format;
		}
	}
}
