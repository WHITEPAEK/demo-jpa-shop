package com.whitepaek.demojpashop.repository.order.simplequery;

import com.whitepaek.demojpashop.domain.Address;
import com.whitepaek.demojpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name; // Lazy 초기화
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; // Lazy 초기화
    }
}
