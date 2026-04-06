package ch.nivisan.raincloud.network.client;

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
