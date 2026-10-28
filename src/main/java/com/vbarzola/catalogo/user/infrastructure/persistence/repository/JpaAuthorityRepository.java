package com.vbarzola.catalogo.user.infrastructure.persistence.repository;

import com.vbarzola.catalogo.user.infrastructure.persistence.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthorityRepository extends JpaRepository<AuthorityEntity, String> {}
