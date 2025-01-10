package comp3350.reshop.logic;

import java.util.Collections;
import java.util.List;

import comp3350.reshop.application.Service;
import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.logic.enums.Location;
import comp3350.reshop.logic.enums.Quality;
import comp3350.reshop.logic.enums.Style;
import comp3350.reshop.logic.enums.Type;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.validation.PostValidator;
import comp3350.reshop.objects.ClothingItem;

public class ClothingItemAccessor {
    private final ClothingItemData clothingItemAccess;

    public ClothingItemAccessor() {
        this.clothingItemAccess = Service.getClothingItemData();
    }

    public ClothingItemAccessor(ClothingItemData clothingItemAccess) {
        this.clothingItemAccess = clothingItemAccess;
    }

    /**
     * Returns the clothing item with the ID `id`. If there is not such item, a null-type value
     * will be returned.
     * @param id the id of the item
     * @return a single `ClothingItem`, or a null-type value
     */
    public ClothingItem getItemById(int id) {
        return clothingItemAccess.getByItemID(id);
    }

    /**
     * Return a list of all items up for sale with a name or description containing the given
     * substring.
     * @param searchTerm the substring to search on
     * @return an unmodifiable list of items
     */
    public List<ClothingItem> getItemsBySearchTerm(String searchTerm) {
        return clothingItemAccess.getBySearchTerm(searchTerm);
    }

    /**
     * Return a list of all items put up for sale by the given `seller`
     * @param seller the username of the seller
     * @return a list of `ClothingItems`
     */
    public List<ClothingItem> getItemsBySeller(String seller) {
        return Collections.unmodifiableList(clothingItemAccess.getBySeller(seller));
    }

    /**
     * Return all items that have been bought by the specified `buyer`
     * @param buyer the username of the buyer
     * @return a list of `ClothingItems`
     */
    public List<ClothingItem> getItemsByBuyer(String buyer) {
        return Collections.unmodifiableList(clothingItemAccess.getByBuyer(buyer));
    }

    /**
     * Return a list of all clothing items that are available for sale
     * @return a list of `ClothingItems`
     */
    public List<ClothingItem> getItemsWithoutBuyer() {
        return clothingItemAccess.getItemsWithoutBuyer();
    }

    /**
     * Marks a clothing item as bought with the username of the buyer
     * @param buyer the username of the buyer for the item
     * @param itemID the ID of the item being bought
     */
    public void setBuyer(String buyer,int itemID) {
        clothingItemAccess.setBuyer(buyer,itemID);
    }

    /**
     * Insert a new clothing item into the database.
     * @param item the clothing item to insert
     * @throws InvalidInputException if any of the item data is invalid
     */
    public void insertClothingItem(ClothingItem item) throws InvalidInputException {
        validateClothingItemData(item);
        clothingItemAccess.addItem(item);
    }

    /**
     * Validate the given `ClothingItem` using the static `PostValidator` validation.
     * @param clothingItem the item to validate
     * @throws InvalidInputException if any of the data is invalid
     */
    private void validateClothingItemData(ClothingItem clothingItem) throws InvalidInputException {
        PostValidator.validateItemName(clothingItem.getItemName());
        PostValidator.validateDescription(clothingItem.getDescription());
        PostValidator.validateImageUriString(clothingItem.getImageUriString());
        PostValidator.validateChoice(clothingItem.getLocation(), "Location", Location.values());
        PostValidator.validateChoice(clothingItem.getType(), "Type", Type.values());
        PostValidator.validateChoice(clothingItem.getStyle(), "Style", Style.values());
        PostValidator.validateChoice(clothingItem.getQuality(), "Quality", Quality.values());
        PostValidator.validatePrice(clothingItem.getPrice());
    }
}
