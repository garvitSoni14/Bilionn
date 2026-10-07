package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.io.AdminBootstrapRequest;
import in.Garvit.Bilionn.io.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createInitialAdmin(AdminBootstrapRequest request);

    String getUserRole(String email);

    List<UserResponse> readUsers();

}
