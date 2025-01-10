package comp3350.reshop.logic.enums;

import javax.annotation.Nonnull;

public enum Location {
    Brandon("Brandon"), Selkirk("Selkirk"), Steinbach("Steinbach"), Winkler("Winkler"), Winnipeg("Winnipeg"), FlinFlon("Flin Flon");

    private final String location;

    Location(final String location) {
        this.location = location;
    }

    @Nonnull
    @Override public String toString() {
        return location;
    }
}
