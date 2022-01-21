package com.lucidiovacas.clients.notification;

public record NotificationRequest(
        Integer customerId,
        String toCustomerName,
        String message
) {
}
