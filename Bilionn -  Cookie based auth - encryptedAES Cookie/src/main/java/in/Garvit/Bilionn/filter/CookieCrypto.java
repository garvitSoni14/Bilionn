package in.Garvit.Bilionn.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

/**
 * AES-256-GCM encryption for the auth cookie.
 * GCM = encryption + tamper detection: if even one character of the cookie is
 * changed, decrypt() fails and returns Optional.empty().
 */
@Component
public class CookieCrypto {

    private static final int IV_LENGTH = 12;      // recommended size for GCM
    private static final int TAG_BITS = 128;

    private final SecretKeySpec key;
    private final SecureRandom random = new SecureRandom();

    public CookieCrypto(@Value("${auth.cookie.secret}") String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("auth.cookie.secret must be at least 32 characters long");
        }
        try {
            byte[] keyBytes = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
            this.key = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialise cookie encryption", e);
        }
    }

    /** plain text -> URL-safe Base64( iv + ciphertext + tag ) */
    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv).put(encrypted);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new IllegalStateException("Could not encrypt cookie", e);
        }
    }

    /** Returns empty if the value is malformed, was tampered with, or uses another key. */
    public Optional<String> decrypt(String cookieValue) {
        try {
            byte[] all = Base64.getUrlDecoder().decode(cookieValue);
            if (all.length <= IV_LENGTH) return Optional.empty();
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(all, 0, iv, 0, IV_LENGTH);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] plain = cipher.doFinal(all, IV_LENGTH, all.length - IV_LENGTH);
            return Optional.of(new String(plain, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}


