package com.example.tp_back.controller;

import com.example.tp_back.entity.Order;
import com.example.tp_back.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Integer> body) {

        Integer productId = body.get("productId");
        Integer quantite  = body.get("quantite");

        if (productId == null || quantite == null || quantite <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Order order = orderService.createOrder(
                userDetails.getUsername(), productId, quantite);

        return ResponseEntity.ok(order);
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                orderService.getMyOrders(userDetails.getUsername()));
    }
}
