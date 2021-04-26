package microfood.orders.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import microfood.orders.entities.OrderItem;

public interface OrderItemsRepository extends JpaRepository<OrderItem, UUID> {

}
