package com.vbarzola.catalogo.user.infrastructure.persistence.repository;

import com.vbarzola.catalogo.user.infrastructure.persistence.entity.UserEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findOneByActivationKey(String activationKey);

    List<UserEntity> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<UserEntity> findOneByResetKey(String resetKey);

    Optional<UserEntity> findOneByEmailIgnoreCase(String email);

    Optional<UserEntity> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<UserEntity> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
