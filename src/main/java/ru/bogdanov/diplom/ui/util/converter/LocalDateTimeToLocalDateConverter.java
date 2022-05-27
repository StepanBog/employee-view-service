package ru.bogdanov.diplom.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author SBogdanov
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
