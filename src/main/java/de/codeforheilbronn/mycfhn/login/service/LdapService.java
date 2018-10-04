package de.codeforheilbronn.mycfhn.login.service;

import de.codeforheilbronn.mycfhn.login.model.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.directory.api.ldap.model.password.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LdapService {

    private LdapTemplate ldap;

    @Autowired
    public LdapService(LdapTemplate ldap) {
        this.ldap = ldap;
    }

    public LdapUser authenticate(String username, String password) {
        LdapUser user = ldap.findOne(LdapQueryBuilder.query()
                .where("cn").is(username), LdapUser.class);
        if(!PasswordUtil.compareCredentials(password.getBytes(StandardCharsets.UTF_8), user.getUserPassword())) {
            throw new AuthorizationException(username);
        }
        return user;
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Could not authorize user")
    static class AuthorizationException extends RuntimeException {
        AuthorizationException(String username) {
            super("Could not authorize " + username);
        }
    }
}
