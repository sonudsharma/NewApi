package com.security;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64Encoder;

public final class PasswordService {
	private static PasswordService instance;
	public static int ENCTYPE_SHA = 0;
	public static int ENCTYPE_DES = 1;

	private PasswordService() {
	}

	public synchronized String encrypt(String plaintext) throws Exception {
		return encrypt(plaintext, ENCTYPE_DES);
	}

	public synchronized String encryptMD5(String plaintext) throws Exception {
		MessageDigest md = null;
		md = MessageDigest.getInstance("MD5"); // step 2
		md.update(plaintext.getBytes());

		return HexString.bufferToHex(md.digest());

		// md.update(plaintext.getBytes(Constants.CHARACTER_ENCODING)); //step 3
		// byte raw[] = md.digest(); //step 4
		//
		// ByteArrayOutputStream bout=new ByteArrayOutputStream();
		// new Base64Encoder().encode(raw, 0, raw.length, bout);//step 5
		// return new String(bout.toByteArray()); //step 6
	}

	public String encrypt(String plainText, int encType) throws Exception {
		if (StringUtils.isNotBlank(plainText)) {
			if (encType == ENCTYPE_DES) {
				HQCrypt hqCrypt = HQCrypt.getInstance();
				String result = null;
				try {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					byte[] raw = hqCrypt.encrypt(plainText.getBytes(), HQCrypt.DES_BASE64_KEY_FOR_PASSWORDS);
					new Base64Encoder().encode(raw, 0, raw.length, bout);
					result = new String(bout.toByteArray());
				} catch (Exception e) {
					// System.out.println("error while encrypting:[" + plainText
					// + "]");
					throw e;
				}
				return result;
			} else if (encType == ENCTYPE_SHA) {
				return encrypt(plainText);
			}
		}
		return null;
	}

	public String decrypt(String encryptedPassword) throws Exception {
		if (encryptedPassword.contains("%")) {
			encryptedPassword = URLDecoder.decode(encryptedPassword, "UTF-8");
		}
		HQCrypt hqCrypt = HQCrypt.getInstance();
		String result = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			new Base64Encoder().decode(encryptedPassword.getBytes(), 0, encryptedPassword.getBytes().length, bout);
			result = new String(hqCrypt.decrypt(bout.toByteArray(), HQCrypt.DES_BASE64_KEY_FOR_PASSWORDS));
		} catch (IllegalArgumentException e) {
			// System.out.println("Error while decrypting:[" + encryptedPassword
			// + "]");
			throw e;
		}
		return result;
	}

	public static synchronized PasswordService getInstance() {
		if (instance == null)
			return new PasswordService();

		return instance;
	}
	
	public static void main(String[] a) {
		PasswordService p = PasswordService.getInstance();
		try {
			System.out.println(p.decrypt("mt0HLOEoiGrdzoxprWhI6w=="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
