package com.github.phoswald.sample.adapter.rest;

import com.github.phoswald.sample.application.OrderApplication;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/order")
public class OrderResource {
    
    @Inject
    private OrderApplication application;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEchoJson(OrderRequest request) {
        application.addOrder(request.getOrderId(), request.getOrderDetails(), request.getPaymentId());
        return Response.ok().build();
    }
}
