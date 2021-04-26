package microfood.orders.mapper;


import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import microfood.orders.OrderStatusEnum;
import microfood.orders.dtos.OrderDTO;
import microfood.orders.dtos.OrderItemDTO;
import microfood.orders.entities.Order;
import microfood.orders.entities.OrderItem;
import microfood.orders.mappers.OrderItemMapper;
import microfood.orders.mappers.OrderMapper;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapper(orderItemMapper);
    }

    @Test
    public void testMapEntityToDto() {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setOrderStatus(OrderStatusEnum.REQUESTED);
        order.setUsername("username");
        order.setRestaurantId(UUID.randomUUID());
        order.setDeliveryAddress("address");
        order.setNotes(null);
        order.setCreatedOn(new Date(System.currentTimeMillis()));

        OrderItem item = new OrderItem();
        item.setOrderItemId(UUID.randomUUID());
//        item.setOrderId(UUID.randomUUID());
        item.setItemId(UUID.randomUUID());
        item.setItemName("item name");
        item.setItemPrice(1.75);
        item.setItemQuantity(2);

        order.setOrderItems(Collections.singletonList(item));

        OrderDTO orderDto = orderMapper.mapEntityToDto(order);

//        Assertions.assertEquals(order.getOrderId(), orderDto.getOrderId());
        Assertions.assertEquals(order.getOrderStatus(), orderDto.getOrderStatus());
        Assertions.assertEquals(order.getUsername(), orderDto.getUserId());
        Assertions.assertEquals(order.getRestaurantId(), orderDto.getRestaurantId());
        Assertions.assertEquals(order.getNotes(), orderDto.getNotes());
        Assertions.assertEquals(order.getDeliveryAddress(), orderDto.getDeliveryAddress());
        Assertions.assertEquals(order.getCreatedOn(), orderDto.getCreatedOn());

        Assertions.assertEquals(order.getOrderItems().get(0).getItemId(), orderDto.getOrderItems().get(0).getItemId());
        Assertions.assertEquals(order.getOrderItems().get(0).getItemName(), orderDto.getOrderItems().get(0).getItemName());
        Assertions.assertEquals(order.getOrderItems().get(0).getItemPrice(), orderDto.getOrderItems().get(0).getItemPrice());
        Assertions.assertEquals(order.getOrderItems().get(0).getItemQuantity(), orderDto.getOrderItems().get(0).getQuantity());
    }

    @Test
    public void testMapDtoToEntity() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(UUID.randomUUID());
        orderDTO.setOrderStatus(OrderStatusEnum.REQUESTED);
        orderDTO.setUserId("username");
        orderDTO.setRestaurantId(UUID.randomUUID());
        orderDTO.setDeliveryAddress("address");
        orderDTO.setNotes(null);
        orderDTO.setCreatedOn(new Date(System.currentTimeMillis()));

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setItemId(UUID.randomUUID());
        itemDTO.setItemName("item name");
        itemDTO.setItemPrice(1.75);
        itemDTO.setQuantity(2);

        OrderItem item = new OrderItem();
        item.setItemId(itemDTO.getItemId());
        item.setItemName(itemDTO.getItemName());
        item.setItemPrice(itemDTO.getItemPrice());
        item.setItemQuantity(itemDTO.getQuantity());

        orderDTO.setOrderItems(Collections.singletonList(itemDTO));
        Mockito.when(orderItemMapper.mapDtosToEntitiesList(orderDTO.getOrderItems())).thenReturn(Collections.singletonList(item));
        Order order = orderMapper.mapDtoToEntity(orderDTO);

        Assertions.assertEquals(order.getOrderId(), orderDTO.getOrderId());
        Assertions.assertEquals(order.getOrderStatus(), orderDTO.getOrderStatus());
        Assertions.assertEquals(order.getUsername(), orderDTO.getUserId());
        Assertions.assertEquals(order.getRestaurantId(), orderDTO.getRestaurantId());
        Assertions.assertEquals(order.getNotes(), orderDTO.getNotes());
        Assertions.assertEquals(order.getDeliveryAddress(), orderDTO.getDeliveryAddress());
        Assertions.assertEquals(order.getCreatedOn(), orderDTO.getCreatedOn());

        Assertions.assertEquals(order.getOrderItems().get(0).getItemId(), orderDTO.getOrderItems().get(0).getItemId());
        Assertions.assertEquals(order.getOrderItems().get(0).getItemName(), orderDTO.getOrderItems().get(0).getItemName());
        Assertions.assertEquals(order.getOrderItems().get(0).getItemPrice(), orderDTO.getOrderItems().get(0).getItemPrice());
        Assertions.assertEquals(order.getOrderItems().get(0).getItemQuantity(), orderDTO.getOrderItems().get(0).getQuantity());
    }
}
