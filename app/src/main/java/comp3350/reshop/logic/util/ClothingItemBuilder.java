package comp3350.reshop.logic.util;

import comp3350.reshop.objects.ClothingItem;

public class ClothingItemBuilder implements IClothingItemBuilder {

	private ClothingItem product;

	public ClothingItemBuilder() {
		product = new ClothingItem();
	}

	@Override
	public void reset() {
		product = new ClothingItem();
	}

	public ClothingItem getProduct() {
		ClothingItem finished = product;
		reset();
		return finished;
	}

	@Override
	public void setId(int id) {
		product.setItemID(id);
	}

	@Override
	public void setName(String name) {
		product.setItemName(name);
	}

	@Override
	public void setDescription(String description) {
		product.setDescription(description);
	}

	@Override
	public void setLocation(String location) {
		product.setLocation(location);
	}

	@Override
	public void setType(String type) {
		product.setType(type);
	}

	@Override
	public void setStyle(String style) {
		product.setStyle(style);
	}

	@Override
	public void setQuality(String quality) {
		product.setQuality(quality);
	}

	@Override
	public void setPrice(int price) {
		product.setPrice(price);
	}

	@Override
	public void setImageUri(String uri) {
		product.setImageUriString(uri);
	}

	@Override
	public void setSeller(String seller) {
		product.setSeller(seller);
	}

	@Override
	public void setBuyer(String buyer) {
		product.setBuyer(buyer);
	}
}
