package in.Garvit.Bilionn.service.impl;

import in.Garvit.Bilionn.entity.UserEntity;
import in.Garvit.Bilionn.entity.Role;
import in.Garvit.Bilionn.io.AdminBootstrapRequest;
import in.Garvit.Bilionn.io.UserResponse;
import in.Garvit.Bilionn.repository.UserRepository;
import in.Garvit.Bilionn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public UserResponse createInitialAdmin(AdminBootstrapRequest request) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalStateException("An admin account already exists; sign in to create additional accounts");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name, email, and password are required");
        }
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        UserEntity admin = UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .name(request.getName().trim())
                .email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        return convertToResponse(userRepository.save(admin));
    }

    private UserResponse convertToResponse(UserEntity newUser){
        return UserResponse.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .userId(newUser.getUserId())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .role(newUser.getRole().name())
                .accountStatus(newUser.getAccountStatus())
                .build();
    }
    @Override
    public String getUserRole(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for the email: "+email));
        return existingUser.getRole().name();
    }

    @Override
    public List<UserResponse> readUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> convertToResponse(user))
                .collect(Collectors.toList());
    }

}
