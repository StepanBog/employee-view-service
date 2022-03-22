package tech.inno.odp.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author VKozlov
 */
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
        if (StringUtils.isEmpty(value)) {
            return Result.ok(null);
        }
        return Result.ok(LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss")));
    }

    @Override
    public String convertToPresentation(LocalDateTime value, ValueContext context) {
        if (value == null) {
            return "";
        }
        return value.format(DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss"));
    }
}
