package microfood.orders.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import microfood.orders.dtos.OrderItemDTO;
import microfood.orders.entities.OrderItem;

@Component
public class OrderItemMapper {

    public final ModelMapper mapper;

    public OrderItemMapper() {
        this.mapper = new ModelMapper();
        mapper.typeMap(OrderItemDTO.class, OrderItem.class).addMappings(mp -> mp.skip(OrderItem::setOrderItemId));
    }

    public OrderItemDTO mapEntityToDto(OrderItem orderItem) {
        return mapper.map(orderItem, OrderItemDTO.class);

    }

    public OrderItem mapDtoToEntity(OrderItemDTO orderItemDTO) {
        return mapper.map(orderItemDTO, OrderItem.class);

    }

    public List<OrderItem> mapDtosToEntitiesList(List<OrderItemDTO> orderItemDTOS) {
        return orderItemDTOS.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
    }

    public List<OrderItemDTO> mapEntitiesToListDto(List<OrderItem> orderItems) {
        return orderItems.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }
}
