package comp3350.reshop.logic.enums;

import javax.annotation.Nonnull;

public enum Quality {
    NewWithTags("New with Tags"), NewWithoutTags("New without Tags"), LikeNew("Like New"), UsedExcellent("Used - Excellent"), UsedGood("Used - Good"), UsedFair("Used - Fair");
    private final String quality;


    Quality(final String quality) {
        this.quality = quality;
    }

    @Nonnull
    @Override public String toString() {
        return quality;
    }

    public static Quality valueOfLabel(String label) {
        for (Quality q : values()){
            if(q.quality.equals(label)){
                return q;
            }
        }
        return null;
    }
}
