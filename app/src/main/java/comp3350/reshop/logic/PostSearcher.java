package comp3350.reshop.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.objects.ClothingItem;

public class PostSearcher {
    private final ClothingItemAccessor accessor;
    public PostSearcher() {
        accessor = new ClothingItemAccessor();
    }

    public PostSearcher(ClothingItemData clothingItemData) {
        accessor = new ClothingItemAccessor(clothingItemData);
    }

    /**
     * Get all clothing items that match tokens in the given `search` argument.
     * @param search the search query
     * @return a list of `ClothingItems`
     */
    public List<ClothingItem> getItemsFromSearch(String search) {
        Map<Integer, ClothingItem> resultSet = new HashMap<>();
        String[] searchTerms = search.split(" ");

        for(String term : searchTerms){
            for (ClothingItem item : accessor.getItemsBySearchTerm(term)) {
                if (!resultSet.containsKey(item.getItemID())) {
                    resultSet.put(item.getItemID(), item);
                }
            }
        }

        return new ArrayList<>(resultSet.values());
    }

    /**
     * Sort the given `items` list using ordering defined by the `comparator` argument.
     * @param items a list of `ClothingItems` to sort on
     * @param comparator a comparator with some ordering on `ClothingItems`
     */
    public void sort(List<ClothingItem> items, Comparator<ClothingItem> comparator) {
        items.sort(comparator);
    }

    /**
     * Filter a collection of `ClothingItem`s on a collection of filter types. If `filterTypes` is
     * empty, return the list unchanged.
     * @param items the list of clothing items to filter on
     * @param filterTypes the list of clothing types to filter by
     * @return a list of `ClothingItem`s
     */
    public List<ClothingItem> getItemsFilteredType(Collection<ClothingItem> items, Collection<String> filterTypes){
        List<ClothingItem> result;

        if (!filterTypes.isEmpty()) {
            result = new ArrayList<>();

            for (ClothingItem item : items) {
                if (filterTypes.contains(item.getType())) {
                    result.add(item);
                }
            }
        } else {
            result = new ArrayList<>(items);
        }
        return result;
    }

    /**
     * Filter a collection of `ClothingItem`s that have a price within the range [`lower`, `upper`].
     * @param items the list of clothing items to filter on
     * @param lower the minimum price
     * @param upper the maximum price
     * @return a list of `ClothingItem`s
     */
    public List<ClothingItem> getItemsFilteredPrice(Collection<ClothingItem> items, int lower, int upper){
        List<ClothingItem> result = new ArrayList<>();

        for (ClothingItem item : items) {
            if (item.getPrice() >= lower && item.getPrice() <= upper) {
                result.add(item);
            }
        }

        return result;
    }
}
