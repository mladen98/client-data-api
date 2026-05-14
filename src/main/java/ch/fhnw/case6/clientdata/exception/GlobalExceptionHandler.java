package ch.fhnw.case6.clientdata.exception;

import ch.fhnw.case6.clientdata.dto.CustomerData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<CustomerData> handleCustomerNotFound(CustomerNotFoundException ex) {
        CustomerData response = new CustomerData();
        response.setCustomerLookupSuccess(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomerData> handleBadRequest(IllegalArgumentException ex) {
        CustomerData response = new CustomerData();
        response.setCustomerLookupSuccess(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomerData> handleTechnicalError(Exception ex) {
        CustomerData response = new CustomerData();
        response.setCustomerLookupSuccess(false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}