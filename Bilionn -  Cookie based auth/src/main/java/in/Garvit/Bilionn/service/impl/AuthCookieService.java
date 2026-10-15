package in.Garvit.Bilionn.service.impl;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Four PLAIN cookies: userId, name, email, role. No encryption. NOT SECURE - local/learning use only. */
@Component
public class AuthCookieService {

    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
    public static final long MAX_AGE_SECONDS = 60 * 60 * 10;   // 10 hours

    private static final List<String> SLOTS = List.of(USER_ID, NAME, EMAIL, ROLE);

    public record AuthPayload(Long userId, String name, String email, String role) {}

    private final boolean secure;

    public AuthCookieService(@Value("${auth.cookie.secure:false}") boolean secure) {
        this.secure = secure;
    }

    public List<ResponseCookie> issue(Long userId, String name, String email, String role) {
        return List.of(
                build(USER_ID, String.valueOf(userId), MAX_AGE_SECONDS),
                build(NAME, name, MAX_AGE_SECONDS),
                build(EMAIL, email, MAX_AGE_SECONDS),
                build(ROLE, role, MAX_AGE_SECONDS));
    }

    public List<ResponseCookie> clear() {
        return SLOTS.stream().map(slot -> build(slot, "", 0)).toList();
    }

    public Optional<AuthPayload> read(Cookie[] cookies) {
        if (cookies == null) return Optional.empty();

        Map<String, String> values = new HashMap<>();
        for (Cookie cookie : cookies) {
            if (SLOTS.contains(cookie.getName())) {
                values.put(cookie.getName(), URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8));
            }
        }
        String email = values.get(EMAIL);
        String role = values.get(ROLE);
        String userId = values.get(USER_ID);
        if (isBlank(email) || isBlank(role) || isBlank(userId)) return Optional.empty();

        try {
            return Optional.of(new AuthPayload(Long.valueOf(userId), values.getOrDefault(NAME, ""), email, role));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private ResponseCookie build(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8))
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}