package ru.bogdanov.diplom.ui.components.navigation.bar;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.bogdanov.diplom.backend.service.impl.AuthService;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;
import ru.bogdanov.diplom.ui.components.FlexBoxLayout;
import ru.bogdanov.diplom.ui.components.navigation.tab.NaviTab;
import ru.bogdanov.diplom.ui.components.navigation.tab.NaviTabs;
import ru.bogdanov.diplom.ui.util.LumoStyles;
import ru.bogdanov.diplom.ui.util.UIUtils;
import ru.bogdanov.diplom.ui.views.employee.EmployeeHome;

import java.util.ArrayList;

@CssImport("./styles/components/app-bar.css")
public class AppBar extends FlexBoxLayout {

    private String CLASS_NAME = "app-bar";

    private FlexBoxLayout container;

    private Button menuIcon;
    private Button contextIcon;

    private H4 title;
    private FlexBoxLayout actionItems;
    private Avatar avatar;

    private FlexBoxLayout tabContainer;
    @Getter
    private NaviTabs tabs;
    private ArrayList<Registration> tabSelectionListeners;
    private Button addTab;

    private Registration searchRegistration;

    public enum NaviMode {
        MENU, CONTEXTUAL
    }

    public AppBar(String title, NaviTab... tabs) {
        setClassName(CLASS_NAME);

        initMenuIcon();
        initContextIcon();
        initTitle(title);
        initAvatar();
        initActionItems();
        initContainer();
        initTabs(tabs);
    }

    public void setNaviMode(NaviMode mode) {
        if (mode.equals(NaviMode.MENU)) {
            menuIcon.setVisible(true);
            contextIcon.setVisible(false);
        } else {
            menuIcon.setVisible(false);
            contextIcon.setVisible(true);
        }
    }

    private void initMenuIcon() {
        menuIcon = UIUtils.createTertiaryInlineButton(VaadinIcon.MENU);
        menuIcon.addClassName(CLASS_NAME + "__navi-icon");
        menuIcon.addClickListener(e -> EmployeeMainLayout.get().getNaviDrawer().toggle());
        UIUtils.setAriaLabel("Menu", menuIcon);
        UIUtils.setLineHeight("1", menuIcon);
    }

    private void initContextIcon() {
        contextIcon = UIUtils
                .createTertiaryInlineButton(VaadinIcon.ARROW_LEFT);
        contextIcon.addClassNames(CLASS_NAME + "__context-icon");
        contextIcon.setVisible(false);
        UIUtils.setAriaLabel("Back", contextIcon);
        UIUtils.setLineHeight("1", contextIcon);
    }

    private void initTitle(String title) {
        this.title = new H4(title);
        this.title.setClassName(CLASS_NAME + "__title");
    }

    private void initAvatar() {
        avatar = new Avatar();
        avatar.setClassName(CLASS_NAME + "__avatar");

        ContextMenu contextMenu = new ContextMenu(avatar);
        contextMenu.setOpenOnClick(true);
        contextMenu.addItem("??????????",
                e -> new AuthService(null).logout()
        );
    }

    private void initActionItems() {
        actionItems = new FlexBoxLayout();
        actionItems.addClassName(CLASS_NAME + "__action-items");
        actionItems.setVisible(false);
    }

    private void initContainer() {
        container = new FlexBoxLayout(menuIcon, contextIcon, this.title,
                actionItems, avatar);
        container.addClassName(CLASS_NAME + "__container");
        container.setAlignItems(Alignment.CENTER);
        add(container);
    }

    private void initTabs(NaviTab... tabs) {
        addTab = UIUtils.createSmallButton(VaadinIcon.PLUS);
        addTab.addClickListener(e -> this.tabs
                .setSelectedTab(addClosableNaviTab("New Tab", EmployeeHome.class)));
        addTab.setVisible(false);

        this.tabs = tabs.length > 0 ? new NaviTabs(tabs) : new NaviTabs();
        this.tabs.setClassName(CLASS_NAME + "__tabs");
        this.tabs.setVisible(false);
        for (NaviTab tab : tabs) {
            configureTab(tab);
        }

        this.tabSelectionListeners = new ArrayList<>();

        tabContainer = new FlexBoxLayout(this.tabs, addTab);
        tabContainer.addClassName(CLASS_NAME + "__tab-container");
        tabContainer.setAlignItems(Alignment.CENTER);
        add(tabContainer);
    }

    /* === MENU ICON === */

    public Button getMenuIcon() {
        return menuIcon;
    }

    /* === CONTEXT ICON === */

    public Button getContextIcon() {
        return contextIcon;
    }

    public void setContextIcon(Icon icon) {
        contextIcon.setIcon(icon);
    }

    /* === TITLE === */

    public String getTitle() {
        return this.title.getText();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    /* === ACTION ITEMS === */

    public Component addActionItem(Component component) {
        actionItems.add(component);
        updateActionItemsVisibility();
        return component;
    }

    public Button addActionItem(VaadinIcon icon) {
        Button button = UIUtils.createButton(icon, ButtonVariant.LUMO_SMALL,
                ButtonVariant.LUMO_TERTIARY);
        addActionItem(button);
        return button;
    }

    public void removeAllActionItems() {
        actionItems.removeAll();
        updateActionItemsVisibility();
    }

    /* === AVATAR == */

    public Avatar getAvatar() {
        return avatar;
    }

    /* === TABS === */

    public void centerTabs() {
        tabs.addClassName(LumoStyles.Margin.Horizontal.AUTO);
    }

    private void configureTab(Tab tab) {
        tab.addClassName(CLASS_NAME + "__tab");
        updateTabsVisibility();
    }

    public Tab addTab(String text) {
        Tab tab = tabs.addTab(text);
        configureTab(tab);
        return tab;
    }

    public Tab addTab(Tab tab) {
        tabs.add(tab);
        configureTab(tab);
        return tab;
    }

    public Tab addTab(String text,
                      Class<? extends Component> navigationTarget) {
        Tab tab = tabs.addTab(text, navigationTarget);
        configureTab(tab);
        return tab;
    }

    public Tab addClosableNaviTab(String text,
                                  Class<? extends Component> navigationTarget) {
        Tab tab = tabs.addClosableTab(text, navigationTarget);
        configureTab(tab);
        return tab;
    }

    public Tab getSelectedTab() {
        return tabs.getSelectedTab();
    }

    public void setSelectedTab(Tab selectedTab) {
        tabs.setSelectedTab(selectedTab);
    }

    public void updateSelectedTab(String text,
                                  Class<? extends Component> navigationTarget) {
        tabs.updateSelectedTab(text, navigationTarget);
    }

    public void navigateToSelectedTab() {
        tabs.navigateToSelectedTab();
    }

    public void addTabSelectionListener(
            ComponentEventListener<Tabs.SelectedChangeEvent> listener) {
        Registration registration = tabs.addSelectedChangeListener(listener);
        tabSelectionListeners.add(registration);
    }

    public int getTabCount() {
        return tabs.getTabCount();
    }

    public void removeAllTabs() {
        tabSelectionListeners.forEach(registration -> registration.remove());
        tabSelectionListeners.clear();
        tabs.removeAll();
        updateTabsVisibility();
    }

    /* === ADD TAB BUTTON === */

    public void setAddTabVisible(boolean visible) {
        addTab.setVisible(visible);
    }

    /* === SEARCH === */

    public void searchModeOn() {
        menuIcon.setVisible(false);
        title.setVisible(false);
        actionItems.setVisible(false);
        tabContainer.setVisible(false);

        contextIcon.setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
        contextIcon.setVisible(true);
        searchRegistration = contextIcon
                .addClickListener(e -> searchModeOff());
    }

    private void searchModeOff() {
        menuIcon.setVisible(true);
        title.setVisible(true);
        tabContainer.setVisible(true);

        updateActionItemsVisibility();
        updateTabsVisibility();

        contextIcon.setVisible(false);
        searchRegistration.remove();
    }

    /* === RESET === */

    public void reset() {
        title.setText("");
        setNaviMode(NaviMode.MENU);
        removeAllActionItems();
        removeAllTabs();
    }

    /* === UPDATE VISIBILITY === */

    private void updateActionItemsVisibility() {
        actionItems.setVisible(actionItems.getComponentCount() > 0);
    }

    private void updateTabsVisibility() {
        tabs.setVisible(tabs.getComponentCount() > 0);
    }
}
