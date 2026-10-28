package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.security.SecurityUtils;
import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.UpdateCurrentUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCurrentUserUseCaseImpl implements UpdateCurrentUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateCurrentUserUseCaseImpl.class);

    private final UserRepository userRepository;

    @Override
    public void updateCurrentUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                User saved = userRepository.save(user);
                LOG.debug("Changed Information for User: {}", saved);
            });
    }
}
