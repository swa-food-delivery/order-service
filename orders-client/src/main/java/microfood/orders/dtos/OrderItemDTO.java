package microfood.orders.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private UUID itemId;
    private String itemName;
    private Double itemPrice;
    private Integer quantity;
}
