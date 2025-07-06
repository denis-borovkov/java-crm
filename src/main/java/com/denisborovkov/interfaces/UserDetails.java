package com.denisborovkov.interfaces;

import com.denisborovkov.models.User;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.denisborovkov.models.User.class, name = "com.denisborovkov.models.User")
})

public interface UserDetails {

    Long getId();
    String getName();
    String getEmail();
    String getPassword();
    User setName(String name);
    User setEmail(String email);
    User setPassword(String password);
    User build();
}
