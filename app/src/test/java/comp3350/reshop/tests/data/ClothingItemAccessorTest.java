package comp3350.reshop.tests.data;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.logic.ClothingItemAccessor;
import comp3350.reshop.logic.enums.Location;
import comp3350.reshop.logic.enums.Quality;
import comp3350.reshop.logic.enums.Style;
import comp3350.reshop.logic.enums.Type;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.tests.utils.TestLogging;

public class ClothingItemAccessorTest {
    private ClothingItemData clothingItems;
    private ClothingItemAccessor accessor;
    private ClothingItemBuilder builder;

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Clothing Item Accessor integration suite =====");
    }

    @Before
    public void setUp() {
        this.clothingItems = mock();
        this.accessor = new ClothingItemAccessor(clothingItems);
        this.builder = new ClothingItemBuilder();

        List<ClothingItem> db = new ArrayList<>();
        
        db.add(buildItem(1, "A T-Shirt", "Comfortable cotton t-shirt", "Brandon", "Crop Top", "Vintage", "Like New", 1999, "android.resource://comp3350.reshop/drawable/item_converse1", "daviest")); 
        db.add(buildItem(2, "B Jacket t-shirt", "Warm winter jacket", "Selkirk", "Jacket", "Preppy", "Like New", 3999, "android.resource://comp3350.reshop/drawable/item_converse2", "thakkard"));
        db.add(buildItem(3, "C Sweater", "Cozy wool sweater", "Winkler", "T-shirt", "Preppy", "New without Tags", 2999, "android.resource://comp3350.reshop/drawable/item_converse3", "hongj"));
        db.add(buildItem(4, "D Raincoat", "Waterproof raincoat", "Winnipeg", "Pants", "Vintage", "Used - Fair", 4999, "android.resource://comp3350.reshop/drawable/item_converse4", "sunh"));
        db.add(buildItem(5, "E Shorts", "Comfortable cotton shorts", "Brandon", "Pyjamas", "Minimalist", "Used - Good", 910, "android.resource://comp3350.reshop/drawable/item_navy_pants", "santosa"));

        when(clothingItems.getByItemID(1)).thenReturn(db.get(0));
        when(clothingItems.getByItemID(2)).thenReturn(db.get(1));

        when(clothingItems.getBySeller("daviest")).thenReturn(db.subList(0,1));
        when(clothingItems.getBySeller("DaviesT")).thenReturn(new ArrayList<>());
        when(clothingItems.getBySeller("doesnotexist")).thenReturn(new ArrayList<>());

        List<ClothingItem> comfortDb = new ArrayList<>();
        comfortDb.add(db.get(0));
        comfortDb.add(db.get(4));
        when(clothingItems.getBySearchTerm("COMFORTABLE")).thenReturn( comfortDb );
        when(clothingItems.getBySearchTerm("Shirt")).thenReturn( db.subList(0,2) );
        when(clothingItems.getBySearchTerm("SHIRT")).thenReturn( db.subList(0,2) );

        builder.setBuyer("santosa");
        List<ClothingItem> santosDb = new ArrayList<>();
        santosDb.add(builder.getProduct());
        when(clothingItems.getByBuyer("santosa")).thenReturn(santosDb);

        when(clothingItems.getItemsWithoutBuyer()).thenReturn(db);
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Clothing Item Accessor integration suite =====\n");
    }

    @Test
    public void testGetBySeller() {
        assertEquals("daviest",accessor.getItemsBySeller("daviest").get(0).getSeller());
        assertEquals(0,accessor.getItemsBySeller("DaviesT").size());
        assertEquals(0,accessor.getItemsBySeller("doesnotexist").size());
    }

    @Test
    public void testGetBySearchTerms() {
        assertEquals("Comfortable cotton t-shirt",accessor.getItemsBySearchTerm("COMFORTABLE").get(0).getDescription());
        assertEquals("Comfortable cotton t-shirt",accessor.getItemsBySearchTerm("Shirt").get(0).getDescription());
        assertEquals("B Jacket t-shirt",accessor.getItemsBySearchTerm("SHIRT").get(1).getItemName());
    }

    @Test
    public void testGetByBuyer() {
        assertEquals(1,accessor.getItemsByBuyer("santosa").size());
        assertEquals("santosa",accessor.getItemsByBuyer("santosa").get(0).getBuyer());
    }

    @Test
    public void testGetItemWithoutBuyer() {
        assertEquals(5,accessor.getItemsWithoutBuyer().size());
        assertNull(accessor.getItemsWithoutBuyer().get(0).getBuyer());
    }

    @Test
    public void testSetBuyer() {

        boolean thrown = false;
        try{
            accessor.setBuyer("sunh",2);
        } catch (Exception e){
            thrown = true;
        }
        assertFalse(thrown);
    }

    @Test
    public void testInsertClothingItem() {
        ClothingItem item1 = buildBasicItem(10, "content://media/picker/0/com.android.providers.media.photopicker/media/1000000034");
        boolean passed = true;

        try {
            accessor.insertClothingItem(item1);
        } catch (Exception e) {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }

    @Test
    public void testInvalidInsertClothingItem() {
        ClothingItem item1 = buildBasicItem(11, null);

        assertThrows("Should throw exception if the clothing item to be added is invalid",
                InvalidInputException.class, () -> accessor.insertClothingItem(item1));
    }
    private ClothingItem buildBasicItem(int id, String uri) {
        builder.setId(id);
        builder.setName("name");
        builder.setDescription("description");
        builder.setLocation(Location.Brandon.toString());
        builder.setType(Type.Sandals.toString());
        builder.setStyle(Style.Preppy.toString());
        builder.setQuality(Quality.LikeNew.toString());
        builder.setPrice(1000);
        builder.setImageUri(uri);
        builder.setSeller("daviest");
        return builder.getProduct();
    }

    private ClothingItem buildItem(int id, String name, String desc, String location, String type, String style, String quality, int price, String uri, String seller) {
        builder.setId(id);
        builder.setName(name);
        builder.setDescription(desc);
        builder.setLocation(location);
        builder.setType(type);
        builder.setStyle(style);
        builder.setQuality(quality);
        builder.setPrice(price);
        builder.setImageUri(uri);
        builder.setSeller(seller);
        return builder.getProduct();
    }
}
