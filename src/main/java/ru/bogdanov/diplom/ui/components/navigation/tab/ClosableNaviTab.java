package ru.bogdanov.diplom.ui.components.navigation.tab;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import ru.bogdanov.diplom.ui.util.FontSize;
import ru.bogdanov.diplom.ui.util.UIUtils;

public class ClosableNaviTab extends NaviTab {

    private final Button close;

    public ClosableNaviTab(String label,
                           Class<? extends Component> navigationTarget) {
        super(label, navigationTarget);
        getElement().setAttribute("closable", true);

        close = UIUtils.createButton(VaadinIcon.CLOSE, ButtonVariant.LUMO_TERTIARY_INLINE);
        UIUtils.setFontSize(FontSize.XXS, close);
        add(close);
    }

    public Button getCloseButton() {
        return close;
    }
}
