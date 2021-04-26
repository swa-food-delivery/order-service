package microfood.orders.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import microfood.orders.OrderStatusEnum;
import microfood.orders.dtos.OrderDTO;
import microfood.orders.entities.Order;
import microfood.orders.exceptions.OrderNotFoundException;
import microfood.orders.exceptions.OrdersCannotCancelException;
import microfood.orders.mappers.OrderMapper;
import microfood.orders.repos.OrderItemsRepository;
import microfood.orders.repos.OrdersRepository;

@Service
@Transactional
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, OrderMapper orderMapper, OrderItemsRepository orderItemsRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        // TODO: add validation
        orderDTO.setOrderStatus(OrderStatusEnum.REQUESTED);
        Order order = orderMapper.mapDtoToEntity(orderDTO);
        Order createdOrder = ordersRepository.save(order);
        return orderMapper.mapEntityToDto(createdOrder);
    }

    public OrderDTO getOrderById(UUID orderId) throws OrderNotFoundException {
        Order order = ordersRepository.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);
        OrderDTO orderDTO = orderMapper.mapEntityToDto(order);
        return orderDTO;
    }

    public void cancelOrder(UUID orderId) throws OrdersCannotCancelException, OrderNotFoundException {
        Order order = ordersRepository.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);

        if (order.getOrderStatus().equals(OrderStatusEnum.REQUESTED)) {
            order.setOrderStatus(OrderStatusEnum.CANCELLED);
            // TODO: send order cancellation
        } else {
            throw new OrdersCannotCancelException("Cannot cancel order since it was already processed");
        }
    }

    public List<OrderDTO> getOrdersByUser(String username) {
        return orderMapper.mapEntitiesToDtoList(ordersRepository.getByUsername(username));
    }

    public List<OrderDTO> getOrdersByRestaurantId(UUID restaurantId) {
        return orderMapper.mapEntitiesToDtoList(ordersRepository.getByRestaurantId(restaurantId));
    }
}
