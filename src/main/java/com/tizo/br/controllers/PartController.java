package com.tizo.br.controllers;

import com.tizo.br.model.Part;
import com.tizo.br.services.PartsService;
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

@Tag(name = "Parts", description = "Endpoints for Managing Type of Part of Products")
@RestController
@RequestMapping("/api/parts/v1")
public class PartController {

    @Autowired
    private PartsService partsService;

    @GetMapping(
            produces = {"application/json"})
    @Operation(
            summary = "Find all Part",
            description = "Find all Part and turn back in a list",
            tags = {"Parts"},
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
    public ResponseEntity<PagedModel<Part>> getAllParts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Part> parts = partsService.findAll(pageable);

        PagedModel<Part> pagedModel = PagedModel.of(parts.getContent().stream()
                        .map(this::getConfiguedPart).collect(Collectors.toList()),
                new PagedModel.PageMetadata(
                        parts.getSize(),
                        parts.getNumber(),
                        parts.getTotalElements(),
                        parts.getTotalPages()
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new Part",
            description = "Adds a new Part by passing in a JSON",
            tags = {"Parts"},
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

    @DeleteMapping(value = "{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Part a Part",
            description = "Part a part by ID",
            tags = {"Parts"},
            responses = {
                    @ApiResponse(description = "No Content (Part deleted successfully)", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request (Invalid ID format)", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized (User not authenticated)", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found (Part ID does not exist)", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> deletePart(@PathVariable("id") Long id) {
        partsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    @PutMapping(
            value = "{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Update a Part",
            description = "Update a Part by ID",
            tags = {"Parts"},
            responses = {
                    @ApiResponse(
                            description = "Success part updated successfully",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Part.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Part> updatePart(@PathVariable("id") Long id, @Valid @RequestBody Part part) {
        part.setId(id);
        Part updatedpart = partsService.update(part);
        return ResponseEntity.status(HttpStatus.OK).body(updatedpart);
    }

    @GetMapping(
            value = "{id}",
            produces = {"application/json"})
    @Operation(
            summary = "Find a part",
            description = "Find part by id",
            tags = {"Parts"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = Part.class))
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Part> getPartById(@PathVariable("id") Long id) {
        Part part = partsService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getConfiguedPart(part));
    }

    private Part getConfiguedPart(Part part) {
        part.add(linkTo(methodOn(PartController.class).deletePart(part.getId())).withRel("delete").withType("DELETE"));
        part.add(linkTo(methodOn(PartController.class).updatePart(part.getId(), part)).withRel("update").withType("PUT"));
        part.add(linkTo(methodOn(PartController.class).getPartById(part.getId())).withSelfRel().withType("GET"));

        return part;
    }
}
