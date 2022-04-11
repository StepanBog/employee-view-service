package tech.inno.odp.ui.components.field;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.Converter;

/**
 * @author VKozlov
 */
public class CustomTextField extends TextField {

    private Converter<String, String>[] converters;

    public CustomTextField(String label) {
        super(label);
    }

    public CustomTextField() {
        super();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    @Override
    public String getValue() {
        String value = super.getValue();
        for (Converter<String, String> converter : converters) {
            value = converter.convertToPresentation(value, null);
        }
        return value;
    }

    public void setConverters(Converter<String, String>... converters) {
        this.converters = converters;
    }
}
