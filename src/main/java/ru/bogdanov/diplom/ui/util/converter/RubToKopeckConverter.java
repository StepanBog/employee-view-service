package ru.bogdanov.diplom.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RubToKopeckConverter implements Converter<BigDecimal, BigDecimal> {

    @Override
    public Result<BigDecimal> convertToModel(BigDecimal value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
        return Result.ok(value.movePointRight(2));
    }

    @Override
    public BigDecimal convertToPresentation(BigDecimal value, ValueContext context) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.movePointLeft(2).setScale(0, RoundingMode.HALF_UP);
    }
}
