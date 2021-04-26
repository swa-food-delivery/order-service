package microfood.orders.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import microfood.orders.entities.Order;

public interface OrdersRepository extends JpaRepository<Order, UUID> {

    Optional<Order> getByOrderId(UUID orderId);

    List<Order> getByUsername(String username);

    List<Order> getByRestaurantId(UUID restaurantId);
}
