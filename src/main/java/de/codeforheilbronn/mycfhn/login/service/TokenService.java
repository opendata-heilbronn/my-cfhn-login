package de.codeforheilbronn.mycfhn.login.service;

import de.codeforheilbronn.mycfhn.login.model.LdapUser;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class TokenService {

    @Value("${cfhn.login.expireDays:14}")
    private int expireDays;

    private PrivateKey privateKey;

    public TokenService(@Value("${cfhn.login.privateKey}") String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String encodedKey = privateKeyString
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        privateKey = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.decode(encodedKey))
        );
    }

    public String generateToken(LdapUser user) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(todayPlus(expireDays))
                .setIssuedAt(todayPlus(0))
                .setIssuer("mycfhn-login")
                .setAudience("mycfhn")
                .setSubject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("birthday", user.getBirthday())
                .claim("mail", user.getMail())
                .claim("entryDate", user.getEntryDate())
                .claim("groups", user.getGroups())
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    private Date todayPlus(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

}
