package microfood.orders.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import microfood.orders.dtos.OrderDTO;
import microfood.orders.dtos.OrderItemDTO;
import microfood.orders.entities.Order;

@Component
public class OrderMapper {

    private final ModelMapper mapper;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.mapper = new ModelMapper();
        this.orderItemMapper = orderItemMapper;
        mapper.createTypeMap(Order.class, OrderDTO.class).addMapping(Order::getUsername, OrderDTO::setUserId);
        mapper.createTypeMap(OrderDTO.class, Order.class).addMapping(OrderDTO::getUserId, Order::setUsername)
                .addMappings(mp -> mp.using(ctx -> orderItemMapper.mapDtosToEntitiesList((List<OrderItemDTO>) ctx.getSource()))
                        .map(OrderDTO::getOrderItems, Order::setOrderItems));
    }

    public Order mapDtoToEntity(OrderDTO orderDTO) {
        Order order = mapper.map(orderDTO, Order.class);
        order.getOrderItems().forEach(item -> item.setOrder(order));
        return order;
    }

    public OrderDTO mapEntityToDto(Order order) {
        return mapper.map(order, OrderDTO.class);
    }

    public List<OrderDTO> mapEntitiesToDtoList(List<Order> orders) {
        return orders.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }
}
