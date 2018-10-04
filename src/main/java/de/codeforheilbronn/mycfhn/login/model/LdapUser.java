package de.codeforheilbronn.mycfhn.login.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Data
@Entry(objectClasses = {"inetOrgPerson", "member"})
public final class LdapUser {
    @Id
    @JsonSerialize(using = NameSerializer.class)
    private Name dn;
    @Attribute(name = "cn")
    private String username;
    private String birthday;
    @Attribute(name = "displayName")
    private String fullName;
    private String mail;
    private String entryDate;
    private byte[] userPassword;

    @Attribute(name = "memberOf")
    private List<String> groups;

    static class NameSerializer extends JsonSerializer<Name> {
        @Override
        public void serialize(Name name, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(String.join(",", Collections.list(name.getAll())));
        }
    }
}
