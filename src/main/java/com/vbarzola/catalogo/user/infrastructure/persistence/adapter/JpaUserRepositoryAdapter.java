package com.vbarzola.catalogo.user.infrastructure.persistence.adapter;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import com.vbarzola.catalogo.user.infrastructure.persistence.mapper.UserMapper;
import com.vbarzola.catalogo.user.infrastructure.persistence.repository.JpaUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findOneByActivationKey(String activationKey) {
        return jpaRepository.findOneByActivationKey(activationKey).map(mapper::toDomainModel);
    }

    @Override
    public List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime) {
        return jpaRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(dateTime)
            .stream()
            .map(mapper::toDomainModel)
            .toList();
    }

    @Override
    public Optional<User> findOneByResetKey(String resetKey) {
        return jpaRepository.findOneByResetKey(resetKey).map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findOneByEmailIgnoreCase(String email) {
        return jpaRepository.findOneByEmailIgnoreCase(email).map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findOneByLogin(String login) {
        return jpaRepository.findOneByLogin(login).map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findOneWithAuthoritiesByLogin(String login) {
        return jpaRepository.findOneWithAuthoritiesByLogin(login).map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return jpaRepository.findOneWithAuthoritiesByEmailIgnoreCase(email).map(mapper::toDomainModel);
    }

    @Override
    public Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable) {
        return jpaRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(mapper::toDomainModel);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomainModel);
    }

    @Override
    public User save(User user) {
        return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(user)));
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(mapper.toEntity(user));
    }

    @Override
    public void flush() {
        jpaRepository.flush();
    }
}
