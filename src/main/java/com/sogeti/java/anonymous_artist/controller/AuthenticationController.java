package com.sogeti.java.anonymous_artist.controller;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/anonymous-artist/api/auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("authenticate")
    public ResponseEntity<String> loginUser(@RequestBody @Valid AccountRequest accountRequest) {
        authenticationService.authenticateUser(accountRequest);
        String jwt = authenticationService.generateJwt(accountRequest.email());
        String userFirstName = authenticationService.getFirstNameOfUserByEmail(accountRequest.email());

        return ResponseEntity.ok().body("Hi there, " + userFirstName + "!\njwt: " + jwt);
    }
}
