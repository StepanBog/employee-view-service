package ru.bogdanov.diplom.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.math.BigDecimal;

/**
 * @author SBogdanov
 */
public class BigDecimalToLongConverter implements Converter<BigDecimal, Long> {

    @Override
    public Result<Long> convertToModel(BigDecimal value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
        return Result.ok(value.longValue());
    }

    @Override
    public BigDecimal convertToPresentation(Long value, ValueContext context) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value);
    }
}
