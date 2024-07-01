package com.tizo.br.controllers;

import com.tizo.br.model.vo.security.UserAccountRecordVO;
import com.tizo.br.model.vo.security.UserInfosVO;
import com.tizo.br.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Users", description = "Endpoints for Managing Users")
@RestController
@RequestMapping("/api/users/v1")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping(produces = {"application/json"})
    @Operation(
            summary = "Find all users",
            description = "Find all users and turn back in a list",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = UserInfosVO.class)
                                    )
                            )
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)

            }
    )
    public ResponseEntity<List<UserInfosVO>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    @PostMapping(
            value = "/recordUser/commonUser",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new User with common user role",
            description = "Adds a new Person by passing in a JSON, this user has the accesses of a regular user.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserInfosVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<UserInfosVO> createCommonUser(@RequestBody UserAccountRecordVO user) {
        List<Long> authorities = new ArrayList<>();
        authorities.add(3L);
        return ResponseEntity.ok(service.createUser(user, authorities));
    }

    @PostMapping(
            value ="/recordUser/managerUser",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Adds a new User with common and manager user role",
            description = "Adds a new Person by passing in a JSON, this user has the access rights of a regular user and a manager.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserInfosVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<UserInfosVO> createManager(@RequestBody UserAccountRecordVO user) {
        List<Long> authorities = new ArrayList<>();
        authorities.add(2L);
        authorities.add(3L);
        return ResponseEntity.ok(service.createUser(user, authorities));
    }
}
