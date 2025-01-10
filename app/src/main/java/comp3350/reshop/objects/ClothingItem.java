package comp3350.reshop.objects;

public class ClothingItem {
    private int itemID;
    private String itemName;
    private String description;
    private String location;
    private String type;
    private String style;
    private String quality;
    private int price;
    private String imageUriString;
    private String seller;
    private String buyer;


    public ClothingItem() {
        itemID = -1;
        itemName = "";
        description = "";
        location = "";
        type = "";
        style = "";
        quality = "";
        price = 0;
        imageUriString = "";
        seller = null;
        buyer = null;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getStyle() {
        return style;
    }

    public String getQuality() {
        return quality;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUriString() {
        return imageUriString;
    }

    public String getSeller() { return seller; }

    public String getBuyer() { return buyer; }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUriString(String imageUriString) {
        this.imageUriString = imageUriString;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
}
