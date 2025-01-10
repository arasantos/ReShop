package comp3350.reshop.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import comp3350.reshop.R;
import comp3350.reshop.application.Service;
import comp3350.reshop.logic.ClothingItemAccessor;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.presentation.util.ImageViewerUtils;

public class PostActivity extends AppCompatActivity {
    private String currentUser;
    private ClothingItem item;
    private int itemID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ClothingItemAccessor clothingItemAccessor = new ClothingItemAccessor(Service.getClothingItemData());
        currentUser = getIntent().getStringExtra("CURRENT_USER");
        itemID = getIntent().getIntExtra("ITEM_ID",-1);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        TextView qualityTextView = findViewById(R.id.qualityTextView);
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView styleTextView = findViewById(R.id.styleTextView);
        TextView sellerTextView= findViewById(R.id.sellerTextView);
        ImageView imageView = findViewById(R.id.imageView);

        // Display item information
        item = clothingItemAccessor.getItemById(itemID);
        nameTextView.setText(item.getItemName());
        priceTextView.setText(String.format(Locale.ROOT, "Price: $%.2f", item.getPrice() / 100.00));
        qualityTextView.setText(String.format(Locale.ROOT, "Quality: %s", item.getQuality()));
        typeTextView.setText(String.format(Locale.ROOT, "Type: %s", item.getType()));
        locationTextView.setText(String.format(Locale.ROOT, "Location: %s", item.getLocation()));
        descriptionTextView.setText(String.format(Locale.ROOT, "Description:\n%s", item.getDescription()));
        styleTextView.setText(String.format(Locale.ROOT, "Style: %s", item.getStyle()));
        sellerTextView.setText(String.format(Locale.ROOT, "Seller: %s", item.getSeller()));

        // Load image from res using stored pathname
        String uriString = item.getImageUriString();
        Uri uri = ImageViewerUtils.generateUriFromString(uriString);
        ImageViewerUtils.turnUriIntoDrawable(this, imageView, uri, true);
        Log.d("PostActivity", "Parsed URI: " + uri.toString());

        loadNavigation();
        setUpBuyButtonBehaviour();
    }

    private void loadNavigation() {
        BottomNavigationView nav = findViewById(R.id.postBottomNavigation);
        Menu menu = nav.getMenu();
        MenuItem sellAction = menu.findItem(R.id.nav_action_sell);
        MenuItem homeAction = menu.findItem(R.id.nav_action_home);
        MenuItem accountAction = menu.findItem(R.id.nav_action_account);

        // Open the post creator page at the top of the back stack
        MenuItem.OnMenuItemClickListener postListener = item -> {
            Intent createPost = new Intent(PostActivity.this, PostCreatorActivity.class);

            createPost.putExtra("CURRENT_USER", currentUser);

            PostActivity.this.startActivity(createPost);
            return true;
        };

        // Open the account page
        MenuItem.OnMenuItemClickListener accountListener = item -> {
            Intent openAccount = new Intent(PostActivity.this, AccountActivity.class);

            openAccount.putExtra("CURRENT_USER", currentUser);

            PostActivity.this.startActivity(openAccount);
            return true;
        };
        // Open the home page
        MenuItem.OnMenuItemClickListener homeListener = item -> {
            Intent openHome = new Intent(PostActivity.this, HomeActivity.class);

            openHome.putExtra("CURRENT_USER", currentUser);

            PostActivity.this.startActivity(openHome);
            return true;
        };
        sellAction.setOnMenuItemClickListener(postListener);
        accountAction.setOnMenuItemClickListener(accountListener);
        homeAction.setOnMenuItemClickListener(homeListener);
    }

    public void buyButtonOnClick(View v) {
        Intent paymentIntent = new Intent(PostActivity.this, CheckoutActivity.class);
        paymentIntent.putExtra("CURRENT_USER", currentUser); // Set current user
        paymentIntent.putExtra("ITEM_ID", itemID); // Set current item

        PostActivity.this.startActivity(paymentIntent);
    }

    private void setUpBuyButtonBehaviour() {
        final Button buyButton = findViewById(R.id.buyButtonPost);
        buyButton.setEnabled(!currentUser.equals(item.getSeller()) && item.getBuyer() == null);
    }
}
