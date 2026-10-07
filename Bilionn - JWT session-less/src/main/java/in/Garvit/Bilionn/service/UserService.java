package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.io.UserRequest;
import in.Garvit.Bilionn.io.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    String getUserRole(String email);

    List<UserResponse> readUsers();

    void deleteUser(String id);

}
