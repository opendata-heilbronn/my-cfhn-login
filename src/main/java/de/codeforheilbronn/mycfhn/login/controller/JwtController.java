package de.codeforheilbronn.mycfhn.login.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("jwt")
public class JwtController {

    @Value("${cfhn.login.publicKey}")
    private String publicKey;

    @GetMapping("/public-key")
    public String getPublicKey() {
        return publicKey;
    }
}
