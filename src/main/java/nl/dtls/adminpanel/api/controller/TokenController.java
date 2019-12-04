package nl.dtls.adminpanel.api.controller;

import javax.validation.Valid;
import nl.dtls.adminpanel.api.dto.auth.AuthDTO;
import nl.dtls.adminpanel.service.jwt.JwtTokenProvider;
import nl.dtls.shared.api.dto.auth.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<TokenDTO> generateToken(@RequestBody @Valid AuthDTO data) {
        try {
            String username = data.getEmail();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username);
            return ResponseEntity.ok()
                .body(new TokenDTO(token));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}