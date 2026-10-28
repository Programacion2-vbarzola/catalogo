package com.vbarzola.catalogo.user.application.service;

import com.vbarzola.catalogo.user.application.dto.AdminUserDTO;
import com.vbarzola.catalogo.user.application.dto.UserDTO;
import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.ActivateUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.ChangePasswordUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.CompletePasswordResetUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.CreateUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.DeleteUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.GetAllPublicUsersUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.GetAllUsersUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.GetCurrentUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.GetUserByLoginUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.RegisterUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.RemoveNotActivatedUsersUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.RequestPasswordResetUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.UpdateCurrentUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.in.UpdateUserUseCase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service class for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final RegisterUserUseCase registerUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final CompletePasswordResetUseCase completePasswordResetUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UpdateCurrentUserUseCase updateCurrentUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserByLoginUseCase getUserByLoginUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetAllPublicUsersUseCase getAllPublicUsersUseCase;
    private final RemoveNotActivatedUsersUseCase removeNotActivatedUsersUseCase;

    public Optional<User> activateRegistration(String key) {
        return activateUserUseCase.activateUser(key);
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        return completePasswordResetUseCase.completePasswordReset(newPassword, key);
    }

    public Optional<User> requestPasswordReset(String mail) {
        return requestPasswordResetUseCase.requestPasswordReset(mail);
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        User user = toDomain(userDTO);
        return registerUserUseCase.registerUser(user, password);
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = toDomain(userDTO);
        return createUserUseCase.createUser(user);
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        User user = toDomain(userDTO);
        user.setId(userDTO.getId());
        return updateUserUseCase.updateUser(user).map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        deleteUserUseCase.deleteUser(login);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        updateCurrentUserUseCase.updateCurrentUser(firstName, lastName, email, langKey, imageUrl);
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        changePasswordUseCase.changePassword(currentClearTextPassword, newPassword);
    }

    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return getAllUsersUseCase.getAllUsers(pageable).map(AdminUserDTO::new);
    }

    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return getAllPublicUsersUseCase.getAllPublicUsers(pageable).map(UserDTO::new);
    }

    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return getUserByLoginUseCase.getUserByLogin(login);
    }

    public Optional<User> getUserWithAuthorities() {
        return getCurrentUserUseCase.getCurrentUser();
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired every day, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        removeNotActivatedUsersUseCase.removeNotActivatedUsers();
    }

    private User toDomain(AdminUserDTO userDTO) {
        return User.builder()
            .login(userDTO.getLogin())
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .email(userDTO.getEmail())
            .imageUrl(userDTO.getImageUrl())
            .langKey(userDTO.getLangKey())
            .activated(userDTO.isActivated())
            .authorities(userDTO.getAuthorities())
            .build();
    }
}
