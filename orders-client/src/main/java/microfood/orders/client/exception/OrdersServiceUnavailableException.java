package microfood.orders.client.exception;

public class OrdersServiceUnavailableException extends RuntimeException {
    public OrdersServiceUnavailableException(String message) {
        super(message);
    }
}
