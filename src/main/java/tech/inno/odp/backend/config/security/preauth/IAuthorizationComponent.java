package tech.inno.odp.backend.config.security.preauth;

import javax.annotation.Nonnull;

public interface IAuthorizationComponent {

    boolean EmployeePathIdEqualsTokenCheck(@Nonnull String id);
}
