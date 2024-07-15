package com.tizo.br.controllers;

import com.tizo.br.model.vo.security.AccountCredentialsVO;
import com.tizo.br.model.vo.security.TokenVO;
import com.tizo.br.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Endpoints for Managing Authentication of Users")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @PostMapping(
            value = "/signin",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Generate a JWT token",
            description = "Generate a JWT token when the user passes their credentials",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity signIn(@RequestBody AccountCredentialsVO data) {

        if (checkIfParamsIsNotNull(data))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

        var token = authServices.signIn(data);

        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

        return token;
    }

    @SuppressWarnings("rawtypes")
    @PutMapping(value = "/refresh/{email}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(summary = "Refresh the JWT token",
            description = "Generate a JWT token when the user passes their email and their old token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity refreshToken(@PathVariable("email") String email,
                                       @RequestHeader("Authorization") String refreshToken) {

        if (checkIfParamsIsNotNull(email, refreshToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

        var token = authServices.refreshToken(email, refreshToken);

        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

        return token;
    }

    private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getEmail() == null || data.getEmail().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }

    private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }
}
