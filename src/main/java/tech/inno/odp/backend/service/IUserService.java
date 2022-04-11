package tech.inno.odp.backend.service;


import com.vaadin.flow.data.provider.Query;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.grpc.generated.auth.user.UserSearchRequest;
import tech.inno.odp.grpc.generated.auth.user.UsersResponse;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author VKozlov
 * Сервис для работы с методами пользователя
 */
public interface IUserService {

    /**
     * Найти пользователей
     *
     * @param request - критерии поиска
     * @return - список пользователей
     */
    UsersResponse find(final @NotNull UserSearchRequest request);

    /**
     * Найти пользователей
     *
     * @param query    - критерии поиска
     * @param pageSize - количество элементов выводимых на 1й странице
     * @return - список пользователей
     */
    List<User> find(Query<User, User> query, int pageSize);

    /**
     * Сохранить пользователя
     *
     * @param user - сущность пользователя
     */
    User save(final @NotNull User user);

    /**
     * Получить общее количество элементов
     *
     * @return общее количество
     */
    int getTotalCount(Query<User, User> query);
}
