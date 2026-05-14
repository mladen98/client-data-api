package ch.fhnw.case6.clientdata.api;

import ch.fhnw.case6.clientdata.dto.CustomerData;
import ch.fhnw.case6.clientdata.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/customers/{customerReference}")
    public CustomerData getCustomerData(@PathVariable String customerReference) {
        return customerService.getCustomerData(customerReference);
    }
}