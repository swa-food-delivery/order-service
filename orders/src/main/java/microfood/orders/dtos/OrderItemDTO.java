package microfood.orders.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private UUID itemId;
    private String itemName;
    private Double itemPrice;
    private Integer quantity;
}
