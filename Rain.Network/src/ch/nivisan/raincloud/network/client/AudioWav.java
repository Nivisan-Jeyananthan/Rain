package ch.nivisan.raincloud.network.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AudioWav {
	private RandomAccessFile raf;
	private long dataSize = 0; 
	private final int sampleRate = 48000;
	private final short channels = 1;
	private final short bitsPerSample = 16;

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

	/**
	 * Writes the header entry for the file.
	 * 1. Clears the file 
	 * 2. Sets ChunkIDs as RIFF
	 * 3. Sets ChunkSize as 0
	 * 4. Sets Format as Wave
	 * 5. Sets SubChunk1ID
	 * 6. Sets SubChunk1Size as pcm
	 * 7. Sets AudioFormat as pcm
	 * 8. Begins with the datachunk
	 * 8. Sets SubChunk2ID called data
	 * 9. Sets SubChunk2Size as 0
	 */
	private void writeHeaderPlaceholder() {
		try {
			raf.setLength(0);

			raf.writeBytes("RIFF");
			writeLEInt(raf, 0); 
			raf.writeBytes("WAVE"); 

			// fmt‑Chunk
			raf.writeBytes("fmt ");
			writeLEInt(raf, 16); 
			writeLEShort(raf, (short) 1);
			writeLEShort(raf, channels);
			writeLEInt(raf, sampleRate);

			int byteRate = sampleRate * channels * bitsPerSample / 8;
			writeLEInt(raf, byteRate);

			short blockAlign = (short) (channels * bitsPerSample / 8);
			writeLEShort(raf, blockAlign);
			writeLEShort(raf, bitsPerSample);

			raf.writeBytes("data"); 
			writeLEInt(raf, 0); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void append(byte[] data) {
		try {
			raf.seek(raf.length());
			raf.write(data);
			dataSize += data.length;

			updateHeader(); 
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

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

	/**
	 * Writes the value of a int or intlike value into the file as byte using little endian
	 * @param raf
	 * @param value
	 */
	private static void writeLEInt(RandomAccessFile raf, int value) {
		try {
			raf.write(value & 0xFF);
			raf.write((value >> 8) & 0xFF);
			raf.write((value >> 16) & 0xFF);
			raf.write((value >> 24) & 0xFF);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Writes the value of a short into the file as byte using little endian
	 * @param raf
	 * @param value
	 */
	private static void writeLEShort(RandomAccessFile raf, short value) {
		try {
			raf.write(value & 0xFF);
			raf.write((value >> 8) & 0xFF);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}