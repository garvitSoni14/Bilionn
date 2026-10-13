package in.Garvit.Bilionn.controller;

import in.Garvit.Bilionn.entity.UserEntity;
import in.Garvit.Bilionn.io.AuthRequest;
import in.Garvit.Bilionn.io.AuthResponse;
import in.Garvit.Bilionn.service.UserService;
import in.Garvit.Bilionn.service.impl.AppUserDetailsService;
import in.Garvit.Bilionn.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final String JWT_COOKIE_NAME = "jwt";
    private static final long JWT_COOKIE_MAX_AGE_SECONDS = 60 * 60 * 10;    //10hours

    @Value("${jwt.cookie.secure:false}")
    private boolean jwtCookieSecure;

    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailsService appUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) throws Exception {
        authenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());

        UserEntity user = userService.getByEmail(request.getEmail()); // adjust to your service
        String jwt = jwtUtil.generateToken(userDetails, user.getId(), user.getName(), String.valueOf(user.getRole()));

        ResponseCookie cookie = jwtCookie(jwt, JWT_COOKIE_MAX_AGE_SECONDS);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Success");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = jwtCookie("", 0);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie jwtCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(JWT_COOKIE_NAME, value)
                .httpOnly(false)
                .secure(jwtCookieSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();
    }

    private void authenticate(String email, String password) throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch(DisabledException e){
            throw  new Exception("User disables");
        }catch (BadCredentialsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or password is incorrect");
        }
    }
    @PostMapping("/encode")
    public String encodePassword(@RequestBody Map<String, String> request){
        return passwordEncoder.encode(request.get("password"));
    }


    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        String role = userService.getUserRole(email);

        return ResponseEntity.ok(new AuthResponse(email, role));
    }
}


