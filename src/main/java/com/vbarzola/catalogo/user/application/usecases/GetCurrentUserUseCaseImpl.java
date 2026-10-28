package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.security.SecurityUtils;
import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.GetCurrentUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }
}
