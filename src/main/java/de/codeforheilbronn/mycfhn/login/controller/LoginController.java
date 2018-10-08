package de.codeforheilbronn.mycfhn.login.controller;

import de.codeforheilbronn.mycfhn.login.model.LoginData;
import de.codeforheilbronn.mycfhn.login.model.LoginResponse;
import de.codeforheilbronn.mycfhn.login.service.LdapService;
import de.codeforheilbronn.mycfhn.login.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {

    @NonNull
    private LdapService ldapService;
    @NonNull
    private TokenService tokenService;

    @Value("${cfhn.login.domain}")
    private String cookieDomain;
    @Value("${cfhn.login.secure}")
    private boolean cookieSecure;

    @PostMapping(value = "/token.json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(
            @RequestBody LoginData data,
            HttpServletResponse response
    ) {
        String token = tokenService.generateToken( ldapService.authenticate(data.getUsername(), data.getPassword()));
        Cookie cookie = new Cookie("cfhn", token);
        cookie.setDomain(cookieDomain);
        cookie.setSecure(cookieSecure);
        cookie.setMaxAge(60*60*24*7*2);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new LoginResponse(token);
    }

    @PostMapping(value = "/token.form", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public LoginResponse loginForm(@RequestParam String redirect, @ModelAttribute LoginData data, HttpServletResponse response) {
        LoginResponse token = login(data, response);
        response.addHeader("Location", redirect);
        response.setStatus(HttpStatus.SEE_OTHER.value());
        return token;
    }
}
