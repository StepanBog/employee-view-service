package tech.inno.odp.ui.util.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author VKozlov
 */
public class StringToStringWithNullValueConverter implements Converter<String, String> {

    @Override
    public Result<String> convertToModel(String text, ValueContext context) {
        return Result.ok(StringUtils.isEmpty(text) ? null : text);
    }

    @Override
    public String convertToPresentation(String text, ValueContext context) {
        return StringUtils.isEmpty(text) ? null : text;
    }
}
