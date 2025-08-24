package com.example.Pill_Mate_Backend.domain.oauth2.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenCipher {
    private final byte[] key;

    public TokenCipher(@Value("${security.token-secret}") String secret32) {
        this.key = secret32.getBytes();
    }
    public String encrypt(String plain) {
        try {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);
            var cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
            byte[] enc = cipher.doFinal(plain.getBytes());
            byte[] out = new byte[iv.length + enc.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(enc, 0, out, iv.length, enc.length);
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String decrypt(String b64) {
        try {
            byte[] in = Base64.getDecoder().decode(b64);
            byte[] iv = new byte[12];
            byte[] enc = new byte[in.length - 12];
            System.arraycopy(in, 0, iv, 0, 12);
            System.arraycopy(in, 12, enc, 0, enc.length);
            var cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(enc));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}