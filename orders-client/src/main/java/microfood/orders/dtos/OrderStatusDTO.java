package microfood.orders.dtos;

import lombok.Data;
import microfood.orders.enums.OrderStatusEnum;

@Data
public class OrderStatusDTO {
    OrderStatusEnum status;
}
