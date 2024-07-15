package com.tizo.br.controllers;

import com.tizo.br.model.Sector;
import com.tizo.br.model.vo.security.UserInfosVO;
import com.tizo.br.services.SectorService;
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

@Tag(name = "Sectors", description = "Endpoints for Managing Sectors of Products")
@RestController
@RequestMapping("/api/sectors/v1")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping(name = "/findAll", produces = {"application/json"})
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
    public ResponseEntity<List<Sector>> getAllSectors() {
        return ResponseEntity.ok(sectorService.getAllSectors());
    }

    @PostMapping(name = "/createSector",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new Sector",
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
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector) {
        return ResponseEntity.ok(sectorService.createSector(sector));
    }

}
