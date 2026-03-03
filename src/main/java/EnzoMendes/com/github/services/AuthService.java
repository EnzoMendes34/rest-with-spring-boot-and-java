package EnzoMendes.com.github.services;

import EnzoMendes.com.github.data.dto.security.AccountCredentialsDTO;
import EnzoMendes.com.github.data.dto.security.TokenDTO;
import EnzoMendes.com.github.exceptions.RequiredObjectIsNullException;
import EnzoMendes.com.github.model.User;
import EnzoMendes.com.github.repositories.UserRepository;
import EnzoMendes.com.github.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static EnzoMendes.com.github.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository repository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository repository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.repository = repository;
    }

    public ResponseEntity<TokenDTO> singIn(AccountCredentialsDTO credentials){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername());
        if(user == null) throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found.");

        var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username,String refreshToken){
        var user = repository.findByUsername(username);
        TokenDTO token;
        if(user != null) {
            token = tokenProvider.refreshAccessToken(refreshToken);
        } else{
            throw new UsernameNotFoundException("Username " + username + " not found.");
        }
        return ResponseEntity.ok(token);
    }

    private String generateHashedPassword(String password){
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(passwordEncoder);
        return passwordEncoder.encode(password);
    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user){
        if(user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one new User");
        var entity = new User();
        entity.setFullName(user.getFullName());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setUserName(user.getUsername());
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        var dto = repository.save(entity);

        return new AccountCredentialsDTO(dto.getUsername(), dto.getPassword(), dto.getFullName());
    }
}
