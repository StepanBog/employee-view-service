package ru.bogdanov.diplom.ui.handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(EmployeeMainLayout.class)
public class CustomRouteNotFoundHandler extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {
        UI.getCurrent().access(() -> Notification.show("URL с сегментом пути: \"" + event.getLocation().getPath() + "\" не существует", 5000 , Notification.Position.TOP_CENTER));
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
