package ru.bogdanov.diplom.backend.service;


import ru.bogdanov.diplom.backend.data.containers.Auth;

/**
 * @author SBogdanov
 * Сервис для работы с методами аунтефикации
 */
public interface IAuthService {

    /**
     * Получить сервисный токен для подключения к API ODP
     *
     * @return сервисный токен
     */
    Auth auth(String authHeader);

    /**
     * Проверка валидности переданного токена
     *
     * @param accessToken - переданный токен
     * @return - данные о владельце токена
     */
    Auth validateToken(String accessToken);

    /**
     * Выход пользователя из приложения
     */
    void logout();
}
