package com.backend.crame.global.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesGcmCrypto {
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final int GCM_TAG_BITS = 128;
	private static final int IV_BYTES = 12;

	private final SecretKey key;
	private final String keyVersion;

	public AesGcmCrypto(
		@Value("${app.secrets.master-key-base64}") String keyB64,
		@Value("${app.secrets.key-version:kv1}") String keyVersion
	) {
		this.key = new SecretKeySpec(Base64.getDecoder().decode(keyB64), "AES");
		this.keyVersion = keyVersion;
	}

	public String getKeyVersion() { return keyVersion; }

	public String encrypt(String plain) {
		try {
			byte[] iv = SecureRandom.getInstanceStrong().generateSeed(IV_BYTES);
			Cipher c = Cipher.getInstance(TRANSFORMATION);
			c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
			byte[] ct = c.doFinal(plain.getBytes(StandardCharsets.UTF_8));
			byte[] out = ByteBuffer.allocate(iv.length + ct.length).put(iv).put(ct).array();
			return Base64.getEncoder().encodeToString(out);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Secret encrypt failed", e);
		}
	}

	public String decrypt(String stored) {
		try {
			byte[] all = Base64.getDecoder().decode(stored);
			byte[] iv = Arrays.copyOfRange(all, 0, IV_BYTES);
			byte[] ct = Arrays.copyOfRange(all, IV_BYTES, all.length);
			Cipher c = Cipher.getInstance(TRANSFORMATION);
			c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
			return new String(c.doFinal(ct), StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Secret decrypt failed", e);
		}
	}
}
