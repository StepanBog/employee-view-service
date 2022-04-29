package tech.inno.odp.ui.handler;

import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
import tech.inno.odp.utils.NotificationUtils;

@Slf4j
public class SaveButtonErrorHandler implements ErrorHandler {
    @Override
    public void error(ErrorEvent errorEvent) {
        log.error(errorEvent.getThrowable().fillInStackTrace().getCause() + " " + errorEvent.getThrowable().getMessage());
        NotificationUtils.showNotificationOnSaveError();
    }
}
