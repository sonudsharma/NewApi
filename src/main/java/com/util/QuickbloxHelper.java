package com.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class QuickbloxHelper {
	private static String convertMapToDataStr(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		
		Object[] ks = data.keySet().toArray();
		for(int i = 0; i < data.size(); i++) {
			String s = ks[i].toString();
			
			sb.append(s);
			sb.append("=");
			sb.append(data.get(s));
			
			if(i != data.size() - 1) {
				sb.append("&");
			}
		}
		
		sb.append("&user[login]=auronia&user[password]=auro@123");
		
		return sb.toString();
	}
	
	public static String generateSignature(Map<String, Object> data, String authKey) {
		String dataStr = convertMapToDataStr(data);
		
		System.out.println("dataStr " + dataStr);
		String hash = encode(authKey, dataStr);
		System.out.println("Hash " + hash);
		return hash;
	}
	
	private static String encode(String key, String data) {
	    try {

	        Mac sha256_HMAC = Mac.getInstance("HmacSHA1");
	        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
	        sha256_HMAC.init(secret_key);

	        return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	
}
