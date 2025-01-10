package comp3350.reshop.logic.enums;

import javax.annotation.Nonnull;

public enum Type {
    Shoes("Shoes"), TShirt("T-shirt"), Pants("Pants"), Sandals("Sandals"), Hoodie("Hoodie"), Jacket("Jacket"), Pyjamas("Pyjamas"), CropTop("Crop Top"), PoloShirts("Polo Shirts"), Shorts("Shorts");
     private final String type;

    Type(final String type) {
        this.type = type;
    }

    @Nonnull
    @Override public String toString() {
        return type;
    }
}


