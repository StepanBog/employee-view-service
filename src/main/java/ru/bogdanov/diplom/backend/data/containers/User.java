package ru.bogdanov.diplom.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bogdanov.diplom.backend.data.enums.UserRoleName;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author SBogdanov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    private String id;

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Пароль
     */
    private String password;

    /**
     * Поле не используется, пришло из UserDetails
     */
    private boolean accountNonExpired;

    /**
     * Поле не используется, пришло из UserDetails
     */
    private boolean accountNonLocked;

    /**
     * Поле не используется, пришло из UserDetails
     */
    private boolean credentialsNonExpired;

    /**
     * Флаг доступности пользователя
     */
    private boolean enabled;

    /**
     * Права пользователя
     */
    private List<SimpleGrantedAuthority> authorities;

    /**
     * Имя роли
     */
    private UserRoleName roleName;

    /**
     * id работодателя
     */
    private String employerId;

    /**
     * id работника
     */
    private String employeeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
