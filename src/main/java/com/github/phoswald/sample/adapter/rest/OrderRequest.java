package com.github.phoswald.sample.adapter.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    
    private String orderId;
    private String orderDetails;
    private String paymentId;
}
