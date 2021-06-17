package microfood.orders.controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import microfood.orders.dtos.BadRequestDTO;
import microfood.orders.dtos.InternalErrorDTO;
import microfood.orders.exceptions.OrderNotFoundException;
import microfood.orders.exceptions.OrderStatusException;
import microfood.orders.exceptions.OrdersCannotCancelException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    @ExceptionHandler({OrderNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleNotFoundException() {

    }

    @ExceptionHandler({OrdersCannotCancelException.class, OrderStatusException.class})
    public ResponseEntity<BadRequestDTO> handleBadRequestException(Exception e) {
        return new ResponseEntity<>(new BadRequestDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<InternalErrorDTO> handleInternalError(Exception e) {
        return new ResponseEntity<>(new InternalErrorDTO(e.getMessage(), ExceptionUtils.getStackTrace(e)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
