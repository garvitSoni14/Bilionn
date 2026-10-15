package in.Garvit.Bilionn.service.impl;

import in.Garvit.Bilionn.entity.UserEntity;
import in.Garvit.Bilionn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found for the email: " +email));
        return User.withUsername(existingUser.getEmail())
                .password(existingUser.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + existingUser.getRole().name())))
                .disabled(existingUser.getAccountStatus() != in.Garvit.Bilionn.entity.AccountStatus.ACTIVE)
                .build();
    }
}
