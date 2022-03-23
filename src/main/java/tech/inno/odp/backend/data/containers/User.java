package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    private String id;

    private String username;

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
     * id работодателя
     */
    private UUID employerId;

    /**
     * id работника
     */
    private UUID employeeId;
}
