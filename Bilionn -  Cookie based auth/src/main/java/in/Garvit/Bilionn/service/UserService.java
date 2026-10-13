package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.entity.UserEntity;
import in.Garvit.Bilionn.io.AdminBootstrapRequest;
import in.Garvit.Bilionn.io.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse createInitialAdmin(AdminBootstrapRequest request);

    UserEntity getByEmail(String email);

    String getUserRole(String email);

    List<UserResponse> readUsers();


}
