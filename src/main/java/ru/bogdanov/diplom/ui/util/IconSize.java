package ru.bogdanov.diplom.ui.util;

public enum IconSize {

    XS("size-xs"),
    S("size-s"),
    M("size-m"),
    L("size-l");

    private String style;

    IconSize(String style) {
        this.style = style;
    }

    public String getClassName() {
        return style;
    }

}
