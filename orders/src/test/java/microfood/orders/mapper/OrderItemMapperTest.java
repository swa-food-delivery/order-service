package microfood.orders.mapper;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import microfood.orders.dtos.OrderItemDTO;
import microfood.orders.entities.OrderItem;
import microfood.orders.mappers.OrderItemMapper;

@ExtendWith(MockitoExtension.class)
public class OrderItemMapperTest {

    private OrderItemMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new OrderItemMapper();
    }

    @Test
    public void testMapEntityToDto() {
        OrderItem item = new OrderItem();
        item.setOrderItemId(UUID.randomUUID());
        item.setItemId(3224);
        item.setItemName("item name");
        item.setItemPrice(1.75);
        item.setItemQuantity(2);

        OrderItemDTO itemDto = mapper.mapEntityToDto(item);

        Assertions.assertEquals(item.getItemId(), itemDto.getItemId());
        Assertions.assertEquals(item.getItemName(), itemDto.getItemName());
        Assertions.assertEquals(item.getItemPrice(), itemDto.getItemPrice());
        Assertions.assertEquals(item.getItemQuantity(), itemDto.getQuantity());
    }

    @Test
    public void testMapDtoToEntity() {
        OrderItemDTO item = new OrderItemDTO();
        item.setItemId(3224);
        item.setItemName("item name");
        item.setItemPrice(1.75);
        item.setQuantity(2);

        OrderItem itemEntity = mapper.mapDtoToEntity(item);

        Assertions.assertEquals(item.getItemId(), itemEntity.getItemId());
        Assertions.assertEquals(item.getItemName(), itemEntity.getItemName());
        Assertions.assertEquals(item.getItemPrice(), itemEntity.getItemPrice());
        Assertions.assertEquals(item.getQuantity(), itemEntity.getItemQuantity());
        Assertions.assertNull(itemEntity.getOrderItemId());
    }
}
