package in.Garvit.Bilionn.io;

/** Returned by GET /me. Angular fills AuthService.currentUser from this. */
public record CurrentUserResponse(Long userId, String name, String email, String role) {}
