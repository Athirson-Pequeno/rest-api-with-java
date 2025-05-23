package com.tizo.br.services;

import com.tizo.br.model.Order;
import com.tizo.br.model.OrderProduct;
import com.tizo.br.model.Product;
import com.tizo.br.model.enums.Status;
import com.tizo.br.model.vo.security.OrderRecord;
import com.tizo.br.repositories.OrderProductRepository;
import com.tizo.br.repositories.OrderRepository;
import com.tizo.br.repositories.ProductRepository;
import com.tizo.br.security.jwt.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Order createOrder(List<OrderRecord> orders, String token) {

        Order order = new Order();
        order.setDate(new Date());
        order.setClient(jwtTokenProvider.getUserDetails(token.replace("Bearer ", "")).getUsername());
        order = orderRepository.save(order);

        for (OrderRecord orderRecord : orders) {

            Long produtoId = orderRecord.productID();
            Integer amount = orderRecord.amount();
            Status status = Status.RECEIVED;

            Product product = productRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setStatus(status);
            orderProduct.setAmount(amount);

            orderProductRepository.save(orderProduct);
        }

        return orderRepository.findById(order.getId()).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Order not found"));
    }

    public Order update(@Valid Order order) {
        orderRepository.findById(order.getId()).orElseThrow(() ->
                new EntityNotFoundException("Order not found"));

        return orderRepository.save(order);
    }
}