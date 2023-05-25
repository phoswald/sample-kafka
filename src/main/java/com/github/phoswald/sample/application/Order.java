package com.github.phoswald.sample.application;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Order {
    
    private String orderId;
    private String orderDetails;
    private String paymentId;
    private OrderStatus status;
    private Instant timestamp;
}
