package in.Garvit.Bilionn.filter;

import in.Garvit.Bilionn.service.impl.AppUserDetailsService;
import in.Garvit.Bilionn.service.impl.AuthCookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Replaces JwtRequestFilter. Reads the encrypted "auth" cookie on every request.
 * Missing / tampered / expired cookie => request stays unauthenticated (Spring then answers 401/403).
 */
@Component
@RequiredArgsConstructor
public class AuthCookieFilter extends OncePerRequestFilter {

    private final AppUserDetailsService userDetailsService;
    private final AuthCookieService authCookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException    {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            authCookieService.read(request.getCookies()).ifPresent(payload -> {
                try {
                    // Loading the user keeps authorities identical to before AND means a
                    // disabled / deleted user is locked out immediately, not after 10 hours.
                    UserDetails userDetails = userDetailsService.loadUserByUsername(payload.email());
                    if (userDetails.isEnabled()) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (UsernameNotFoundException ignored) {
                    // cookie for a user that no longer exists -> unauthenticated
                }
            });
        }
        filterChain.doFilter(request, response);
    }
}
