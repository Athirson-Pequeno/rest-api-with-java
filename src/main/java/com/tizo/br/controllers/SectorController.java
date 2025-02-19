package com.tizo.br.controllers;

import com.tizo.br.model.Sector;
import com.tizo.br.services.SectorService;
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

@Tag(name = "Sectors", description = "Endpoints for Managing Sectors of Products")
@RestController
@RequestMapping("/api/sectors/v1")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping(
            produces = {"application/json"})
    @Operation(
            summary = "Find all Sectors",
            description = "Find all Sectors and turn back in a list",
            tags = {"Sectors"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Sector.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<PagedModel<Sector>> getAllSectors(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Sector> sectors = sectorService.findAll(pageable);

        PagedModel<Sector> pagedModel = PagedModel.of(sectors.getContent().stream()
                .map(this::getConfiguredSector).collect(Collectors.toList()),
                new PagedModel.PageMetadata(
                   sectors.getSize(),
                   sectors.getNumber(),
                   sectors.getTotalElements(),
                   sectors.getTotalPages()
                ));

        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @GetMapping(
            value = "{id}",
            produces = {"application/json"})
    @Operation(
            summary = "Find a sector",
            description = "Find sector by id",
            tags = {"Sectors"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = Sector.class))
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})

    public ResponseEntity<Sector> getSectorById(@PathVariable("id") Long id) {
        Sector sector = sectorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getConfiguredSector(sector));
    }

    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            summary = "Adds a new Sector",
            description = "Adds a new Sector by passing in a JSON",
            tags = {"Sectors"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Sector.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Sector> createSector(@Valid @RequestBody Sector sector) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectorService.create(sector));
    }

    @DeleteMapping(value = "{id}")
    @Operation(
            summary = "Delete a Sector",
            description = "Delete a sector by ID",
            tags = {"Sectors"},
            responses = {
                    @ApiResponse(
                            description = "No Content (Sector deleted successfully)",
                            responseCode = "204",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Bad Request (Invalid ID format)",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Unauthorized (User not authenticated)",
                            responseCode = "401",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Not Found (Sector ID does not exist)",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<?> deleteSector(@PathVariable Long id) {
        sectorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(
            value = "{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Update a sector",
            description = "Update a sector by id",
            tags = {"Sectors"},
            responses = {
                    @ApiResponse(description = "Success (Sector updated successfully)", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Sector.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)}
    )
    public ResponseEntity<Sector> updateSector(@PathVariable Long id, @Valid @RequestBody Sector sector) {
        sector.setId(id);
        Sector updatedSector = sectorService.update(sector);
        return ResponseEntity.status(HttpStatus.OK).body(getConfiguredSector(updatedSector));
    }

    private Sector getConfiguredSector(Sector sector) {
        sector.add(linkTo(methodOn(SectorController.class).deleteSector(sector.getId())).withRel("delete").withType("DELETE"));
        sector.add(linkTo(methodOn(SectorController.class).updateSector(sector.getId(),sector)).withRel("update").withType("PUT"));
        sector.add(linkTo(methodOn(SectorController.class).getSectorById(sector.getId())).withSelfRel().withType("GET"));
        return sector;
    }
}
