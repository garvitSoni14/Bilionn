package in.Garvit.Bilionn.repository;

import in.Garvit.Bilionn.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
