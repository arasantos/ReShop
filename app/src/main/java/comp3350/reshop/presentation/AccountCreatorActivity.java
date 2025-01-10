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
import comp3350.reshop.application.Service;
import comp3350.reshop.logic.AccountEditor;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.objects.User;

public class AccountCreatorActivity extends AppCompatActivity {

    private AccountEditor accountCreator;
    private TextView usernameInput;
    private EditText passwordInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private GeneralSpinner citySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creator);
        loadInputFields();
        accountCreator = new AccountEditor(Service.getUserData());
    }

    /**
     * Create a new account with the input data. If the data is invalid, the account will not be
     * created, no further action is taken. If the creation is successful, the user will be directed
     * to the account page.
     * @param v view calling this action
     */
    public void onCreateAccountButtonClick(View v) {
        User user = new User(usernameInput.getText().toString(),
                passwordInput.getText().toString(), firstNameInput.getText().toString(),
                lastNameInput.getText().toString(), citySpinner.getSelectedOption());

        try {
            Intent openHomePage = new Intent(AccountCreatorActivity.this,
                    HomeActivity.class);

            // Set the current application user
            accountCreator.createNewAccount(user);
            openHomePage.putExtra("CURRENT_USER", user.getUsername());

            AccountCreatorActivity.this.startActivity(openHomePage);
            Log.i("AccountEditor",
                    "Created new account with name " + usernameInput.getText().toString());
        } catch (InvalidInputException e) {
            Log.e("AccountEditor", "Could not create account");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInputFields() {
        usernameInput = findViewById(R.id.accountUsernameInput);
        passwordInput = findViewById(R.id.accountPasswordInput);
        firstNameInput = findViewById(R.id.accountFirstNameInput);
        lastNameInput = findViewById(R.id.accountLastNameInput);
        citySpinner = new GeneralSpinner(this, findViewById(R.id.accountCitySpinner), R.array.location_dropdown_options);
    }
}