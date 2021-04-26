package microfood.orders.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microfood.orders.OrderStatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private OrderStatusEnum orderStatus;
    private String userId;
    private UUID restaurantId;
    private String notes;
    private List<OrderItemDTO> orderItems;
    private String deliveryAddress;
    private Date createdOn;
}
