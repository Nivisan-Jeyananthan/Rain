package ch.nivisan.raincloud.network.utilities;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CipherMain {

	public static void main(String[] args) {
		SecretKey symmetricKey = StringCipher.generateKey();
		IvParameterSpec iv = StringCipher.generateIv();

		if (symmetricKey == null || iv == null)
			return;

		String message = "Somethingl";
		byte[] cipherText = StringCipher.encrypt(message, symmetricKey, iv);
		if (cipherText == null) {
			return;
		}
		IO.println("The encrypted message is: " + cipherText);

		String encodedString = StringCipher.encodeString(cipherText);
		IO.println("The encoded message is: " + encodedString);

		byte[] decodedString = StringCipher.decodeString(encodedString);
		IO.println("The decoded message is: " + decodedString);

		String originalMessage = StringCipher.decrypt(decodedString, symmetricKey, iv);
		IO.println("The decrypted message is: " + originalMessage);

		KeyPair keyPair = StringCipher.generateRSAKey();
		System.out.println("Bytes : " + StringCipher.encodeString(keyPair.getPublic().getEncoded()));
		System.out.println("Length : " + StringCipher.encodeString(keyPair.getPublic().getEncoded()).length());

		byte[] rsaCipherText = StringCipher.encryptRSA(originalMessage, keyPair.getPublic());

		String rsaString = StringCipher.encodeString(rsaCipherText);

		byte[] rsaTransferedText = StringCipher.decodeString(rsaString);

		StringCipher.decryptRSA(rsaCipherText, keyPair.getPrivate());

		IO.println("prints bytes: ");
		String encodeText = "test data";
		var data = StringCipher.encryptData(encodeText);
		printBytes(data);

		FileService.saveToFile("file.txt", data);

		IO.println();
		IO.println(StringCipher.decryptData(data));

	}

	private static void printHex(int value) {
		System.out.printf("%x\n", value);
	}

	static void printBytes(byte[] data) {
		for (int i = 0; i < data.length; i++) {
			System.out.printf("0x%x ", data[i]);
		}
	}
}
