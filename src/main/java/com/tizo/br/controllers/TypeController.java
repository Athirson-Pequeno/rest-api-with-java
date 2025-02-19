package com.tizo.br.controllers;

import com.tizo.br.model.Type;
import com.tizo.br.services.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Types", description = "Endpoints for Managing Type of Products")
@RestController
@RequestMapping("/api/types/v1")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping(produces = {"application/json"})
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
    public ResponseEntity<PagedModel<Type>> getAllTypes(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Type> types = typeService.findAll(pageable);

        PagedModel<Type> pagedModel = PagedModel.of(types.getContent().stream().map(this::getConfiguredType).collect(Collectors.toList()),
                new PagedModel.PageMetadata(
                        types.getSize(),
                        types.getNumber(),
                        types.getTotalElements(),
                        types.getTotalPages()
                ));

        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new Type",
            security = @SecurityRequirement(name = "bearer-key"),
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
        return ResponseEntity.status(HttpStatus.CREATED).body(typeService.create(type));
    }

    @DeleteMapping(value = "{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Delete a Type",
            description = "Delete a type by ID",
            tags = {"Types"},
            responses = {
                    @ApiResponse(description = "No Content (Type deleted successfully)", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request (Invalid ID format)", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized (User not authenticated)", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found (Type ID does not exist)", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> deleteType(@PathVariable("id") Long id) {
        typeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(
            value = "{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Update a Type",
            description = "Update a Type by ID",
            tags = {"Types"},
            responses = {
                    @ApiResponse(
                            description = "Success type updated successfully",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Type.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Type> updateType(Long id, Type type) {
        type.setId(id);
        Type updatedType = typeService.update(type);
        return ResponseEntity.status(HttpStatus.OK).body(updatedType);
    }

    @GetMapping(
            value = "{id}",
            produces = {"application/json"})
    @Operation(
            summary = "Find a type",
            description = "Find type by id",
            tags = {"Types"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = Type.class))
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Type> getTypeById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(typeService.findById(id));
    }

    private Type getConfiguredType(Type type) {
        type.add(linkTo(methodOn(TypeController.class).deleteType(type.getId())).withRel("delete").withType("DELETE"));
        type.add(linkTo(methodOn(TypeController.class).updateType(type.getId(), type)).withRel("update").withType("PUT"));
        type.add(linkTo(methodOn(TypeController.class).getTypeById(type.getId())).withSelfRel().withType("GET"));
        return type;
    }
}
