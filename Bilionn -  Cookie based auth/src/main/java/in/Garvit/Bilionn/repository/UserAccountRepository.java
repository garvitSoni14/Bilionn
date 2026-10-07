package in.Garvit.Bilionn.repository;

import in.Garvit.Bilionn.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface UserAccountRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
