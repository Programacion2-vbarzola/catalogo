package com.vbarzola.catalogo.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String login;

    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private boolean activated;
    private String langKey;
    private String imageUrl;

    @JsonIgnore
    private String activationKey;

    @JsonIgnore
    private String resetKey;

    private Instant resetDate;

    @JsonIgnore
    @Builder.Default
    private Set<String> authorities = new HashSet<>();

    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
