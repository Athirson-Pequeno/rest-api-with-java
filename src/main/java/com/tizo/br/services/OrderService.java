package com.tizo.br.services;

import com.tizo.br.model.Order;
import com.tizo.br.model.Product;
import com.tizo.br.model.enums.Status;
import com.tizo.br.model.OrderProduct;
import com.tizo.br.repositories.OrderProductRepository;
import com.tizo.br.repositories.OrderRepository;
import com.tizo.br.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    public Order createOrder() {
        Order order = new Order();
        order.setQuantity(200);
        order = orderRepository.save(order);
        System.out.println(order);

        List<Long> productIDs = new ArrayList<>();
        List<Status> statusList = new ArrayList<>();

        productIDs.add(1L);
        statusList.add(Status.RECEIVED);

        for (int i = 0; i < productIDs.size(); i++) {
            Long produtoId = productIDs.get(i);
            Status status = statusList.get(i);

            Product product = productRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setStatus(status);

            orderProductRepository.save(orderProduct);
        }

        Order orderWithProducts = orderRepository.findById(order.getId()).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        orderWithProducts.getItens().size();

        return orderWithProducts;
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }
}