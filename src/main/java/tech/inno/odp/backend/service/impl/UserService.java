package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.mapper.UserMapper;
import tech.inno.odp.backend.service.IUserService;
import tech.inno.odp.grpc.generated.auth.user.UserSearchRequest;
import tech.inno.odp.grpc.generated.auth.user.UserServiceGrpc;
import tech.inno.odp.grpc.generated.auth.user.UsersResponse;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author VKozlov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @GrpcClient("odp-auth")
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
