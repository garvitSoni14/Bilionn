package in.Garvit.Bilionn.repository;

import in.Garvit.Bilionn.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
   Optional<CategoryEntity> findByCategoryId(String categoryId);
}
