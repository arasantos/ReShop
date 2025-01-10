package comp3350.reshop.objects;

public class NullClothingItem extends ClothingItem {
    @Override
    public int getItemID() {
        return -1;
    }

    @Override
    public String getItemName() {
        return "Item Not Found";
    }

    @Override
    public String getDescription() {
        return "No description available";
    }

    @Override
    public String getLocation() {
        return "None";
    }

    @Override
    public String getType() {
        return "None";
    }

    @Override
    public String getStyle() {
        return "None";
    }

    @Override
    public String getQuality() {
        return "None";
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public String getImageUriString() {
        return "android.resource://comp3350.reshop/drawable/placeholder_image";
    }

    @Override
    public String getSeller() {
        return "None";
    }

    @Override
    public String getBuyer() {
        return "None";
    }
}
