package comp3350.reshop.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import comp3350.reshop.R;
import comp3350.reshop.application.Service;
import comp3350.reshop.logic.ClothingItemAccessor;
import comp3350.reshop.logic.LoginManager;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.objects.User;
import comp3350.reshop.presentation.util.ClothingItemListAdapter;


public class AccountActivity extends AppCompatActivity {

    private String currentUser;
    private ClothingItemAccessor accessor;
    private RecyclerView listingsRecyclerView;
    private RecyclerView boughtItemsRecyclerView;
    private Button listingsButton;
    private Button boughtItemsButton;
    private List<ClothingItem> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        currentUser = getIntent().getStringExtra("CURRENT_USER");
        accessor = new ClothingItemAccessor(Service.getClothingItemData());

        listingsRecyclerView = findViewById(R.id.listingsRecyclerView);
        boughtItemsRecyclerView = findViewById(R.id.boughtItemsRecyclerView);

        listingsButton = findViewById(R.id.showListingsButton);
        boughtItemsButton = findViewById(R.id.showBoughtItemsButton);

        loadNavigation();
        showAccountInfo();
        showItems();
    }

    /*
     * Open the account editor page
     */
    public void onEditAccountButtonClick(View v) {
        Intent editAccountIntent = new Intent(AccountActivity.this, AccountEditorActivity.class);

        // Set current user
        editAccountIntent.putExtra("CURRENT_USER", currentUser);

        AccountActivity.this.startActivity(editAccountIntent);
    }

    public void onLogoutButtonClick(View v) {
        Intent logout = new Intent(AccountActivity.this, LoginActivity.class);
        // clear all activities on top of the last running login activity, essentially restarting the app
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AccountActivity.this.startActivity(logout);
    }


    /**
     * Gets user info using logic and displays that information
     */
    @SuppressLint("SetTextI18n") // Suppress resource string warnings
    private void showAccountInfo() {
        TextView username = findViewById(R.id.accountUsername);
        TextView fullName = findViewById(R.id.accountFullName);
        TextView accountCity = findViewById(R.id.accountCity);

        LoginManager loginManager = new LoginManager();
        User user = loginManager.getUser(currentUser);

        username.setText("@" + user.getUsername());
        fullName.setText(user.getFirstName() + " " + user.getLastName());
        accountCity.setText(user.getCity());
    }

    private void loadNavigation() {
        BottomNavigationView nav = findViewById(R.id.accountBottomNavigation);
        nav.setSelectedItemId(R.id.nav_action_account);
        Menu menu = nav.getMenu();
        MenuItem sellAction = menu.findItem(R.id.nav_action_sell);
        MenuItem accountAction = menu.findItem(R.id.nav_action_account);
        MenuItem homeAction = menu.findItem(R.id.nav_action_home);

        // Open the post creator page at the top of the back stack
        MenuItem.OnMenuItemClickListener openPostListener = item -> {
            Intent createPost = new Intent(AccountActivity.this, PostCreatorActivity.class);

            // Set current user
            createPost.putExtra("CURRENT_USER", currentUser);

            AccountActivity.this.startActivity(createPost);
            return true;
        };

        // Open the home page and search
        MenuItem.OnMenuItemClickListener openSearchListener = item -> {
            Intent openSearch = new Intent(AccountActivity.this, HomeActivity.class);

            // Set current user
            openSearch.putExtra("CURRENT_USER", currentUser);

            AccountActivity.this.startActivity(openSearch);
            return true;
        };


        sellAction.setOnMenuItemClickListener(openPostListener);
        homeAction.setOnMenuItemClickListener(openSearchListener);
        accountAction.setEnabled(false);
    }

    public void showItemsForSale(View v){
        //makes unselected button appear unselected
        boughtItemsButton.setTextColor(ContextCompat.getColor(this, R.color.purple_500));
        boughtItemsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        //makes selected button appear selected
        listingsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        listingsButton.setTextColor(ContextCompat.getColor(this, R.color.white));

        boughtItemsRecyclerView.setVisibility(View.GONE);
        listingsRecyclerView.setVisibility(View.VISIBLE);

        showItems();
    }

    public void showBoughtItems(View v){
        //makes selected button appear selected
        boughtItemsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        boughtItemsButton.setTextColor(ContextCompat.getColor(this, R.color.white));

        //makes unselected button appear unselected
        listingsButton.setTextColor(ContextCompat.getColor(this, R.color.purple_500));
        listingsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        listingsRecyclerView.setVisibility(View.GONE);
        boughtItemsRecyclerView.setVisibility(View.VISIBLE);

        itemList = accessor.getItemsByBuyer(currentUser);
        ClothingItemListAdapter adapter = new ClothingItemListAdapter(itemList,currentUser);

        boughtItemsRecyclerView.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
        boughtItemsRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void showItems() {
        itemList= accessor.getItemsBySeller(currentUser);
        ClothingItemListAdapter adapter = new ClothingItemListAdapter(itemList,currentUser);

        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
        listingsRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
