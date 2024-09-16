package com.tizo.br.controllers;

import com.tizo.br.model.Part;
import com.tizo.br.repositories.SectorRepository;
import com.tizo.br.services.PartsService;
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

@Tag(name = "Part", description = "Endpoints for Managing Type of Part of Products")
@RestController
@RequestMapping("/api/parts/v1")
public class PartController {

    @Autowired
    private PartsService partsService;

    @Autowired
    private SectorRepository repository;

    @GetMapping(name = "/findAll", produces = {"application/json"})
    @Operation(
            summary = "Find all Part",
            description = "Find all Part and turn back in a list",
            tags = {"Part"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Part.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)

            }
    )
    public ResponseEntity<List<Part>> getAllParts() {
        return ResponseEntity.ok(partsService.getAllParts());
    }

    @PostMapping(name = "/create",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new Part",
            description = "Adds a new Part by passing in a JSON",
            tags = {"Part"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Part.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Part> createType(@RequestBody Part part) {
        System.out.println(part);
        return ResponseEntity.ok(partsService.createPart(part));
    }

}
