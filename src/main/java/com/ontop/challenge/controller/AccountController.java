package com.ontop.challenge.controller;

import com.ontop.challenge.config.JwtTokenUtil;
import com.ontop.challenge.dto.request.AccountCreationRequest;
import com.ontop.challenge.dto.request.LoginRequest;
import com.ontop.challenge.dto.response.JwtResponse;
import com.ontop.challenge.exception.ErrorResponse;
import com.ontop.challenge.model.Accounts;
import com.ontop.challenge.model.Users;
import com.ontop.challenge.repositories.UserRepository;
import com.ontop.challenge.service.AccountService;
import com.ontop.challenge.service.JwtUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;


    public AccountController(AccountService accountService, AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;

    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountCreationRequest request) {
        //try {
        Accounts checkIfUserExist = accountService.findByAccountNumber(request.getAccountNumber());
        if (checkIfUserExist != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse(HttpStatus.OK.value(), "VALID_BODY", "User account already exist"));
        }
        return ResponseEntity.ok().body(accountService.createAccount(request));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", "something bad happened"));
//        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findAccount(@RequestHeader("Authorization") String Authorization, @PathVariable(name = "id") Integer id) {
        try {
            Accounts accounts = accountService.findAccount(id);
            if (accounts == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "INVALID_USER", "user not found"));
            }
            return ResponseEntity.ok().body(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", "something bad happened"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAccount(@RequestHeader("Authorization") String Authorization) {
        try {
            List<Accounts> accountsList = accountService.getAllAccount();

            if (accountsList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "INVALID_USER", "no record found"));
            }
            return ResponseEntity.ok().body(accountService.getAllAccount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GENERIC_ERROR", "something bad happened"));
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);


        Users user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: "));

        return ResponseEntity.ok(new JwtResponse("Bearer " + token, authenticationRequest.getUsername(), user.getId()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
