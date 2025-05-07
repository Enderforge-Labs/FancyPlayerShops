package com.snek.framework.data_types.ui;








public enum TextAlignment {
    LEFT("left"),
    CENTER("center"),
    RIGHT("right");

    private final String text;

    TextAlignment(String text) {
        this.text = text;
    }

    public String asString() {
        return text;
    }
}
