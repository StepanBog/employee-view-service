package tech.inno.odp.ui.components.detailsdrawer;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.shared.Registration;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Right;
import tech.inno.odp.ui.layout.size.Vertical;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;

public class DetailsDrawerFooter extends FlexBoxLayout {

    private Button save;
    private Button cancel;

    public DetailsDrawerFooter() {
        setBackgroundColor(LumoStyles.Color.Contrast._5);
        setPadding(Horizontal.RESPONSIVE_L, Vertical.S);
        setSpacing(Right.S);
        setWidthFull();

        save = UIUtils.createPrimaryButton("Save");
        cancel = UIUtils.createTertiaryButton("Cancel");
        add(save, cancel);
    }

    public Registration addSaveListener(
            ComponentEventListener<ClickEvent<Button>> listener) {
        return save.addClickListener(listener);
    }

    public Registration addCancelListener(
            ComponentEventListener<ClickEvent<Button>> listener) {
        return cancel.addClickListener(listener);
    }

}
