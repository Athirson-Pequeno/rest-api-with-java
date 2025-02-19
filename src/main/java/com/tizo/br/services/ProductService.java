package com.tizo.br.services;

import com.tizo.br.model.Part;
import com.tizo.br.model.Product;
import com.tizo.br.repositories.PartsRepository;
import com.tizo.br.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        for (Part p : product.getParts()) {
            parts.add(partsRepository.findById(p.getId()).orElseThrow(() -> new EntityNotFoundException("Produto com ID " + p.getId() + " não encontrado.")));
        }
        product.setParts(parts);
        return productRepository.save(product);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Product not found"));
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Product not found"));

        productRepository.delete(product);
    }

    public Product update(@Valid Product product) {
        productRepository.findById(product.getId()).orElseThrow(() ->
                new EntityNotFoundException("Product not found"));

        return productRepository.save(product);
    }
}
