package ru.bogdanov.diplom.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import ru.bogdanov.diplom.ui.layout.size.Left;
import ru.bogdanov.diplom.ui.layout.size.Right;
import ru.bogdanov.diplom.ui.util.FontSize;
import ru.bogdanov.diplom.ui.util.LumoStyles;
import ru.bogdanov.diplom.ui.util.TextColor;
import ru.bogdanov.diplom.ui.util.UIUtils;
import ru.bogdanov.diplom.ui.util.css.BorderRadius;
import ru.bogdanov.diplom.ui.util.css.Display;

public class Token extends FlexBoxLayout {

    private final String CLASS_NAME = "token";

    public Token(String text) {
        setAlignItems(Alignment.CENTER);
        setBackgroundColor(LumoStyles.Color.Primary._10);
        setBorderRadius(BorderRadius.M);
        setClassName(CLASS_NAME);
        setDisplay(Display.INLINE_FLEX);
        setPadding(Left.S, Right.XS);
        setSpacing(Right.XS);

        Label label = UIUtils.createLabel(FontSize.S, TextColor.BODY, text);
        Button button = UIUtils.createButton(VaadinIcon.CLOSE_SMALL, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE);
        add(label, button);
    }

}
