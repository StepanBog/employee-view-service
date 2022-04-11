package tech.inno.odp.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author VKozlov
 */
public class LocalDateToLocalDateTimeConverter implements Converter<LocalDate, LocalDateTime> {

    @Override
    public Result<LocalDateTime> convertToModel(LocalDate value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
        return Result.ok(value.atStartOfDay());
    }

    @Override
    public LocalDate convertToPresentation(LocalDateTime value, ValueContext context) {
        if (value == null) {
            return null;
        }
        return value.toLocalDate();
    }
}
