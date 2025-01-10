package comp3350.reshop.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import comp3350.reshop.R;
import comp3350.reshop.application.Main;
import comp3350.reshop.application.Service;
import comp3350.reshop.logic.LoginManager;
import comp3350.reshop.logic.exceptions.InvalidInputException;

public class LoginActivity extends AppCompatActivity {

    private LoginManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The database should always be copied to the device before any other logic class is called,
        // otherwise managers or whichever logic classes will not get the updated path name.
        Main.copyDatabaseToDevice(getApplicationContext());
        lm = new LoginManager(Service.getUserData());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            final EditText usernameInput = findViewById(R.id.usernameInput);
            final EditText passwordInput = findViewById(R.id.passwordInput);
            final Button loginButton = findViewById(R.id.loginButton);

            loginButton.setEnabled(!usernameInput.getText().toString().isEmpty() &&
                    !passwordInput.getText().toString().isEmpty());

            /*
             * Set a text watcher to enable the login button if the fields are filled in.
             */
            TextWatcher loginWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    loginButton.setEnabled(!usernameInput.getText().toString().isEmpty() &&
                            !passwordInput.getText().toString().isEmpty());
                }
            };

            usernameInput.addTextChangedListener(loginWatcher);
            passwordInput.addTextChangedListener(loginWatcher);

        } catch (final Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onDestroy() { super.onDestroy(); }


    /**
     * Attempt to log into the app using the provided username and password. If the username does
     * not exist or the password does not match the username found, an error will occur.
     * <br><br>
     * A successful login will direct the user to the home activity.
     * @param v the view calling this intent
     */
    public void onLoginButtonClick(View v) {
        try {
            final EditText usernameInput = findViewById(R.id.usernameInput);
            final EditText passwordInput = findViewById(R.id.passwordInput);
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            lm.validateLogin(username, password);

            Intent login = new Intent(LoginActivity.this,
                    HomeActivity.class);

            // Set current application user
            login.putExtra("CURRENT_USER", username);

            LoginActivity.this.startActivity(login);
        } catch (InvalidInputException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Direct user to the Account Creation Page
     * @param v the view calling this intent
     */
    public void onCreateAccountButtonClick(View v) {
        Intent createAccountIntent = new Intent(LoginActivity.this,
                AccountCreatorActivity.class);
        LoginActivity.this.startActivity(createAccountIntent);
    }
}