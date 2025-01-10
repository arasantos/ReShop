package comp3350.reshop.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import comp3350.reshop.R;
import comp3350.reshop.logic.PaymentManager;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.util.PaymentBuilder;
import comp3350.reshop.objects.Payment;

public class CheckoutActivity extends AppCompatActivity {
    private String currentUser;
    private GeneralSpinner citySpinner;
    private int itemID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        citySpinner = new GeneralSpinner(this, findViewById(R.id.locationDropdown), R.array.location_dropdown_options);
        currentUser = getIntent().getStringExtra("CURRENT_USER");
        itemID = getIntent().getIntExtra("ITEM_ID", -1);
    }

    public void buyButtonOnClick(View view) {
        EditText addressEditText = findViewById(R.id.addressInput);
        TextView countryTextView = findViewById(R.id.countryText);
        EditText postalCodeEditText = findViewById(R.id.postalCodeInput);
        EditText nameEditText = findViewById(R.id.nameOnCardInput);
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberInput);
        EditText cvvEditText = findViewById(R.id.cvvInput);
        EditText cardNumberEditText = findViewById(R.id.cardNumberInput);
        EditText expiryDateEditText = findViewById(R.id.expiryInput);

        String city = citySpinner.getSelectedOption();
        String country = countryTextView.getText().toString();
        String address = addressEditText.getText().toString() + ", " + city + ", " + country;
        String postalCode = postalCodeEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String cvv = cvvEditText.getText().toString();
        String cardNumber = cardNumberEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();

        PaymentBuilder builder = new PaymentBuilder();
        builder.setAddress(address);
        builder.setPostalCode(postalCode);
        builder.setName(name);
        builder.setPhoneNumber(phoneNumber);
        builder.setCvv(cvv);
        builder.setCardNumber(cardNumber);
        builder.setExpiry(expiryDate);
        Payment payment = builder.getProduct();
        PaymentManager paymentManager = new PaymentManager();

        try {
            paymentManager.processPayment(payment, itemID, currentUser);
            Toast.makeText(CheckoutActivity.this, "Bought successfully", Toast.LENGTH_SHORT).show();

            Intent openHomePage = new Intent(CheckoutActivity.this, HomeActivity.class);
            openHomePage.putExtra("CURRENT_USER", currentUser);
            CheckoutActivity.this.startActivity(openHomePage);
        } catch (InvalidInputException e) {
            Log.e("CheckoutActivity", "Could not process payment");
            Toast.makeText(CheckoutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void cancelButtonOnClick(View view) {
        CheckoutActivity.this.finish();
    }
}
