package EnzoMendes.com.github.controllers;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.data.dto.security.AccountCredentialsDTO;
import EnzoMendes.com.github.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint.")
@RestController
@RequestMapping("/auth")
public class AuthController implements EnzoMendes.com.github.controllers.docs.AuthControllerDocs {

    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials){
         if(credentialIsInvalid(credentials)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");

         var token = service.singIn(credentials);

         if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");

         return ResponseEntity.ok().body(token);
    }
    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable(value = "username") String username,
                                          @RequestHeader("Authorization") String refreshToken){
         if(parametersAreInvalid(username, refreshToken)){
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");
         }

         var token = service.refreshToken(username, refreshToken);

         if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");

         return ResponseEntity.ok().body(token);
    }

    @PostMapping(
            value = "/createUser",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials){
        return service.create(credentials);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null ||
                StringUtils.isBlank(credentials.getPassword()) ||
                        StringUtils.isBlank(credentials.getUsername());
    }

}
