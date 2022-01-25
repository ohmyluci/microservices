package com.lucidiovacas.customer;

import com.lucidiovacas.amqp.RabbitMqMessageProducer;
import com.lucidiovacas.clients.fraud.FraudCheckResponse;
import com.lucidiovacas.clients.fraud.FraudClient;
import com.lucidiovacas.clients.notification.NotificationClient;
import com.lucidiovacas.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    //private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
//    private final NotificationClient notificationClient;
    private final RabbitMqMessageProducer rabbitMqMessageProducer;


    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstname(request.firstName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken

        customerRepository.saveAndFlush(customer);
        // todo: check if fraudster
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getFirstname(),
                "Welcome to my " +
                "microservices application"
        );
        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );


    }
}
