package comp3350.reshop.logic.comparators;

import java.util.Comparator;
import comp3350.reshop.logic.enums.Quality;

import comp3350.reshop.objects.ClothingItem;

public class QualityDescendingComparator implements Comparator<ClothingItem> {
    @Override
    public int compare(ClothingItem item1, ClothingItem item2) {
        Quality quality1 = Quality.valueOfLabel(  item1.getQuality() );
        Quality quality2 = Quality.valueOfLabel(  item2.getQuality() );

        if(quality1 != null)
            return quality1.compareTo(quality2);
        else
            return -1;
    }

}
