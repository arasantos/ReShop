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
import comp3350.reshop.logic.LoginManager;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.objects.User;

public class AccountEditorActivity extends AppCompatActivity {

    private AccountEditor accountEditor;
    private TextView usernameInput;
    private EditText passwordInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private GeneralSpinner citySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editor);

        loadInputFields();
        accountEditor = new AccountEditor(Service.getUserData());
        initializeAccountData();
    }

    /**
     * Update the current account with new input data. If the data is invalid, the account will not be
     * updated, no action is taken. Once updated, the user will be taken back to the account page.
     * @param v view calling this action
     */
    public void onSaveAccountDataButtonClick(View v) {
        User user = new User(usernameInput.getText().toString(), passwordInput.getText().toString(),
                firstNameInput.getText().toString(), lastNameInput.getText().toString(),
                citySpinner.getSelectedOption());

        try {
            Intent saveAccount = new Intent(AccountEditorActivity.this, AccountActivity.class);

            // Update the current application user
            accountEditor.updateAccount(user);
            saveAccount.putExtra("CURRENT_USER", user.getUsername());

            AccountEditorActivity.this.startActivity(saveAccount);
            Log.i("AccountEditor",
                    "Update account with username " + usernameInput.getText().toString());
        } catch (InvalidInputException e) {
            Log.e("AccountEditor", "Could not save account data");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Do not save any input entered. Return to the account page.
     * @param v view calling this action
     */
    public void onCancelEditButtonClick(View v) {
        finish();
    }

    private void loadInputFields() {
        usernameInput = findViewById(R.id.accountUsernameText);
        passwordInput = findViewById(R.id.accountPasswordInput);
        firstNameInput = findViewById(R.id.accountFirstNameInput);
        lastNameInput = findViewById(R.id.accountLastNameInput);
        citySpinner = new GeneralSpinner(this, findViewById(R.id.accountCitySpinner), R.array.location_dropdown_options);
    }

    /**
     * Fetch the data for the current user to populate the input forms as hints.
     */
    private void initializeAccountData() {
        LoginManager loginManager = new LoginManager();
        String currentUser = getIntent().getStringExtra("CURRENT_USER");
        User user = loginManager.getUser(currentUser);

        usernameInput.setText(user.getUsername());
        passwordInput.setText(user.getPassword());
        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
        citySpinner.setSelectedOption(user.getCity());
    }
}
