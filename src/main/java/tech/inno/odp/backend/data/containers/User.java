package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.inno.odp.backend.data.enums.UserRoleName;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author VKozlov
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

    private boolean accountNonExpired;

    private boolean accountNonLocked;

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
     * Имена ролей
     */
    private List<UserRoleName> roleNames;

    /**
     * id работодателя
     */
    private String employerId;

    /**
     * id работника
     */
    private String employeeId;

    /**
     * Настройки пользователей
     */
    private UserSettings settings;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
