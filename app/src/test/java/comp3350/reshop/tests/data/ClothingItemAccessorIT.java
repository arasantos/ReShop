package comp3350.reshop.tests.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.File;
import java.io.IOException;

import comp3350.reshop.application.Main;
import comp3350.reshop.data.hsqldb.ClothingItemHSQLDB;
import comp3350.reshop.logic.ClothingItemAccessor;
import comp3350.reshop.logic.enums.Location;
import comp3350.reshop.logic.enums.Quality;
import comp3350.reshop.logic.enums.Style;
import comp3350.reshop.logic.enums.Type;
import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.tests.utils.TestDB;
import comp3350.reshop.tests.utils.TestLogging;

public class ClothingItemAccessorIT {
    private ClothingItemHSQLDB clothingItems;
    private ClothingItemAccessor accessor;
    private ClothingItemBuilder builder;
    private File tempDB;

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Clothing Item Accessor integration suite =====");
    }

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestDB.copyDB();
        this.clothingItems = new ClothingItemHSQLDB(Main.getDBPathName(), new ClothingItemBuilder());
        this.accessor = new ClothingItemAccessor(clothingItems);
        this.builder = new ClothingItemBuilder();
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
        accessor.setBuyer("santosa",1);
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
        assertNull(accessor.getItemById(2).getBuyer());
        accessor.setBuyer("sunh",2);
        assertEquals("sunh",accessor.getItemById(2).getBuyer());
    }

    @Test
    public void testInsertClothingItem() {
        ClothingItem item1 = buildItem(10, "content://media/picker/0/com.android.providers.media.photopicker/media/1000000034");
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
        ClothingItem item1 = buildItem(11, null);

        assertThrows("Should throw exception if the clothing item to be added is invalid",
                InvalidInputException.class, () -> accessor.insertClothingItem(item1));
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }

    private ClothingItem buildItem(int id, String uri) {
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
}
