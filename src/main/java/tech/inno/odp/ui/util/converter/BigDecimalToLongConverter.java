package tech.inno.odp.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.math.BigDecimal;

/**
 * @author VKozlov
 */
public class BigDecimalToLongConverter implements Converter<BigDecimal, Long> {

    @Override
    public Result<Long> convertToModel(BigDecimal value, ValueContext context) {
        return Result.ok(value.longValue());
    }

    @Override
    public BigDecimal convertToPresentation(Long value, ValueContext context) {
        return BigDecimal.valueOf(value);
    }
}
