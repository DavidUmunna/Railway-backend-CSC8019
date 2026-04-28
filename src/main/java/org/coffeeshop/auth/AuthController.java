package org.coffeeshop.auth;

import jakarta.validation.Valid;
import org.coffeeshop.auth.dtos.AuthResponsedto;
import org.coffeeshop.auth.dtos.LoginRequestdto;
import org.coffeeshop.security.JwtService;
import org.coffeeshop.security.StaffUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.coffeeshop.users.models.Staff;
import org.springframework.web.bind.annotation.PostMapping;
// import your custom UserDetails implementation if it exists, e.g.:
// import org.coffeeshop.security.StaffUserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
/**
 * Handles authentication endpoints.
 * @author willian
 * @version 1.0
 */
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user and returns a JWT for subsequent requests.
     *
     * @param request login credentials
     * @return token response including bearer token and role
     */
    @PostMapping("/login")
   public ResponseEntity<AuthResponsedto> login(@Valid @RequestBody LoginRequestdto request) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            )
    );

    StaffUserDetails userDetails = (StaffUserDetails) authentication.getPrincipal();
    String token = jwtService.generateToken(userDetails);

    String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse("ROLE_USER");

    return ResponseEntity.ok(
            new AuthResponsedto(
                    token,
                    userDetails.getUsername(),
                    userDetails.getId(),
                    role
            )
    );
}


}
