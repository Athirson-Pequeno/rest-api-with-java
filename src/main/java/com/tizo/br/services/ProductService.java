package com.tizo.br.services;

import com.tizo.br.model.Part;
import com.tizo.br.model.Product;
import com.tizo.br.repositories.PartsRepository;
import com.tizo.br.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PartsRepository partsRepository;

    public Product createProduct(Product product) {
        List<Part> parts = new ArrayList<>();
        for (Part p :  product.getParts()) {
            parts.add(partsRepository.findById(p.getId()).orElseThrow(() -> new EntityNotFoundException("Produto com ID " + p.getId() + " não encontrado.")));
        }
        product.setParts(parts);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
