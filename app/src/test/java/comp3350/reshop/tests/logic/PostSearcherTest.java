package comp3350.reshop.tests.logic;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.logic.PostSearcher;
import comp3350.reshop.logic.comparators.IdAscendingComparator;
import comp3350.reshop.logic.comparators.PriceAscendingComparator;
import comp3350.reshop.logic.comparators.PriceDescendingComparator;
import comp3350.reshop.logic.comparators.QualityAscendingComparator;
import comp3350.reshop.logic.comparators.QualityDescendingComparator;
import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.tests.utils.TestLogging;
import comp3350.reshop.logic.enums.Quality;

public class PostSearcherTest {
    private ClothingItem validItem;
    private ClothingItem validItem2;
    private ClothingItem validItem3;
    private ClothingItem validItem4;
    private final ClothingItemBuilder builder = new ClothingItemBuilder();
    private ClothingItemData db;
    private PostSearcher postSearcher;

    @Rule
    public TestRule logger = TestLogging.getTestLogger();

    @BeforeClass
    public static void startSuite() {
        System.out.println("\n===== Starting Post Searcher suite =====");
    }

    @Before
    public void setUp() {
        db = mock();
        postSearcher = new PostSearcher(db);

        builder.setId(0);
        builder.setName("Test Item");
        builder.setPrice(1550);
        builder.setType("Shoes");
        validItem = builder.getProduct();

        builder.setId(1);
        builder.setName("Test Item 2");
        builder.setPrice(2600);
        builder.setType("Shoes");
        validItem2 = builder.getProduct();

        builder.setId(2);
        builder.setName("not in search one");
        builder.setPrice(100);
        builder.setType("Hoodie");
        validItem3 = builder.getProduct();

        builder.setId(3);
        builder.setName("four");
        builder.setPrice(100);
        builder.setType("Jacket");
        validItem4 = builder.getProduct();
    }

    @AfterClass
    public static void endSuite() {
        System.out.println("===== Completed Post Searcher suite =====\n");
    }

    @Test
    public void testSearchOnSingleToken(){
        String searchForTest = "Test";
        String searchForNot = "not";

        // Set mocks
        List<ClothingItem> mockStringTestList = new ArrayList<>();
        mockStringTestList.add(validItem);
        mockStringTestList.add(validItem2);
        when(db.getBySearchTerm("Test")).thenReturn(mockStringTestList);

        List<ClothingItem> mockStringNotList = new ArrayList<>();
        mockStringNotList.add(validItem3);
        when(db.getBySearchTerm("not")).thenReturn(mockStringNotList);

        // Set expected
        Set<ClothingItem> resultTest = new HashSet<>();
        resultTest.add(validItem);
        resultTest.add(validItem2);

        Set<ClothingItem> resultNot = new HashSet<>();
        resultNot.add(validItem3);

        // Tests
        assertEquals(new HashSet<>(postSearcher.getItemsFromSearch(searchForTest)),resultTest);
        assertEquals(new HashSet<>(postSearcher.getItemsFromSearch(searchForNot)),resultNot);
    }

    @Test
    public void testSearchMultiToken() {
        String searchForSearchItem = "search Item";
        String searchForTestItem = "Test Item";

        // Set mocks
        Set<ClothingItem> resultSearchItem = new HashSet<>();
        resultSearchItem.add(validItem);
        resultSearchItem.add(validItem2);
        resultSearchItem.add(validItem3);

        Set<ClothingItem> resultTestItem = new HashSet<>();
        resultTestItem.add(validItem);
        resultTestItem.add(validItem2);

        // Set expected
        List<ClothingItem> mockStringSearchListmockStringNotList = new ArrayList<>();
        mockStringSearchListmockStringNotList.add(validItem3);
        when(db.getBySearchTerm("search")).thenReturn(mockStringSearchListmockStringNotList);

        List<ClothingItem> mockStringItemList = new ArrayList<>();
        mockStringItemList.add(validItem);
        mockStringItemList.add(validItem2);
        when(db.getBySearchTerm("Item")).thenReturn(mockStringItemList);

        // Test
        assertEquals(new HashSet<>(postSearcher.getItemsFromSearch(searchForSearchItem)),resultSearchItem);
        assertEquals(new HashSet<>(postSearcher.getItemsFromSearch(searchForTestItem)),resultTestItem);
    }

    @Test
    public void testPriceDescendingSort() {
        builder.setPrice(100);
        ClothingItem cheapItem = builder.getProduct();

        builder.setPrice(9000);
        ClothingItem expensiveItem = builder.getProduct();

        builder.setPrice(120);
        ClothingItem cheapItem2 = builder.getProduct();

        builder.setPrice(9900);
        ClothingItem expensiveItem2 = builder.getProduct();

        List<ClothingItem> result = new ArrayList<>();
        result.add(cheapItem);
        result.add(expensiveItem);
        result.add(cheapItem2);
        result.add(expensiveItem2);

        // ------------------ Test Descending --------------------
        List<ClothingItem> expectedPriceDescendingResult = new ArrayList<>();
        Comparator<ClothingItem> priceDescendingComparator = new PriceDescendingComparator();
        expectedPriceDescendingResult.add(expensiveItem2);
        expectedPriceDescendingResult.add(expensiveItem);
        expectedPriceDescendingResult.add(cheapItem2);
        expectedPriceDescendingResult.add(cheapItem);

        postSearcher.sort(result, priceDescendingComparator);
        assertEquals(result, expectedPriceDescendingResult);
    }

    @Test
    public void testPriceAscendingSort() {
        builder.setPrice(100);
        ClothingItem cheapItem = builder.getProduct();

        builder.setPrice(9000);
        ClothingItem expensiveItem = builder.getProduct();

        builder.setPrice(120);
        ClothingItem cheapItem2 = builder.getProduct();

        builder.setPrice(9900);
        ClothingItem expensiveItem2 = builder.getProduct();

        List<ClothingItem> result = new ArrayList<>();
        result.add(cheapItem);
        result.add(expensiveItem);
        result.add(cheapItem2);
        result.add(expensiveItem2);
        // ------------------ Test Ascending --------------------
        List<ClothingItem> expectedPriceAscendingResult = new ArrayList<>();
        Comparator<ClothingItem> priceAscendingComparator = new PriceAscendingComparator();
        expectedPriceAscendingResult.add(cheapItem);
        expectedPriceAscendingResult.add(cheapItem2);
        expectedPriceAscendingResult.add(expensiveItem);
        expectedPriceAscendingResult.add(expensiveItem2);

        postSearcher.sort(result, priceAscendingComparator);
        assertEquals(result, expectedPriceAscendingResult);
    }

    @Test
    public void testQualityDescendingSort() {
        builder.setQuality(Quality.UsedFair.toString());
        ClothingItem fairItem = builder.getProduct();

        builder.setQuality(Quality.UsedExcellent.toString());
        ClothingItem excellentItem = builder.getProduct();

        builder.setQuality(Quality.UsedGood.toString());
        ClothingItem goodItem = builder.getProduct();

        builder.setQuality(Quality.NewWithTags.toString());
        ClothingItem newItem = builder.getProduct();

        List<ClothingItem> result = new ArrayList<>();
        result.add(fairItem);
        result.add(excellentItem);
        result.add(goodItem);
        result.add(newItem);

        // ------------------ Test Descending --------------------
        List<ClothingItem> expectedPriceDescendingResult = new ArrayList<>();
        Comparator<ClothingItem> qualityDescendingComparator = new QualityDescendingComparator();
        expectedPriceDescendingResult.add(newItem);
        expectedPriceDescendingResult.add(excellentItem);
        expectedPriceDescendingResult.add(goodItem);
        expectedPriceDescendingResult.add(fairItem);

        postSearcher.sort(result, qualityDescendingComparator);
        assertEquals(result, expectedPriceDescendingResult);
    }

    @Test
    public void testQualityAscendingSort(){
        builder.setQuality(Quality.UsedFair.toString());
        ClothingItem fairItem = builder.getProduct();

        builder.setQuality(Quality.UsedExcellent.toString());
        ClothingItem excellentItem = builder.getProduct();

        builder.setQuality(Quality.UsedGood.toString());
        ClothingItem goodItem = builder.getProduct();

        builder.setQuality(Quality.NewWithTags.toString());
        ClothingItem newItem = builder.getProduct();

        List<ClothingItem> result = new ArrayList<>();
        result.add(fairItem);
        result.add(excellentItem);
        result.add(goodItem);
        result.add(newItem);
        // ------------------ Test Ascending --------------------
        List<ClothingItem> expectedPriceAscendingResult = new ArrayList<>();
        Comparator<ClothingItem> qualityAscendingComparator = new QualityAscendingComparator();
        expectedPriceAscendingResult.add(fairItem);
        expectedPriceAscendingResult.add(goodItem);
        expectedPriceAscendingResult.add(excellentItem);
        expectedPriceAscendingResult.add(newItem);

        postSearcher.sort(result, qualityAscendingComparator);
        assertEquals(result, expectedPriceAscendingResult);
    }

    @Test
    public void testIdAscendingSort() {
        builder.setId(1);
        ClothingItem first = builder.getProduct();

        builder.setId(2);
        ClothingItem second = builder.getProduct();

        builder.setId(3);
        ClothingItem third = builder.getProduct();

        List<ClothingItem> expected = new ArrayList<>();
        expected.add(first);
        expected.add(second);
        expected.add(third);

        // ------------------ Test Ascending --------------------
        List<ClothingItem> actual = new ArrayList<>();
        actual.add(second);
        actual.add(third);
        actual.add(first);
        Comparator<ClothingItem> idAscendingComparator = new IdAscendingComparator();

        assertNotEquals("List before sorting should not be in order", expected, actual);
        postSearcher.sort(actual, idAscendingComparator);
        assertEquals("List after sorting should match expected list object", expected, actual);
    }

    @Test
    public void testSearchOnSingleType(){
        List<String> searchForShoes = new ArrayList<>();
        searchForShoes.add("Shoes");

        // Set stub
        List<ClothingItem> mockStringShoesList = new ArrayList<>();
        mockStringShoesList.add(validItem);
        mockStringShoesList.add(validItem2);
        mockStringShoesList.add(validItem3);


        // Set expected
        Set<ClothingItem> resultShoes = new HashSet<>();
        resultShoes.add(validItem);
        resultShoes.add(validItem2);

        // Tests
        assertEquals(resultShoes,
                new HashSet<>(postSearcher.getItemsFilteredType(mockStringShoesList, searchForShoes)));
    }

    @Test
    public void testSearchOnPrice(){
        int lower = 150;
        int upper = 2000;

        // Set mocks
        List<ClothingItem> mockIntPriceList = new ArrayList<>();
        mockIntPriceList.add(validItem);
        mockIntPriceList.add(validItem2);

        // Set expected
        Set<ClothingItem> resultPrice = new HashSet<>();
        resultPrice.add(validItem);

        // Tests
        assertEquals(resultPrice,
                new HashSet<>(postSearcher.getItemsFilteredPrice(mockIntPriceList, lower,upper)));
    }

    @Test
    public void testSearchMultiType() {
        List<String> searchForHoodieShoes = new ArrayList<>();
        searchForHoodieShoes.add("Hoodie");
        searchForHoodieShoes.add("Shoes");

        // Set expected
        Set<ClothingItem> resultHoodieShoes = new HashSet<>();
        resultHoodieShoes.add(validItem);
        resultHoodieShoes.add(validItem2);
        resultHoodieShoes.add(validItem3);

        // Set mocks
        List<ClothingItem> mockList = new ArrayList<>();
        mockList.add(validItem);
        mockList.add(validItem2);
        mockList.add(validItem3);
        mockList.add(validItem4);

        // Test
        assertEquals(resultHoodieShoes,
                new HashSet<>(postSearcher.getItemsFilteredType(mockList, searchForHoodieShoes)));
    }

    @Test
    public void testNoFilters() {
        List<String> empty = new ArrayList<>();

        // Set expected
        Set<ClothingItem> expected = new HashSet<>();
        expected.add(validItem);
        expected.add(validItem2);
        expected.add(validItem3);

        // Test
        assertEquals(expected, new HashSet<>(postSearcher.getItemsFilteredType(expected, empty)));
    }
}
