package microfood.orders.exceptions;

public class OrdersCannotCancelException extends OrdersException {
    public OrdersCannotCancelException(String message) {
        super(message);
    }
}
