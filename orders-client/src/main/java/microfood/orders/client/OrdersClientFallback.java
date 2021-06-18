package microfood.orders.client;

import java.util.UUID;

import org.springframework.stereotype.Component;

import microfood.orders.client.exception.OrdersServiceUnavailableException;
import microfood.orders.dtos.OrderStatusDTO;

@Component("OrdersClientFallback")
public class OrdersClientFallback implements OrdersClient {
    @Override
    public void setOrderStatus(UUID orderId, OrderStatusDTO status) {
        throw new OrdersServiceUnavailableException("orders-service is unavailable");
    }
}
