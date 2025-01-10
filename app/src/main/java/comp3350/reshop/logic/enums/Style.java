package comp3350.reshop.logic.enums;

import javax.annotation.Nonnull;

public enum Style {
    Vintage("Vintage"), Streetwear("Streetwear"), Minimalist("Minimalist"), Athleisure("Athleisure"), Preppy("Preppy");
    private final String style;

    Style(final String style) {
        this.style = style;
    }

    @Nonnull
    @Override public String toString() {
        return style;
    }
}
