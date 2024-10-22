package com.tizo.br.controllers;

import com.tizo.br.model.Order;
import com.tizo.br.model.vo.security.OrderRecord;
import com.tizo.br.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "Endpoints for Managing Orders")
@RestController
@RequestMapping("/api/orders/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(name = "/create",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Add a new Order",
            description = "Add a new Order from a List of Products IDs",
            tags = {"Order"},
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

    @GetMapping(name = "/findAll", produces = {"application/json"})
    @Operation(
            summary = "Find all Order",
            description = "Find all Order and turn back in a list",
            tags = {"Order"},
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
    public List<Order> getALL() {
        return orderService.findAll();
    }

}
