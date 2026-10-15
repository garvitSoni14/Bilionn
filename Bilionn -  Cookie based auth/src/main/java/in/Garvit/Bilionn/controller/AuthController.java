package in.Garvit.Bilionn.controller;



import in.Garvit.Bilionn.entity.UserEntity;
import in.Garvit.Bilionn.io.AuthRequest;
import in.Garvit.Bilionn.io.CurrentUserResponse;
import in.Garvit.Bilionn.service.UserService;
import in.Garvit.Bilionn.service.impl.AuthCookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthCookieService authCookieService;
    private final UserService userService;

    /** Checks email + password, then sets four plain HttpOnly cookies: userId, name, email, role. */
    @PostMapping("/login")
    public ResponseEntity<CurrentUserResponse> login(@RequestBody AuthRequest request) {
        authenticate(request.getEmail(), request.getPassword());

        UserEntity user = userService.getByEmail(request.getEmail());
        String role = String.valueOf(user.getRole());

        HttpHeaders headers = new HttpHeaders();
        authCookieService.issue(user.getId(), user.getName(), request.getEmail(), role)
                .forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(new CurrentUserResponse(user.getId(), user.getName(), request.getEmail(), role));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        HttpHeaders headers = new HttpHeaders();
        authCookieService.clear().forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));
        return ResponseEntity.noContent().headers(headers).build();
    }

    /** Angular calls this on startup / after login to learn who is logged in. */
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {
        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserEntity user = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(new CurrentUserResponse(
                user.getId(), user.getName(), authentication.getName(), String.valueOf(user.getRole())));
    }

    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is disabled");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or password is incorrect");
        }
    }

    // unchanged from your version - make sure SecurityConfig does NOT leave this public in production
    @PostMapping("/encode")
    public String encodePassword(@RequestBody Map<String, String> request) {
        return passwordEncoder.encode(request.get("password"));
    }
}