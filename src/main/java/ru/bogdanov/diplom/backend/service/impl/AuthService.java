package ru.bogdanov.diplom.backend.service.impl;

import com.google.protobuf.Empty;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.bogdanov.diplom.backend.config.WebSecurityConfig;
import ru.bogdanov.diplom.backend.data.containers.Auth;
import ru.bogdanov.diplom.backend.mapper.AuthMapper;
import ru.bogdanov.diplom.backend.service.IAuthService;
import ru.bogdanov.diplom.grpc.generated.auth.AuthRequest;
import ru.bogdanov.diplom.grpc.generated.auth.AuthResponse;
import ru.bogdanov.diplom.grpc.generated.auth.AuthServiceGrpc;
import ru.bogdanov.diplom.grpc.generated.auth.model.UserRole;
import ru.bogdanov.diplom.grpc.generated.auth.token.TokenResponse;
import ru.bogdanov.diplom.grpc.generated.auth.token.TokenServiceGrpc;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserServiceGrpc;

import javax.validation.constraints.NotNull;

/**
 * @author SBogdanov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceClient;
    @GrpcClient("auth-service")
    private TokenServiceGrpc.TokenServiceBlockingStub tokenServiceClient;
    @GrpcClient("auth-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceClient;

    private final AuthMapper authMapper;

    @Override
    public Auth auth(@NotNull final String authHeader) {
        Metadata header = new Metadata();
        Metadata.Key<String> key = Metadata.Key.of(HttpHeaders.AUTHORIZATION, Metadata.ASCII_STRING_MARSHALLER);
        header.put(key, authHeader);

        AuthResponse authResponse = authServiceClient.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(header))
                .auth(AuthRequest.newBuilder()
                        .setRole(UserRole.ROLE_EMPLOYEE)
                        .build());
        return authMapper.transform(authResponse);
    }

    @Override
    public Auth validateToken(String accessToken) {
        Metadata header = new Metadata();
        Metadata.Key<String> key = Metadata.Key.of(HttpHeaders.AUTHORIZATION, Metadata.ASCII_STRING_MARSHALLER);
        header.put(key, accessToken);

        TokenResponse tokenResponse =
                tokenServiceClient.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(header))
                        .validateToken(Empty.getDefaultInstance());

        return authMapper.transform(tokenResponse);
    }

    @Override
    public void logout() {
        UI.getCurrent().getPage().setLocation(WebSecurityConfig.LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(),
                null,
                null);
    }
}
