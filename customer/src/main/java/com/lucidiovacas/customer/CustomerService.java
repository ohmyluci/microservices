package com.lucidiovacas.customer;

import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstname(request.firstName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        // todo: check if fraudster
        customerRepository.save(customer);
    }
}
