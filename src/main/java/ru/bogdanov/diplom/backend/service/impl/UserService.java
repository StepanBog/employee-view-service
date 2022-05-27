package ru.bogdanov.diplom.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.bogdanov.diplom.backend.data.containers.User;
import ru.bogdanov.diplom.backend.mapper.UserMapper;
import ru.bogdanov.diplom.backend.service.IUserService;
import ru.bogdanov.diplom.grpc.generated.auth.user.OneUserRequest;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserSearchRequest;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserServiceGrpc;
import ru.bogdanov.diplom.grpc.generated.auth.user.UsersResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author SBogdanov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @GrpcClient("auth-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceClient;

    private final UserMapper userMapper;

    @Override
    public UsersResponse find(final @NotNull UserSearchRequest request) {
        return userServiceClient.find(request);
    }

    public List<User> findAll(@NotNull UserSearchRequest request) {
        return userMapper.transform(find(request).getUsersList());
    }

    @Override
    public User findById(@NotNull UUID userId) {
        return userMapper.transform(
                userServiceClient.findOne(
                        OneUserRequest.newBuilder()
                                .setUserId(userId.toString())
                                .build()
                )
        );
    }

    @Override
    public List<User> find(Query<User, User> query, int pageSize) {
        UserSearchRequest request = userMapper.transformToSearch(
                query.getFilter().orElse(null),
                query.getOffset() == 0 ? 0 : query.getOffset() / pageSize,
                pageSize
        );
        log.info("Find users request - {}", request);
        UsersResponse response = find(request);

        log.info("Find users result - {}", response.getUsersCount());
        return userMapper.transform(response.getUsersList());
    }

    @Override
    public User save(@NotNull User employer) {
        return userMapper.transform(
                userServiceClient.save(
                        userMapper.transform(employer)
                )
        );
    }

    @Override
    public int getTotalCount(Query<User, User> query) {
        UserSearchRequest request = userMapper.transformToSearch(
                query.getFilter().orElse(null),
                0,
                5
        );
        log.info("Get total users by request - {}", request);
        int result = (int) find(request).getTotalSize();
        log.info("Total users by request - {}; result - {}", request, result);
        return result;
    }
}
