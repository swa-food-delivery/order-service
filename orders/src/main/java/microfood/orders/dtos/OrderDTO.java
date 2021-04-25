package microfood.orders.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import microfood.orders.OrderStatusEnum;

@Data
@AllArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private OrderStatusEnum orderStatus;
    private String userId;
    private UUID restaurantId;
    private String notes;
    private List<OrderItemDTO> orderItems;
    private DeliveryAddressDTO deliveryAddress;
}
