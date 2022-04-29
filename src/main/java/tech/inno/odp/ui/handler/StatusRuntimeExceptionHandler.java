package tech.inno.odp.ui.handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.utils.NotificationUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Tag(Tag.DIV)
@ParentLayout(MainLayout.class)
public class StatusRuntimeExceptionHandler extends Component
        implements HasErrorParameter<StatusRuntimeException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<StatusRuntimeException> parameter) {
        log.error("ExternalServerError: at :" + LocalDateTime.now() + " "
                + parameter.getCaughtException().getCause()
                + " The message: " + parameter.getCaughtException().getMessage());
        UI.getCurrent().access(() -> NotificationUtils.showNotificationOnExternalServerError(event));
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}