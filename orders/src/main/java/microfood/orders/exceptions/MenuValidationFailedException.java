package microfood.orders.exceptions;

public class MenuValidationFailedException extends OrdersException {
    public MenuValidationFailedException() {
        super();
    }

    public MenuValidationFailedException(String message) {
        super(message);
    }
}
