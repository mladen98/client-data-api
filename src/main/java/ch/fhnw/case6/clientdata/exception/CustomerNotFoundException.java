package ch.fhnw.case6.clientdata.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerReference) {
        super("Customer not found for customerReference: " + customerReference);
    }
}