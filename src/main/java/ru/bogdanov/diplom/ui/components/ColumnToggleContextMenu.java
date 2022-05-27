package ru.bogdanov.diplom.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;

/**
 * @author SBogdanov
 */
public class ColumnToggleContextMenu extends ContextMenu {

    public ColumnToggleContextMenu(Component target) {
        super(target);
        setOpenOnClick(true);
    }

    public void addColumnToggleItem(String label, Grid.Column<?> column) {
        MenuItem menuItem = this.addItem(label, e -> {
            column.setVisible(e.getSource().isChecked());
        });
        menuItem.setCheckable(true);
        menuItem.setChecked(column.isVisible());
    }
}
