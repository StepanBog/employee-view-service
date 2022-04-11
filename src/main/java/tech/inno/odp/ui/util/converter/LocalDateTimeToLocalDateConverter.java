package tech.inno.odp.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author VKozlov
 */
public class LocalDateTimeToLocalDateConverter implements Converter<LocalDateTime, LocalDate> {

    @Override
    public Result<LocalDate> convertToModel(LocalDateTime value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
        return Result.ok(value.toLocalDate());
    }

    @Override
    public LocalDateTime convertToPresentation(LocalDate value, ValueContext context) {
        if (value == null) {
            return null;
        }
        return value.atStartOfDay();
    }
}
