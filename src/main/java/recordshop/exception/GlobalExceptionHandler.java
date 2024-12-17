package recordshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(404, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMissingFieldException(MissingFieldException e) {
        return new ResponseEntity<>(new ErrorResponse(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnavailableRouteException(UnavailableRouteException e) {
        return new ResponseEntity<>(new ErrorResponse(404, e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
