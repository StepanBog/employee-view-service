package tech.inno.odp.backend.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import tech.inno.odp.backend.data.containers.Auth;
import tech.inno.odp.backend.service.IAuthService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final IAuthService authService;

    public RefreshedPreAuthenticatedAuthenticationToken login(UsernamePasswordAuthenticationToken token) {

        if (token.getCredentials() == null) {
            log.debug("No authentication credentials found in request.");
            throw new BadCredentialsException("No authentication credentials found in request.");
        }

        String authToken = "Basic " + new String(
                Base64.getEncoder().encode((token.getPrincipal() + ":" + token.getCredentials())
                        .getBytes(StandardCharsets.UTF_8)
                ), StandardCharsets.UTF_8
        );
        Auth auth = authService.auth(authToken);

        String accessToken = auth.getAccessToken().trim();
        String refreshToken = auth.getRefreshToken().trim();

        return new RefreshedPreAuthenticatedAuthenticationToken(
                auth.getUser(),
                accessToken,
                auth.getUser().getAuthorities(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        if (authentication.getCredentials() == null) {
            log.debug("No authentication credentials found in request.");
            throw new BadCredentialsException("No authentication credentials found in request.");
        }

        String accessToken = authentication.getCredentials().toString().trim();
        Auth auth = authService.validateToken(accessToken);

        String refreshToken = auth.getRefreshToken().trim();

        return new RefreshedPreAuthenticatedAuthenticationToken(
                auth.getUser(),
                authentication.getCredentials(),
                auth.getUser().getAuthorities(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
