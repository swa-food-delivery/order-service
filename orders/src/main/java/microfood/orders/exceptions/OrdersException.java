package microfood.orders.exceptions;

public class OrdersException extends Exception {
    public OrdersException() {

    }

    public OrdersException(String message) {
        super(message);
    }

    public OrdersException(String message, final Throwable cause) {
        super(message, cause);
    }

    public OrdersException(final Throwable cause) {
        super(cause);
    }
}
