package microfood.orders.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import microfood.orders.enums.OrderStatusEnum;
import microfood.orders.dtos.OrderDTO;
import microfood.orders.entities.Order;
import microfood.orders.exceptions.OrderNotFoundException;
import microfood.orders.exceptions.OrderStatusException;
import microfood.orders.exceptions.OrdersCannotCancelException;
import microfood.orders.mappers.OrderMapper;
import microfood.orders.repos.OrderItemsRepository;
import microfood.orders.repos.OrdersRepository;
import microfood.tickets.client.RestaurantTicketsClient;
import microfood.tickets.dtos.TicketBaseDTO;

@Service
@Transactional
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderMapper orderMapper;
    private final RestaurantTicketsClient ticketsClient;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, OrderMapper orderMapper,
                        OrderItemsRepository orderItemsRepository,
                        RestaurantTicketsClient ticketsClient) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.orderMapper = orderMapper;
        this.ticketsClient = ticketsClient;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        // TODO: add validation
        orderDTO.setOrderStatus(OrderStatusEnum.REQUESTED);
        Order order = orderMapper.mapDtoToEntity(orderDTO);
        Order createdOrder = ordersRepository.save(order);
        TicketBaseDTO ticketBaseDTO = new TicketBaseDTO(null, order.getOrderId());
        ticketsClient.createTicket(orderDTO.getRestaurantId(), ticketBaseDTO);
        return orderMapper.mapEntityToDto(createdOrder);
    }

    public OrderDTO getOrderById(UUID orderId) throws OrderNotFoundException {
        Order order = ordersRepository.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);
        return orderMapper.mapEntityToDto(order);
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

    public void setOrderStatus(UUID orderId, OrderStatusEnum status) throws OrderNotFoundException, OrderStatusException {
        Order order = ordersRepository.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getOrderStatus().getNextStates().contains(status)) {
            order.setOrderStatus(status);
            ordersRepository.save(order);
        } else {
            throw new OrderStatusException(String.format("Cannot change order status from %s to %s",
                    order.getOrderStatus(), status));
        }

    }
}
