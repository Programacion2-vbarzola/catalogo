package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.GetUserByLoginUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByLoginUseCaseImpl implements GetUserByLoginUseCase {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }
}
