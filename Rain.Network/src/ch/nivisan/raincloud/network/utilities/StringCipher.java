package ch.nivisan.raincloud.network.utilities;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StringCipher {
	public static int offset = 0;
	private final static String algorithm = "AES";
	private final static String transformation = algorithm + "/CFB8/NoPadding";
	private final static String RSA = "RSA";

	public static void listProviders() {
		Set<String> algoSet = new TreeSet<String>();
		for (Provider provider : Security.getProviders()) {
			Set<Provider.Service> service = provider.getServices();
			service.stream().map(Provider.Service::getAlgorithm).forEach(algoSet::add);
		}
		algoSet.forEach(System.out::println);
	}

	public static SecretKey generateKey() {

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
			keyGenerator.init(256);
			return keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Generates the starting variable for the initial state also known as
	 * initilization vector, which requires it to be random, sometimes just
	 * unpredictable
	 * 
	 * @return
	 */
	public static IvParameterSpec generateIv() {
		byte[] initializationVector = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(initializationVector);
		return new IvParameterSpec(initializationVector);
	}

	public static byte[] encrypt(String input, SecretKey key, IvParameterSpec iv) {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			return null;
		}
	}

	public static String decrypt(byte[] cipherText, SecretKey key, IvParameterSpec iv) {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] plainText = cipher.doFinal(cipherText);
			return new String(plainText);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static String encodeString(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	public static SecretKey decodeSecretKey(byte[] keyBytes) {
		return new SecretKeySpec(keyBytes, algorithm);
	}

	public static String encode32(byte[] data) {
		Base64.Encoder base32 = Base64.getUrlEncoder().withoutPadding();

		byte[] encodedByes = base32.encode(data);
		return new String(encodedByes, StandardCharsets.UTF_8);
	}

	public static byte[] decodeString(String data) {
		return Base64.getDecoder().decode(data);
	}

	public static KeyPair generateRSAKey() {
		try {
			KeyPairGenerator keypairGenerator = KeyPairGenerator.getInstance(RSA);
			keypairGenerator.initialize(3072);
			return keypairGenerator.generateKeyPair();
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] encryptRSA(String text, PublicKey key) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			return null;
		}
	}

	public static String decryptRSA(byte[] cipherText, PrivateKey key) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(cipherText);
			return new String(result);
		} catch (Exception e) {
			return null;
		}
	}

	private static final int dataOffset = 3;

	public static byte[] encryptData(String text) {
		if (text == null)
			return null;

		int pointer = 1;
		byte[] temp = new byte[1024];
		byte[] result = new byte[1024];
		final int N = 1021; // nutzbare Range 1..1023
		char[] characters = text.toCharArray();
		int shift = new Random().nextInt(10); // 0..9

		if ((characters.length * 2) > N)
			return null;

		result[0] = (byte) shift;
		pointer = SerializationWriter.writeBytes(result, pointer, (short) characters.length);
		pointer = SerializationWriter.copyBytes(temp, 0, characters);

		for (int i = 0; i < N; i++) {
			int tempIndex = dataOffset + ((i + shift) % N);
			result[tempIndex++] = temp[i];
		}

		return result;
	}

	public static String decryptData(byte[] data) {
		final int[] pointer = new int[] { 0 };
		if (data == null || data.length != 1024)
			throw new IllegalArgumentException("data must be 1024 bytes");

		int shift = Byte.toUnsignedInt(data[pointer[0]++]);
		short length = SerializationReader.readShort(data, pointer[0]);
		pointer[0] += 2;

		final int N = 1021;
		byte[] temp = new byte[1024];

		for (int i = 0; i < N; i++) {
			int src = 1 + Math.floorMod(i + shift, N);
			temp[dataOffset + i] = data[src];
		}

		char[] characters = SerializationReader.readCharArray(temp, pointer, length);
		return new String(characters);
	}
}
