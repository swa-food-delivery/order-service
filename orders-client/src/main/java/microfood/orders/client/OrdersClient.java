package microfood.orders.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import microfood.orders.dtos.OrderStatusDTO;

@FeignClient(name = "orders-service", fallback = OrdersClientFallback.class)
public interface OrdersClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{orderId}/status", consumes = "application/json")
    void setOrderStatus(@PathVariable UUID orderId, OrderStatusDTO status);
}
