package comp3350.reshop.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import comp3350.reshop.R;
import comp3350.reshop.application.Service;
import comp3350.reshop.logic.ClothingItemAccessor;
import comp3350.reshop.logic.PostSearcher;
import comp3350.reshop.logic.comparators.IdAscendingComparator;
import comp3350.reshop.logic.comparators.PriceAscendingComparator;
import comp3350.reshop.logic.comparators.PriceDescendingComparator;
import comp3350.reshop.logic.comparators.QualityAscendingComparator;
import comp3350.reshop.logic.comparators.QualityDescendingComparator;
import comp3350.reshop.logic.enums.Type;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.presentation.util.ClothingItemListAdapter;

public class HomeActivity extends AppCompatActivity {

	private ClothingItemListAdapter adapter;
	private String currentUser;
	private PostSearcher postSearcher;
	private List<ClothingItem> itemList;
	private final HashMap<Integer, Comparator<ClothingItem>> sortingOptions = new HashMap<>();
	private List<String> activeFilters;
	private Comparator<ClothingItem> activeSort;
	private int activeMinPrice, activeMaxPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		init();

		loadNavigation();
		loadSearch();
		loadTypeFilters();
		loadPriceFilters();
		loadSort();
		loadContent();
	}

	/**
	 * Initialize variables
	 */
	private void init() {
		ClothingItemAccessor accessor = new ClothingItemAccessor(Service.getClothingItemData());
		currentUser = getIntent().getStringExtra("CURRENT_USER");
		postSearcher = new PostSearcher(Service.getClothingItemData());

		itemList = accessor.getItemsWithoutBuyer();
		activeFilters = new ArrayList<>();
		activeMinPrice = 0;
		activeMaxPrice = Integer.MAX_VALUE;
		activeSort = new IdAscendingComparator();
		adapter = new ClothingItemListAdapter(itemList,currentUser);
	}

	/**
	 * Populates the page content with clothing items from the database. Filters and sorts the
	 * results based on the active filter and sort parameters
	 */
	private void loadContent() {
		RecyclerView searchResults = findViewById(R.id.searchContent);

		List<ClothingItem> results;
		results = postSearcher.getItemsFilteredType(itemList, activeFilters);
		results = postSearcher.getItemsFilteredPrice(results, activeMinPrice, activeMaxPrice);

		// sort items, use default if no sort selected
		postSearcher.sort(results, activeSort);
		adapter.setSearchList(results);

		searchResults.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
		searchResults.setAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

	/**
	 * Loads the bottom navigation bar
	 */
	private void loadNavigation() {
		BottomNavigationView nav = findViewById(R.id.accountBottomNavigation);
		Menu menu = nav.getMenu();
		MenuItem sellAction = menu.findItem(R.id.nav_action_sell);
		MenuItem homeAction = menu.findItem(R.id.nav_action_home);
		MenuItem accountAction = menu.findItem(R.id.nav_action_account);
		nav.setSelectedItemId(R.id.nav_action_home);

		// Open the post creator page at the top of the back stack
		MenuItem.OnMenuItemClickListener postListener = item -> {
			Intent createPost = new Intent(HomeActivity.this, PostCreatorActivity.class);

			createPost.putExtra("CURRENT_USER", currentUser);

			HomeActivity.this.startActivity(createPost);
			return true;
		};

		// Open the account page
		MenuItem.OnMenuItemClickListener accountListener = item -> {
			Intent openAccount = new Intent(HomeActivity.this, AccountActivity.class);

			openAccount.putExtra("CURRENT_USER", currentUser);

			HomeActivity.this.startActivity(openAccount);
			return true;
		};

		sellAction.setOnMenuItemClickListener(postListener);
		accountAction.setOnMenuItemClickListener(accountListener);
		homeAction.setEnabled(false);
	}

	/**
	 * Populates the filter chip group with the list of available clothing types. Sets the active
	 * filters when the state of any of the chips changes.
	 */
	private void loadTypeFilters() {
		Type[] types = Type.values();
		ChipGroup filters = findViewById(R.id.searchFilters);
		filters.removeAllViews();

		for (Type type : types) {
			Chip filter = new Chip(this);

			filter.setId(type.ordinal());
			filter.setCheckable(true);
			filter.setText(type.toString());
			filter.setTextSize(2, 16);
			filter.setChecked(false);
			filters.addView(filter);
		}

		filters.setOnCheckedStateChangeListener((group, checkedIds) -> {
			List<String> checkedFilters = new ArrayList<>();

			// Set the list of active filters based on which chips are checked
			for (Integer id : checkedIds) {
				Chip child = (Chip) group.getChildAt(id);
				checkedFilters.add(child.getText().toString());
			}
			activeFilters = checkedFilters;
			loadContent();
		});
	}

	/**
	 * Initialize the price filter inputs to set the active price filters on enter key press
	 */
	private void loadPriceFilters() {
		EditText minInput = findViewById(R.id.minInput), maxInput = findViewById(R.id.maxInput);

		minInput.setOnEditorActionListener((v, actionId, event) -> {
			try {
				String input = minInput.getText().toString();
				if (input.trim().isEmpty()) {
					// default value
					activeMinPrice = 0;
				} else {
					activeMinPrice = (int)(Double.parseDouble(input)*100);
				}

				loadContent();
				return true;
			} catch (NumberFormatException e) {
				Toast.makeText(this, R.string.error_price_filter, Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		maxInput.setOnEditorActionListener((v, actionId, event) -> {
			try {
				String input = maxInput.getText().toString();
				if (input.trim().isEmpty()) {
					// default value
					activeMaxPrice = Integer.MAX_VALUE;
				} else {
					activeMaxPrice = (int)(Double.parseDouble(input)*100);
				}
				loadContent();
				return true;
			} catch (NumberFormatException e) {
				Toast.makeText(this, R.string.error_price_filter, Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}

	/**
	 * Initializes the comparators for the sort dropdown and listens for a selected option to update
	 * the page content.
	 */
	private void loadSort() {
		sortingOptions.put(0, new IdAscendingComparator());
		sortingOptions.put(1, new PriceAscendingComparator());
		sortingOptions.put(2, new PriceDescendingComparator());
		sortingOptions.put(3, new QualityAscendingComparator());
		sortingOptions.put(4, new QualityDescendingComparator());

		Spinner sortSpinner = findViewById(R.id.SortSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this,
				R.array.sort_dropdown_options,
				android.R.layout.simple_spinner_item
		);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sortSpinner.setAdapter(adapter);
		sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			/**
			 * Set the selected sorting comparator to the `Comparator` mapped to the selected
			 * option's position in the dropdown
			 * @param parent The AdapterView where the selection happened
			 * @param view The view within the AdapterView that was clicked
			 * @param position The position of the view in the adapter
			 * @param id The row id of the item that is selected
			 */
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				activeSort = sortingOptions.get(position);
				loadContent();
			}

			/**
			 * When no sort option is selected, use the default sort by ID ascending
			 * @param parent The AdapterView that now contains no selected item.
			 */
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				activeSort = new IdAscendingComparator();
				loadContent();
			}
		});
	}

	/**
	 * Initialize the search bar to listen for text changes
	 */
	private void loadSearch() {
		SearchView searchBarContent = findViewById(R.id.searchBar);

		searchBarContent.clearFocus();
		searchBarContent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			/**
			 * Update the list content whenever the search query is submitted.
			 * Aliases the `onQueryTextChange` method.
			 * @param query the query text that is to be submitted
			 *
			 * @return default true
			 */
			@Override
			public boolean onQueryTextSubmit(String query) {
				return onQueryTextChange(query);
			}

			/**
			 * Update the list content whenever the search query changes
			 * @param searchQuery the new content of the query text field.
			 *
			 * @return default true
			 */
			@Override
			public boolean onQueryTextChange(String searchQuery) {
				String emptyListMessage = "There are no items that match \"" + searchQuery + "\"";
				itemList = postSearcher.getItemsFromSearch(searchQuery);

				if (itemList.isEmpty()) {
					Toast.makeText(HomeActivity.this, emptyListMessage, Toast.LENGTH_SHORT).show();
				}

				loadContent();

				return true;
			}
		});
	}
}