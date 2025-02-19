package com.tizo.br.controllers;

import com.tizo.br.model.Product;
import com.tizo.br.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Products", description = "Endpoints for Managing Sectors of Products")
@RestController
@RequestMapping("/api/products/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(produces = {"application/json"})
    @Operation(
            summary = "Find all Products",
            description = "Find all Products and turn back in a pageable list",
            tags = {"Products"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Product.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<PagedModel<Product>> getAllProducts(@PageableDefault(page = 0, size = 50) Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);

        PagedModel<Product> pagedModel = PagedModel.of(products.getContent().stream()
                        .map(this::getConfiguredProduct).collect(Collectors.toList()),
                new PagedModel.PageMetadata(
                        products.getSize(),
                        products.getNumber(),
                        products.getTotalElements(),
                        products.getTotalPages()
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @GetMapping(
            value = "/{id}",
            produces = {"application/json"})
    @Operation(
            summary = "Find a product",
            description = "Find product by id",
            tags = {"Products"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = Product.class))
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(getConfiguredProduct(product));
    }

    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Adds a new Product",
            description = "Adds a new Product by passing in a JSON",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(getConfiguredProduct(newProduct));
    }

    @DeleteMapping(value = "{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Delete a product",
            description = "Delete a product by id",
            tags = {"Products"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)}
    )
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = {"{id}"},
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Update a product",
            description = "Update a product by id",
            tags = {"Products"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)}
    )
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        product.setId(id);
        Product updatedProduct = productService.update(product);
        return ResponseEntity.status(HttpStatus.OK).body(getConfiguredProduct(updatedProduct));
    }

    private Product getConfiguredProduct(Product product) {

        product.add(linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel());
        product.add(linkTo(methodOn(ProductController.class).updateProduct(product.getId(), null)).withRel("update"));
        product.add(linkTo(methodOn(ProductController.class).deleteProduct(product.getId())).withRel("delete"));

        return product;
    }
}
