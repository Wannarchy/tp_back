package com.example.tp_back.repository;

import com.example.tp_back.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {


    List<Order> findByUserId(Integer userId);


    List<Order> findByUserIdAndStatus(Integer userId, Order.OrderStatus status);
}
