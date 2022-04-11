package tech.inno.odp.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

/**
 * @author VKozlov
 */
public class SplitLayoutToggle extends SplitLayout {

    private final Component primaryComponent;
    private final Component secondaryComponent;

    public SplitLayoutToggle(Component primaryComponent, Component secondaryComponent) {
        this.primaryComponent = primaryComponent;
        this.secondaryComponent = secondaryComponent;

        setSizeFull();

        addToPrimary(primaryComponent);
        addToSecondary(secondaryComponent);

        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        setSplitterPosition(20);
    }
}
