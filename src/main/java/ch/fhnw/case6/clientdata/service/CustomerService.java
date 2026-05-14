package ch.fhnw.case6.clientdata.service;

import ch.fhnw.case6.clientdata.dto.CustomerData;
import ch.fhnw.case6.clientdata.exception.CustomerNotFoundException;
import ch.fhnw.case6.clientdata.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerData getCustomerData(String customerReference) {
        if (customerReference == null || customerReference.trim().isEmpty()) {
            throw new IllegalArgumentException("customerReference must not be empty");
        }

        CustomerData customerData = customerRepository
                .findByCustomerReference(customerReference)
                .orElseThrow(() -> new CustomerNotFoundException(customerReference));

        customerData.setCustomerLookupSuccess(isComplete(customerData));
        return customerData;
    }

    private boolean isComplete(CustomerData data) {
        return isNotBlank(data.getDestination())
                && isNotBlank(data.getRecepientPhone())
                && isNotBlank(data.getEmail());
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}