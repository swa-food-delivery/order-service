package microfood.orders.controllers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import microfood.orders.OrderStatusEnum;
import microfood.orders.dtos.DeliveryAddressDTO;
import microfood.orders.dtos.OrderDTO;
import microfood.orders.dtos.OrderItemDTO;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrdersController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        throw new NotImplementedException();
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('test-role')")
    public OrderDTO getOrderById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getDetails().toString());
        log.info(auth.getAuthorities().toString());
        List<OrderItemDTO> items = Collections.singletonList(new OrderItemDTO(UUID.randomUUID(), "Cheeseburger", 1.75, 1));
        DeliveryAddressDTO address = new DeliveryAddressDTO(jwt == null ? "jwt is null" : jwt.getClaims().toString());
        return new OrderDTO(UUID.randomUUID(), OrderStatusEnum.REQUESTED, "userId",
                UUID.randomUUID(), "Some notes", items, address);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable UUID orderId) {
        throw new NotImplementedException();
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(@PathVariable String userId) {
        throw new NotImplementedException();
    }

    @GetMapping("/user/{restaurantId}")
    public List<OrderDTO> getOrdersByRestaurants(@PathVariable String restaurantId) {
        throw new NotImplementedException();
    }


}
