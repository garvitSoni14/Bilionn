package in.Garvit.Bilionn.filter;

import in.Garvit.Bilionn.service.impl.AppUserDetailsService;
import in.Garvit.Bilionn.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import io.jsonwebtoken.JwtException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwt = findJwtCookie(request);
        String email = null;
        if(jwt != null){
            try {
            email = jwtUtil.extractUsername(jwt);
            } catch (JwtException | IllegalArgumentException ignored) {
                // Invalid or expired cookies are treated as unauthenticated requests.
            }
        }
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if(userDetails.isEnabled() && jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException ignored) {
                // Invalid tokens and tokens for deleted users are treated as unauthenticated requests.
            }

        }
        filterChain.doFilter(request, response);
    }

    private String findJwtCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}
