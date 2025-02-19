package com.tizo.br.controllers;

import com.tizo.br.model.Order;
import com.tizo.br.model.Part;
import com.tizo.br.model.vo.security.OrderRecord;
import com.tizo.br.services.OrderService;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Orders", description = "Endpoints for Managing Orders")
@RestController
@RequestMapping("/api/orders/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Add a new Order",
            description = "Add a new Order from a List of Products IDs",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Order.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public Order create(@RequestBody List<OrderRecord> orderRecord, @RequestHeader("Authorization") String token) {
        return orderService.createOrder(orderRecord, token);
    }

    @GetMapping(produces = {"application/json"})
    @Operation(
            summary = "Find all Order",
            description = "Find all Order and turn back in a list",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Order.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<PagedModel<Order>> getALL(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Order> orders = orderService.findAll(pageable);

        PagedModel<Order> pagedModel = PagedModel.of(orders.getContent().stream()
                        .map(this::getConfiguredOrder).collect(Collectors.toList()),
                new PagedModel.PageMetadata(
                        orders.getSize(),
                        orders.getNumber(),
                        orders.getTotalElements(),
                        orders.getTotalPages()
                ));
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @GetMapping(value = "{id}", produces = {"application/json"})
    @Operation(
            summary = "Find Order by id",
            description = "Find Order by id and turn back in a list",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Order.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PutMapping(value = "{id}", produces = {"application/json"}, consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Update a Order",
            description = "Update a Order by ID",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(
                            description = "Success order updated successfully",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Order.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Order> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody Order order) {
        order.setId(id);
        Order updatedOrder = orderService.update(order);
        return ResponseEntity.status(HttpStatus.OK).body(getConfiguredOrder(updatedOrder));
    }

    @DeleteMapping(value = "{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Delete a order",
            description = "Delete a order by id",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Order getConfiguredOrder(Order order) {
        order.add(linkTo(methodOn(OrderController.class).deleteOrder(order.getId())).withRel("delete").withType("DELETE"));
        order.add(linkTo(methodOn(OrderController.class).updateOrder(order.getId(), order)).withRel("update").withType("PUT"));
        order.add(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel().withType("GET"));
        return order;
    }
}
