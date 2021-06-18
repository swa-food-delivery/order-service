package microfood.orders.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Integer itemId;
    private String itemName;
    private Double itemPrice;
    private Integer quantity;
}
