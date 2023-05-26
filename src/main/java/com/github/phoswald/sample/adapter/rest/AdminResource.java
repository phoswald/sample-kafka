package com.github.phoswald.sample.adapter.rest;

import java.util.List;
import java.util.Optional;

import com.github.phoswald.sample.application.Order;
import com.github.phoswald.sample.application.OrderApplication;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@RequestScoped
@Path("admin")
public class AdminResource {
    
    @Inject
    private OrderApplication application;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getOrders(
            @QueryParam("offset") Integer offset,
            @QueryParam("count") Integer count) {
        return application.findOrders(offset, count);
    }

    @GET
    @Path("{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(
            @PathParam("orderId") String orderId) {
        Optional<Order> order = application.findOrderById(orderId);
        if(order.isPresent()) {
            return Response.ok(order.get()).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @POST
    @Path("/event/{orderId}")
    public void postOrderEvent(
            @PathParam("orderId") String orderId) {
        application.produceOrder(orderId);
    }
}
