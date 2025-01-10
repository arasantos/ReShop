package comp3350.reshop.tests.logic;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.tests.utils.TestLogging;

public class ClothingItemBuilderTest {

	@Rule
	public TestRule logger = TestLogging.getTestLogger();

	@BeforeClass
	public static void startSuite() {
		System.out.println("===== Starting clothing item builder test =====");
	}

	@AfterClass
	public static void endSuite() {
		System.out.println("===== Completed clothing item builder test =====");
	}

	@Test
	public void testBuildClothingItem() {
		ClothingItemBuilder builder = new ClothingItemBuilder();

		builder.setId(1);
		builder.setName("name");
		builder.setDescription("description");
		builder.setLocation("location");
		builder.setType("type");
		builder.setStyle("style");
		builder.setQuality("quality");
		builder.setPrice(1000);
		builder.setImageUri("uri");
		builder.setSeller("seller");
		builder.setBuyer("buyer");
		ClothingItem result = builder.getProduct();

		assertEquals("Builder should set the id of the item",
				1, result.getItemID());
		assertEquals("Builder should set the name of the item",
				"name", result.getItemName());
		assertEquals("Builder should set the description of the item",
				"description", result.getDescription());
		assertEquals("Builder should set the location of the item",
				"location", result.getLocation());
		assertEquals("Builder should set the type of the item",
				"type", result.getType());
		assertEquals("Builder should set the style of the item",
				"style", result.getStyle());
		assertEquals("Builder should set the quality of the item",
				"quality", result.getQuality());
		assertEquals("Builder should set the price of the item",
				1000, result.getPrice());
		assertEquals("Builder should set the uri of the item",
				"uri", result.getImageUriString());
		assertEquals("Builder should set the seller of the item",
				"seller", result.getSeller());
		assertEquals("Builder should set the buyer of the item",
				"buyer", result.getBuyer());
	}
}
