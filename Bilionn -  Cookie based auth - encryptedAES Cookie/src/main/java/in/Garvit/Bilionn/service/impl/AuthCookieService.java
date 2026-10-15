package in.Garvit.Bilionn.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import in.Garvit.Bilionn.filter.CookieCrypto;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

/**
 * ONE cookie called "auth".
 * Its value = AES-GCM encrypted JSON:  {"userId":1,"name":"BAdmin","email":"admin@gmail.com","role":"ADMIN","exp":1791843298}
 * GCM also detects tampering, so the cookie cannot be edited or forged without the server's secret key.
 * exp (epoch seconds) is checked on every request.
 */
@Component
public class AuthCookieService {

    public static final String COOKIE_NAME = "auth";
    public static final long MAX_AGE_SECONDS = 60 * 60 * 10;   // 10 hours, same as the old JWT

    /** The JSON that gets encrypted into the cookie. */
    public record AuthPayload(Long userId, String name, String email, String role, long exp) {}

    private final CookieCrypto crypto;
    private final ObjectMapper objectMapper;
    private final boolean secure;

    public AuthCookieService(CookieCrypto crypto,
                             ObjectMapper objectMapper,
                             @Value("${auth.cookie.secure:false}") boolean secure) {
        this.crypto = crypto;
        this.objectMapper = objectMapper;
        this.secure = secure;
    }

    /** The Set-Cookie to send after a successful login. */
    public ResponseCookie issue(Long userId, String name, String email, String role) {
        try {
            AuthPayload payload = new AuthPayload(userId, name, email, role,
                    Instant.now().getEpochSecond() + MAX_AGE_SECONDS);
            String json = objectMapper.writeValueAsString(payload);
            return build(crypto.encrypt(json), MAX_AGE_SECONDS);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create auth cookie", e);
        }
    }

    /** Expired cookie, used by logout. */
    public ResponseCookie clear() {
        return build("", 0);
    }

    /** Empty when the cookie is missing, tampered with, malformed or expired. */
    public Optional<AuthPayload> read(Cookie[] cookies) {
        if (cookies == null) return Optional.empty();
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) return parse(cookie.getValue());
        }
        return Optional.empty();
    }

    private Optional<AuthPayload> parse(String cookieValue) {
        return crypto.decrypt(cookieValue).flatMap(json -> {
            try {
                AuthPayload payload = objectMapper.readValue(json, AuthPayload.class);
                if (payload.email() == null || payload.email().isBlank()
                        || payload.exp() <= Instant.now().getEpochSecond()) {
                    return Optional.empty();
                }
                return Optional.of(payload);
            } catch (Exception e) {
                return Optional.empty();
            }
        });
    }

    private ResponseCookie build(String value, long maxAgeSeconds) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)            // JavaScript cannot read it; Angular uses GET /me
                .secure(secure)            // set auth.cookie.secure=true behind HTTPS
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();
    }
}
