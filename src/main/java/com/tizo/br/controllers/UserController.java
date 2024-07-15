package com.tizo.br.controllers;

import com.tizo.br.model.enums.UsersPermission;
import com.tizo.br.model.vo.security.UserAccountRecordVO;
import com.tizo.br.model.vo.security.UserInfosVO;
import com.tizo.br.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/findAll", produces = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
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
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
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
        List<String> authorities = new ArrayList<>();
        authorities.add(UsersPermission.COMMON_USER.name());
        return ResponseEntity.ok(service.createUser(user, authorities));
    }

    @PostMapping(
            value = "/recordUser/managerUser",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Adds a new User with common and manager user role",
            description = "Adds a new Person by passing in a JSON, this user has the access rights of a regular user and a manager.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserInfosVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<UserInfosVO> createManager(@RequestBody UserAccountRecordVO user) {
        List<String> authorities = new ArrayList<>();
        authorities.add(UsersPermission.MANAGER.name());
        authorities.add(UsersPermission.COMMON_USER.name());
        return ResponseEntity.ok(service.createUser(user, authorities));
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            summary = "Delete a User",
            description = "Delete a User by passing in a id in url",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
