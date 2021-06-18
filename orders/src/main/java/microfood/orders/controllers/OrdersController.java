package microfood.orders.controllers;

import java.util.List;
import java.util.UUID;

import microfood.orders.exceptions.MenuValidationFailedException;
import microfood.restaurants.exceptions.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import microfood.orders.dtos.OrderDTO;
import microfood.orders.dtos.OrderStatusDTO;
import microfood.orders.exceptions.OrderNotFoundException;
import microfood.orders.exceptions.OrderStatusException;
import microfood.orders.exceptions.OrdersCannotCancelException;
import microfood.orders.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) throws RestaurantNotFoundException, MenuValidationFailedException {
        return orderService.createOrder(orderDTO);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable UUID orderId) throws OrderNotFoundException {
        return orderService.getOrderById(orderId);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable UUID orderId) throws OrdersCannotCancelException, OrderNotFoundException {
        orderService.cancelOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(@PathVariable String userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping("/user/{restaurantId}")
    public List<OrderDTO> getOrdersByRestaurants(@PathVariable UUID restaurantId) {
        return orderService.getOrdersByRestaurantId(restaurantId);
    }


    @PutMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.OK)
    public void setOrderStatus(@PathVariable UUID orderId, @RequestBody OrderStatusDTO statusDTO)
            throws OrderNotFoundException, OrderStatusException {
        orderService.setOrderStatus(orderId, statusDTO.getStatus());
    }
}
