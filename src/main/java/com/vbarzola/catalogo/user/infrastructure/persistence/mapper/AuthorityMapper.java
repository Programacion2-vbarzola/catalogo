package com.vbarzola.catalogo.user.infrastructure.persistence.mapper;

import com.vbarzola.catalogo.user.domain.model.Authority;
import com.vbarzola.catalogo.user.infrastructure.persistence.entity.AuthorityEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorityMapper {

    public AuthorityEntity toEntity(Authority domain) {
        if (domain == null) {
            return null;
        }
        return new AuthorityEntity().name(domain.getName());
    }

    public Authority toDomainModel(AuthorityEntity entity) {
        if (entity == null) {
            return null;
        }
        return Authority.builder().name(entity.getName()).build();
    }
}
