package com.DEMOJWT.demo.controller;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repositories.UserRepository;
import com.DEMOJWT.demo.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("crearuser")
    public User crearUsuario(@RequestParam("user") String username, @RequestParam("password") String pwd) {

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUser(username);
        user.setPwd(pwd);
        return userService.save(user);

    }

    @GetMapping("user")
    public ResponseEntity<?> obtener(@RequestParam("user") String username, @RequestParam("password") String pwd){
        String token = getJWTToken(username);

        Map<String, Object> response = new HashMap<>();
        try{
            User userValidation = userService.findByUSerAndPdw(username, pwd);
            userValidation.setToken(token);
            response.put("User: ", userValidation);
        } catch (Exception e) {
            response.put("Mensaje", "El usuario " + "'" + username  + "'" + " no fue encontrado");
        }
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("sofkaJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Valido " + token;
    }
}
