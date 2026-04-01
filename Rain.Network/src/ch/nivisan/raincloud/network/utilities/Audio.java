package ch.nivisan.raincloud.network.utilities;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

public class Audio {
    public final static float sampleRate = 48000.0f;
    public final static int sampleSizeInBits = 16;
    public final static int channels = 1;
    public final static int frameSize = 2;
    public final static int sendRateInMs = 8;
    public final static int secondsInMs = 1000;
    public final static int bufferSize = (int) ((sampleRate * frameSize * sendRateInMs) / secondsInMs);

    public final static AudioFormat defaultFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            sampleRate, sampleSizeInBits, channels, frameSize,
            sampleRate,
            false);
}
