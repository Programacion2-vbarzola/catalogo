package com.vbarzola.catalogo.user.infrastructure.persistence.mapper;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.infrastructure.persistence.entity.AuthorityEntity;
import com.vbarzola.catalogo.user.infrastructure.persistence.entity.UserEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setActivated(domain.isActivated());
        entity.setLangKey(domain.getLangKey());
        entity.setImageUrl(domain.getImageUrl());
        entity.setActivationKey(domain.getActivationKey());
        entity.setResetKey(domain.getResetKey());
        entity.setResetDate(domain.getResetDate());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setLastModifiedBy(domain.getLastModifiedBy());
        entity.setLastModifiedDate(domain.getLastModifiedDate());
        entity.setAuthorities(authoritiesToEntities(domain.getAuthorities()));
        return entity;
    }

    public User toDomainModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
            .id(entity.getId())
            .login(entity.getLogin())
            .password(entity.getPassword())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .email(entity.getEmail())
            .activated(entity.isActivated())
            .langKey(entity.getLangKey())
            .imageUrl(entity.getImageUrl())
            .activationKey(entity.getActivationKey())
            .resetKey(entity.getResetKey())
            .resetDate(entity.getResetDate())
            .createdBy(entity.getCreatedBy())
            .createdDate(entity.getCreatedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .authorities(authoritiesToNames(entity.getAuthorities()))
            .build();
    }

    private Set<AuthorityEntity> authoritiesToEntities(Set<String> authorityNames) {
        if (authorityNames == null) {
            return new HashSet<>();
        }
        return authorityNames.stream().map(name -> new AuthorityEntity().name(name)).collect(Collectors.toSet());
    }

    private Set<String> authoritiesToNames(Set<AuthorityEntity> authorities) {
        if (authorities == null) {
            return new HashSet<>();
        }
        return authorities.stream().map(AuthorityEntity::getName).collect(Collectors.toSet());
    }
}
