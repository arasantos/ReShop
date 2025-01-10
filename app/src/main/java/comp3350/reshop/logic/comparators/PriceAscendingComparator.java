package comp3350.reshop.logic.comparators;

import java.util.Comparator;

import comp3350.reshop.objects.ClothingItem;

public class PriceAscendingComparator implements Comparator<ClothingItem> {
    @Override
    public int compare(ClothingItem item1, ClothingItem item2){
        return item1.getPrice() - item2.getPrice();
    }
}
