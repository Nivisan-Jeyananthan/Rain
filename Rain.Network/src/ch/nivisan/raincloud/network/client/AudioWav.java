package ch.nivisan.raincloud.network.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AudioWav {
	private RandomAccessFile raf;
	private long dataSize = 0; // Bytes im Daten‑Chunk
	private final int sampleRate = 48000;
	private final short channels = 1;
	private final short bitsPerSample = 16;

	// ------------------------------------------------------------
	public AudioWav(File file) {
		try {
			raf = new RandomAccessFile(file, "rw");
			writeHeaderPlaceholder();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------
	private void writeHeaderPlaceholder() {
		try {
			raf.setLength(0);

			raf.writeBytes("RIFF"); // ChunkID
			writeLEInt(raf, 0); // ChunkSize (noch 0)
			raf.writeBytes("WAVE"); // Format

			// fmt‑Chunk
			raf.writeBytes("fmt "); // Subchunk1ID
			writeLEInt(raf, 16); // Subchunk1Size (PCM)
			writeLEShort(raf, (short) 1); // AudioFormat = PCM
			writeLEShort(raf, channels);
			writeLEInt(raf, sampleRate);

			int byteRate = sampleRate * channels * bitsPerSample / 8;
			writeLEInt(raf, byteRate);

			short blockAlign = (short) (channels * bitsPerSample / 8);
			writeLEShort(raf, blockAlign);
			writeLEShort(raf, bitsPerSample);

			// data‑Chunk
			raf.writeBytes("data"); // Subchunk2ID
			writeLEInt(raf, 0); // Subchunk2Size (noch 0)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------
	public synchronized void append(byte[] data) {
		try {
			raf.seek(raf.length());
			raf.write(data);
			dataSize += data.length;

			updateHeader(); // Header patchen
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ans Ende

	}

	// ------------------------------------------------------------
	private void updateHeader() {

		try {
			long chunkSize = 36 + dataSize; // 4 + (8+Subchunk1Size) + (8+dataSize)
			raf.seek(4);
			writeLEInt(raf, (int) chunkSize);
			raf.seek(40);
			writeLEInt(raf, (int) dataSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ------------------------------------------------------------
	public synchronized void close() {
		updateHeader(); // letztes Update
		try {
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------
	private static void writeLEInt(RandomAccessFile raf, int value) {
		try {
			raf.write(value & 0xFF);
			raf.write((value >> 8) & 0xFF);
			raf.write((value >> 16) & 0xFF);
			raf.write((value >> 24) & 0xFF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeLEShort(RandomAccessFile raf, short value) {

		try {
			raf.write(value & 0xFF);
			raf.write((value >> 8) & 0xFF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}