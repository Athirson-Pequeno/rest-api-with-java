package com.tizo.br.controllers;

import com.tizo.br.model.Order;
import com.tizo.br.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order criarPedido() {
        return orderService.createOrder();
    }
    @GetMapping
    public List<Order> getALL() {
        return orderService.findAll();
    }

}
