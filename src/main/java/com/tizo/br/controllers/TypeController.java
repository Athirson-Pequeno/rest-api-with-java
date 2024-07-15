package com.tizo.br.controllers;

import com.tizo.br.model.Type;
import com.tizo.br.model.vo.security.UserInfosVO;
import com.tizo.br.services.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Types", description = "Endpoints for Managing Type of Products")
@RestController
@RequestMapping("/api/types/v1")
public class TypeController {

    @Autowired
    private TypeService typeService;


    @GetMapping(name = "/findAll", produces = {"application/json"})
    @Operation(
            summary = "Find all Types",
            description = "Find all Types and turn back in a list",
            tags = {"Types"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Type.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)

            }
    )
    public ResponseEntity<List<Type>> getAllTypes() {
        return ResponseEntity.ok(typeService.getAllTypes());
    }

    @PostMapping(name = "/createTypes",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new Type",
            description = "Adds a new Type by passing in a JSON",
            tags = {"Types"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Type.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Type> createType(@RequestBody Type type) {

        return ResponseEntity.ok(typeService.createType(type));
    }

}
