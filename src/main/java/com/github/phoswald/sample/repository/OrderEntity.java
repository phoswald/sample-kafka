package com.github.phoswald.sample.repository;

import java.time.Instant;
import java.util.Date;

import com.github.phoswald.sample.application.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ORDER_")
@NamedQuery(name = OrderEntity.SELECT_ALL, query = "select o from OrderEntity o order by o.timestamp desc")
@NamedQuery(name = OrderEntity.SELECT_BY_PAYMENTID, query = "select o from OrderEntity o where o.paymentId = :paymentId")
@Getter
@Setter
public class OrderEntity {

    static final String SELECT_ALL = "OrderEntity.SelectAll";
    static final String SELECT_BY_PAYMENTID = "OrderEntity.SelectByPaymentId";
    
    @Id
    @Column(name = "ORDER_ID_")
    private String orderId;
    
    @Column(name = "ORDER_DETAILS_")
    private String orderDetails;
    
    @Column(name = "PAYMENT_ID_")
    private String paymentId;
    
    @Column(name = "STATUS_")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "TIMESTAMP_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Instant getTimestamp() {
        return timestamp == null ? null : timestamp.toInstant();
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp == null ? null : Date.from(timestamp);
    }
}
