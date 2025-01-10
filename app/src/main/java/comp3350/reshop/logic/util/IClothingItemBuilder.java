package comp3350.reshop.logic.util;

public interface IClothingItemBuilder {
	void reset();
	void setId(int id);
	void setName(String name);
	void setDescription(String description);
	void setLocation(String location);
	void setType(String type);
	void setStyle(String style);
	void setQuality(String quality);
	void setPrice(int price);
	void setImageUri(String uri);
	void setSeller(String seller);
	void setBuyer(String buyer);
}
