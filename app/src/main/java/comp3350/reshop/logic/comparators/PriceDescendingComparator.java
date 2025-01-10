package comp3350.reshop.logic.comparators;

import java.util.Comparator;

import comp3350.reshop.objects.ClothingItem;

public class PriceDescendingComparator implements Comparator<ClothingItem> {
    @Override
    public int compare(ClothingItem item1, ClothingItem item2){
        return item2.getPrice() - item1.getPrice();
    }
}
