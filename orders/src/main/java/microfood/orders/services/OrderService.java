package microfood.orders.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import microfood.orders.dtos.OrderItemDTO;
import microfood.orders.exceptions.MenuValidationFailedException;
import microfood.restaurants.client.RestaurantsClient;
import microfood.restaurants.dto.FoodDTO;
import microfood.restaurants.exceptions.RestaurantNotFoundException;
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
@Slf4j
@Transactional
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderMapper orderMapper;
    private final RestaurantTicketsClient ticketsClient;
    private final RestaurantsClient restaurantsClient;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, OrderMapper orderMapper,
                        OrderItemsRepository orderItemsRepository,
                        RestaurantTicketsClient ticketsClient,
                        RestaurantsClient restaurantsClient) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.orderMapper = orderMapper;
        this.ticketsClient = ticketsClient;
        this.restaurantsClient = restaurantsClient;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) throws RestaurantNotFoundException, MenuValidationFailedException {
        //order validation
        //I absolutely hate this, it's so bad I am probably going to get told to switch fields while I can
        if (!validateMenu(orderDTO, restaurantsClient.getMenu(orderDTO.getRestaurantId()))) {
            throw new MenuValidationFailedException("The order you're trying to submit contains food not available at the restaurant");
        }

        orderDTO.setOrderStatus(OrderStatusEnum.REQUESTED);
        Order order = orderMapper.mapDtoToEntity(orderDTO);
        Order createdOrder = ordersRepository.save(order);
        TicketBaseDTO ticketBaseDTO = new TicketBaseDTO(null, order.getOrderId());
        ticketsClient.createTicket(orderDTO.getRestaurantId(), ticketBaseDTO);
        log.info("CREATED ORDER " + createdOrder.getOrderId() + " FOR USER " + createdOrder.getUsername() +
                "IN A RESTAURANT WITH AN ID: " + orderDTO.getRestaurantId());
        return orderMapper.mapEntityToDto(createdOrder);
    }

    private boolean validateMenu(OrderDTO orderDTO, List<FoodDTO> menu) {
        List<OrderItemDTO> items = orderDTO.getOrderItems();
        List<String> check = menu.stream().map(FoodDTO::getName).collect(Collectors.toList());
        for (OrderItemDTO item : items) {
            if (!check.contains(item.getItemName())) {

                log.info("ORDER VALIDATION FOR USER " + orderDTO.getUserId() + " FAILED. THE ORDER CONTAINS ITEM NOT ON THE MENU: " + item.getItemName());
                return false;
            }
        }
        return true;
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
