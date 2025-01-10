package comp3350.reshop.data;

import java.util.List;

import comp3350.reshop.objects.ClothingItem;
public interface ClothingItemData {
    void addItem(ClothingItem clothing);
    ClothingItem getByItemID(int id);
    List<ClothingItem> getBySearchTerm(String searchTerm);
    List<ClothingItem> getBySeller(String name);
    List<ClothingItem> getByBuyer(String name);
    List<ClothingItem> getItemsWithoutBuyer();
    void setBuyer(String buyerName, int itemID);
}
