package comp3350.reshop.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import comp3350.reshop.R;
import comp3350.reshop.application.Service;
import comp3350.reshop.logic.ClothingItemAccessor;
import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.presentation.util.ImageViewerUtils;

public class PostCreatorActivity extends AppCompatActivity {
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private GeneralSpinner locationSpinner;
    private GeneralSpinner clothingTypeSpinner;
    private GeneralSpinner styleSpinner;
    private GeneralSpinner qualitySpinner;
    private Uri imageUri;
    private String currentUser;

    private final ClothingItemAccessor clothingItemAccessor = new ClothingItemAccessor(Service.getClothingItemData());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creator);
        currentUser = getIntent().getStringExtra("CURRENT_USER");

        openPhotoPicker();
        setUpInputFields();
    }

    private void setUpInputFields() {
        locationSpinner = new GeneralSpinner(this, findViewById(R.id.locationDropdown), R.array.location_dropdown_options);
        clothingTypeSpinner = new GeneralSpinner(this, findViewById(R.id.clothingTypeDropdown), R.array.type_dropdown_options);
        styleSpinner = new GeneralSpinner(this, findViewById(R.id.styleDropdown), R.array.style_dropdown_options);
        qualitySpinner = new GeneralSpinner(this, findViewById(R.id.qualityDropdown), R.array.quality_dropdown_options);

        imageUri = null;
    }

    public void uploadImageButtonOnClick(View v) {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public void removeImageButtonOnClick(View v) {
        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        uploadImageButton.setVisibility(View.VISIBLE);

        ImageView uploadedItemImageView = findViewById(R.id.uploadedItemImageView);
        uploadedItemImageView.setVisibility(View.GONE);

        Button removeImageButton = findViewById(R.id.removeImageButton);
        removeImageButton.setVisibility(View.GONE);

        imageUri = null;
    }

    private void showSelectedImage() {
        ImageView uploadedItemImageView = findViewById(R.id.uploadedItemImageView);
        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        Button removeImageButton = findViewById(R.id.removeImageButton);

        uploadImageButton.setVisibility(View.GONE);
        uploadedItemImageView.setVisibility(View.VISIBLE);
        removeImageButton.setVisibility(View.VISIBLE);

        ImageViewerUtils.turnUriIntoDrawable(this, uploadedItemImageView, imageUri, true);
    }

    private void openPhotoPicker() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> { //uri identifies the resource, usually the location
            if (uri != null) {
                processImage(uri);
                showSelectedImage();
            }
        });
    }

    /**
     * Turns the uri that was retrieved from user's device into a bitmap,
     * then creates a uri for that bitmap, so we can always have access to uri
     * @param uri: chosen image uri
     */
    private void processImage(Uri uri) {
        Bitmap bitmap = ImageViewerUtils.getBitmapFromUri(this, uri);
        imageUri = ImageViewerUtils.getImageUriFromBitmap(this, bitmap);
    }

    public void sellButtonOnClick(View view) {
        EditText priceEditText = findViewById(R.id.priceInput);
        EditText descriptionEditText = findViewById(R.id.descriptionTextInput);
        EditText nameEditText = findViewById(R.id.itemNameInput);

        String price = priceEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String name = nameEditText.getText().toString();

        String selectedClothingStyle = styleSpinner.getSelectedOption();
        String selectedLocation = locationSpinner.getSelectedOption();
        String selectedQuality = qualitySpinner.getSelectedOption();
        String selectedClothingType = clothingTypeSpinner.getSelectedOption();
        String seller = currentUser;
        String imageUriString = "";

        if (imageUri != null) {
            imageUriString = imageUri.toString();
        }

        try {
            double parsedPrice = Double.parseDouble(price);
            ClothingItemBuilder builder = new ClothingItemBuilder();
            builder.setName(name);
            builder.setDescription(description);
            builder.setLocation(selectedLocation);
            builder.setType(selectedClothingType);
            builder.setStyle(selectedClothingStyle);
            builder.setQuality(selectedQuality);
            builder.setPrice((int)(parsedPrice*100));
            builder.setImageUri(imageUriString);
            builder.setSeller(seller);
            ClothingItem newItem = builder.getProduct();

            clothingItemAccessor.insertClothingItem(newItem);

            Toast.makeText(PostCreatorActivity.this, "Submitted successfully", Toast.LENGTH_SHORT).show();

            Intent openHomePage = new Intent(PostCreatorActivity.this, HomeActivity.class);
            openHomePage.putExtra("CURRENT_USER", currentUser);

            PostCreatorActivity.this.startActivity(openHomePage);

            resetDropdowns();
        }

        catch(InvalidInputException e) {
            Log.e("PostCreatorActivity", "Could not create new post");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        catch (NumberFormatException e) {
            Toast.makeText(this, "Price must not be an invalid format.", Toast.LENGTH_SHORT).show(); //string can't be parsed to double
        }
    }

    private void resetDropdowns() {
        locationSpinner.resetDropdown();
        clothingTypeSpinner.resetDropdown();
        styleSpinner.resetDropdown();
        qualitySpinner.resetDropdown();
    }

    public void cancelButtonOnClick(View view) {
        PostCreatorActivity.this.finish();
    }
}