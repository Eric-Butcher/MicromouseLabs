package com.micromouselab.mazes.service;

import com.micromouselab.mazes.controller.AuthController;
import com.micromouselab.mazes.domain.AuthResponseDTO;
import com.micromouselab.mazes.domain.RegisterDTO;
import com.micromouselab.mazes.domain.Role;
import com.micromouselab.mazes.domain.User;
import com.micromouselab.mazes.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.duration}")
    private int jwtDurationMs;

    @Value("${jwt.refresh}")
    private int jwtRefreshMs;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private SecretKey secretKey;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void key() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public AuthResponseDTO authenticateUser(String username, String plaintextPassword){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, plaintextPassword));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(accessToken, refreshToken);
        return authResponseDTO;
    }

    @Transactional
    public void registerUser(RegisterDTO registerRequest){

        if (userRepository.existsByUsername(registerRequest.username())){
            throw new IllegalArgumentException("Username is taken.");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.plaintextPassword());
        User newUser = new User(registerRequest.username(), hashedPassword);
        userRepository.save(newUser);
    }


    public String generateAccessToken(UserDetails userDetails){

        Date currentTime = new Date();
        Date expiryTime = new Date(currentTime.getTime() + jwtDurationMs);
        String role = userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(currentTime)
                .expiration(expiryTime)
                .signWith(this.secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");

        Date currentTime = new Date();
        Date expiryTime = new Date(currentTime.getTime() + jwtRefreshMs);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(currentTime)
                .expiration(expiryTime)
                .signWith(this.secretKey, Jwts.SIG.HS256)
                .compact();
    }

//    public UserDetails validateToken(String jwt) throws IllegalArgumentException {
//        Optional<String> username = extractUsernameFromJwt(jwt);
//        if (username.isPresent()){
//            return userDetailsService.loadUserByUsername(username.get());
//        }
//        throw new IllegalArgumentException("Could not validate username with jwt.");
//    }

    public UserDetails validateToken(String jwt) throws IllegalArgumentException {
        Claims claims = Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        return new org.springframework.security.core.userdetails.User(
                username,
                "",
                List.of(new SimpleGrantedAuthority(role))
        );
    }



    private Optional<String> extractUsernameFromJwt(String jwt){
        return Optional.ofNullable(Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(jwt).getPayload().getSubject());
    }

    private boolean isRefreshJwt(String jwt){
        Claims claims = Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        String tokenType = claims.get("tokenType").toString();
        final String refreshTokenType = "refresh";
        boolean isRefreshTokenType = tokenType.equals(refreshTokenType);
        return isRefreshTokenType;
    }



}
