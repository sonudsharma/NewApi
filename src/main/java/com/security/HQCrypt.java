package com.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64Encoder;

public class HQCrypt {

	private static HQCrypt _crypt = null;
	public static final String AES_BASE64_KEY_FOR_PASSWORDS = "/DCGO6l05NKWYbWCmS7DuA==";
	public static final String DES_BASE64_KEY_FOR_PASSWORDS = "UnbEbuqGsIPgRiYsT4BdsyzlI53jlF0B";
	public static final String MD5_BASE64_KEY_FOR_DIBS = "D60164C157CEA1B361C7C996C2FF163A9D7B7E46";
	private static final String XFORM = "DESede/ECB/PKCS5Padding";
	private static final String ALGORITHM = "DESede";

	private HQCrypt() {
	}

	public static synchronized HQCrypt getInstance() throws Exception {
		if (_crypt == null) {
			_crypt = new HQCrypt();
			KeyGenerator kgen = KeyGenerator.getInstance("DESede");
			kgen.init(168);

			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			new Base64Encoder().encode(raw, 0, raw.length, bout);
			////System.out.println("key : ["+new String(bout.toByteArray())+"]");
		}
		return _crypt;
	}

	public String getSecretKey() throws NoSuchAlgorithmException, IOException {
		KeyGenerator kgen = KeyGenerator.getInstance("DESede");
		kgen.init(168);

		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		new Base64Encoder().encode(raw, 0, raw.length, bout);
		return new String(bout.toByteArray());
	}

	public byte[] encrypt(byte[] input, String key) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		new Base64Encoder().decode(key.getBytes(), 0, key.getBytes().length, bout);
		byte[] raw = bout.toByteArray();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher c = Cipher.getInstance(XFORM);
		c.init(Cipher.ENCRYPT_MODE, skeySpec);
		return c.doFinal(input);
	}

	public byte[] decrypt(byte[] input, String key) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		new Base64Encoder().decode(key.getBytes(), 0, key.getBytes().length, bout);
		byte[] raw = bout.toByteArray();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher c = Cipher.getInstance(XFORM);
		c.init(Cipher.DECRYPT_MODE, skeySpec);
		return c.doFinal(input);
	}

	public String decrypt(String encryptedPassword, String key) throws Exception {
		String result = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			new Base64Encoder().decode(encryptedPassword.getBytes(), 0, encryptedPassword.getBytes().length, bout);

			result = new String(decrypt(bout.toByteArray(), key));
		} catch (IllegalArgumentException e) {
			//System.out.println("Error while decrypting:[" + encryptedPassword + "]");
			throw e;
		}
		return result;
	}

}
