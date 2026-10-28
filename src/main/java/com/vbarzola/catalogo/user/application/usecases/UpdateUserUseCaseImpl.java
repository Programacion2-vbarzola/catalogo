package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.UpdateUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.AuthorityRepository;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateUserUseCaseImpl.class);

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public Optional<User> updateUser(User user) {
        return userRepository
            .findById(user.getId())
            .map(existing -> {
                existing.setLogin(user.getLogin().toLowerCase());
                existing.setFirstName(user.getFirstName());
                existing.setLastName(user.getLastName());
                if (user.getEmail() != null) {
                    existing.setEmail(user.getEmail().toLowerCase());
                }
                existing.setImageUrl(user.getImageUrl());
                existing.setActivated(user.isActivated());
                existing.setLangKey(user.getLangKey());
                Set<String> validAuthorities = new HashSet<>();
                if (user.getAuthorities() != null) {
                    for (String authorityName : user.getAuthorities()) {
                        if (authorityRepository.findById(authorityName).isPresent()) {
                            validAuthorities.add(authorityName);
                        }
                    }
                }
                existing.setAuthorities(validAuthorities);
                User saved = userRepository.save(existing);
                LOG.debug("Changed Information for User: {}", saved);
                return saved;
            });
    }
}
