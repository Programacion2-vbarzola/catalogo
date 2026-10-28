package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.GetAllPublicUsersUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllPublicUsersUseCaseImpl implements GetAllPublicUsersUseCase {

    private final UserRepository userRepository;

    @Override
    public Page<User> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable);
    }
}
