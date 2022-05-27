package ru.bogdanov.diplom.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.BeforeEnterEvent;

public class NotificationUtils {

    public static void showNotificationOnSave() {
//        Notification.show("Данные пользователя сохранены", 5000 , Notification.Position.TOP_CENTER);
        Notification notification = new Notification("Данные пользователя сохранены", 5000 , Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    public static void showNotificationOnSaveError() {
        Notification notification = new Notification("Не удалось сохранить данные пользователя", 5000 , Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public static void showNotificationOnExternalServerError(BeforeEnterEvent event) {
        Notification notification = new Notification("Случилась неизвестная ошибка, данные не удалось получить по адресу: " + event.getLocation().getPath(), 5000 , Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public static void showNotificationOnNoSuchPath(BeforeEnterEvent event) {
        Notification notification = new Notification("URL с сегментом пути: \"" + event.getLocation().getPath() + "\" не существует", 5000 , Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }
}
