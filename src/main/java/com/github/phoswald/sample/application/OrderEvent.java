package com.github.phoswald.sample.application;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OrderEvent {

    private String orderId;
    private String orderDetails;
    private OrderStatus status;
}
