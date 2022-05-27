package ru.bogdanov.diplom.backend.service;


import com.vaadin.flow.data.provider.Query;
import ru.bogdanov.diplom.backend.data.containers.User;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserSearchRequest;
import ru.bogdanov.diplom.grpc.generated.auth.user.UsersResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author SBogdanov
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
     * Найти пользователя по id
     *
     * @param userId - id пользователя
     * @return -  пользователь
     */
    User findById(final @NotNull UUID userId);

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
