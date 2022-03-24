package tech.inno.odp.backend.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

@Getter
@Setter
public class RefreshedPreAuthenticatedAuthenticationToken extends PreAuthenticatedAuthenticationToken {

    private String accessToken;
    private String refreshedAccessToken;

    public RefreshedPreAuthenticatedAuthenticationToken(
            Object aPrincipal,
            Object aCredentials,
            Collection<? extends GrantedAuthority> anAuthorities,
            String accessToken,
            String refreshedAccessToken
    ) {
        super(aPrincipal, aCredentials, anAuthorities);
        this.setRefreshedAccessToken(refreshedAccessToken);
        this.setAccessToken(accessToken);
    }

    public void setRefreshedAccessToken(String refreshedAccessToken) {
        this.refreshedAccessToken = refreshedAccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
