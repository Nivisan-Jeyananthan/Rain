package ch.nivisan.raincloud.network.client;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class MikrofonRecorder {
    private static final float SAMPLE_RATE      = 44100.0f;   // Hz
    private static final int  SAMPLE_SIZE_IN_BITS = 16;      // Bits pro Sample
    private static final int  CHANNELS          = 1;       
    private static final boolean SIGNED        = true;     // Signed PCM
    private static final boolean BIG_ENDIAN    = false;    // little‑Endian (typisch für WAV)

    private static final AudioFormat AUDIO_FORMAT =
            new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS,
                            CHANNELS, SIGNED, BIG_ENDIAN);
    
    public static void main(String[] args) {
        try {
            recordToFile(new File("mikrofon.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------
    // 3. Aufnahme in eine WAV‑Datei
    // --------------------------------------------------------------
    private static void recordToFile(File wavFile) throws Exception{
        // 3.1: Prüfen, ob das gewünschte Format überhaupt unterstützt wird
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Das Audio‑Format wird von Ihrem Mikrofon nicht unterstützt.");
            return;
        }

        // 3.2: TargetDataLine öffnen und starten
        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(AUDIO_FORMAT);          // optional: line.open(AUDIO_FORMAT, bufferSize);
            line.start();                     // Aufnahme starten

            // 3.3: AudioInputStream aus der Line erzeugen
            try (AudioInputStream ais = new AudioInputStream(line)) {
                System.out.println("Aufnahme läuft – drücken Sie ENTER zum Stoppen…");
                // 3.4: Die Daten in eine WAV‑Datei schreiben
                Thread writer = new Thread(() -> {
                    try {
                        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                writer.start();

                // 3.5: Auf Enter warten – dann die Line stoppen
                System.in.read();          // blockiert bis ENTER gedrückt

                line.stop();
                line.close();

                System.out.println("Aufnahme beendet: " + wavFile.getAbsolutePath());
            }
        }
    }
}
