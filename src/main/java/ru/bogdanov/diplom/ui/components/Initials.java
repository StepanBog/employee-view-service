package ru.bogdanov.diplom.ui.components;

import ru.bogdanov.diplom.ui.util.FontSize;
import ru.bogdanov.diplom.ui.util.FontWeight;
import ru.bogdanov.diplom.ui.util.LumoStyles;
import ru.bogdanov.diplom.ui.util.UIUtils;
import ru.bogdanov.diplom.ui.util.css.BorderRadius;

public class Initials extends FlexBoxLayout {

    private String CLASS_NAME = "initials";

    public Initials(String initials) {
        setAlignItems(Alignment.CENTER);
        setBackgroundColor(LumoStyles.Color.Contrast._10);
        setBorderRadius(BorderRadius.L);
        setClassName(CLASS_NAME);
        UIUtils.setFontSize(FontSize.S, this);
        UIUtils.setFontWeight(FontWeight._600, this);
        setHeight(LumoStyles.Size.M);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth(LumoStyles.Size.M);

        add(initials);
    }

}
