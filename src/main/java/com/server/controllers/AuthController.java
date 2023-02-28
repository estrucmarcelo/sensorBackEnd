package com.server.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.dto.AuthMessageResponse;
import com.server.dto.UserSessionResponse;
import com.server.exception.TokenRefreshException;
import com.server.models.ERole;
import com.server.models.RefreshToken;
import com.server.models.Role;
import com.server.models.User;
// import com.server.repositories.RoleRepository;
import com.server.repositories.UserRepository;
import com.server.security.jwt.JwtUtils;
import com.server.security.requests.LoginRequest;
import com.server.security.requests.SignupRequest;
import com.server.security.services.RefreshTokenService;
import com.server.security.services.UserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  // @Autowired
  // private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    // List<String> roles = userDetails.getAuthorities().stream()
    // .map(item -> item.getAuthority())
    // .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
        .body(new UserSessionResponse(userDetails.getId(), userDetails.getEmail()));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
    Optional<User> userFound = userRepository.getUserByEmail(signUpRequest.getEmail());

    if (userFound.isPresent()) {
      return ResponseEntity.badRequest().body(new AuthMessageResponse("Email is already taken."));
    }

    User user = new User(signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    // Set<String> strRoles = signUpRequest.getRole();

    // Set<Role> roles = new HashSet<>();
   
    // Role defaultRole = new Role();
    // defaultRole.setId(0);
    // defaultRole.setName(ERole.ROLE_USER);

    // roles.add(defaultRole);

    // if (strRoles == null) {
    //   Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    //       .orElseThrow(() -> new RuntimeException("Role not found."));
    //   roles.add(userRole);
    // } else {
    //   strRoles.forEach(role -> {
    //     switch (role) {
    //       default:
    //         Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    //             .orElseThrow(() -> new RuntimeException("Role not found."));
    //         roles.add(userRole);
    //     }
    //   });
    // }

    // user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new AuthMessageResponse("User registered successfully."));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principle.toString() != "anonymousUser") {
      String userId = ((UserDetailsImpl) principle).getId();
      System.out.println("user signed out, delete refresh token");
      refreshTokenService.deleteByUserId(userId);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtUtils.getCleanJwtCookie().toString())
        .header(HttpHeaders.SET_COOKIE, jwtUtils.getCleanJwtRefreshCookie().toString())
        .body(new AuthMessageResponse("You have signed out."));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
    String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

    if ((refreshToken != null) && (refreshToken.length() > 0)) {
      return refreshTokenService.findByToken(refreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getUser)
          .map(user -> {
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new AuthMessageResponse("Token refreshed successfully."));
          })
          .orElseThrow(() -> new TokenRefreshException(refreshToken,
              "Refresh token not found."));
    }

    return ResponseEntity.badRequest().body(new AuthMessageResponse("Refresh token is empty."));
  }
}
