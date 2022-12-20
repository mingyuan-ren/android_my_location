package com.example.mylocation;

import static com.applovin.sdk.AppLovinSdk.initializeSdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylocation.database.UserDBHelper;

/**
 * Starting activity and user login page for the application
 */
public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText username_login_text;
    private EditText password_login_text;
    UserDBHelper dbHelper;

    /**
     * Method used for creating new instance of user login activity and initialising onClickListeners for Buttons/TextView.
     * @param savedInstanceState - current activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //For disabling night mode for login page
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        username_login_text = (EditText) findViewById(R.id.username_login_text);
        password_login_text = (EditText) findViewById(R.id.password_login_text);

        Button login_confirm = (Button) findViewById(R.id.login_confirm);
        TextView login_forget_password = (TextView) findViewById(R.id.login_forget_password);
        TextView login_register = (TextView) findViewById(R.id.login_register);

        login_confirm.setOnClickListener(this);
        login_forget_password.setOnClickListener(this);
        login_register.setOnClickListener(this);
    }

    /**
     * Method used for defining behaviour for onClick for Sign In/Sign Up Pages.
     * @param v - current view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_confirm) {
            // Check if the username and password are correct, if so, go to the main page
            if (CheckLogin()) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("username", username_login_text.getText().toString().trim());
                startActivity(intent);
            } else {
                Toast.makeText(this, "The username or password is incorrect!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId() == R.id.login_forget_password) {
            // Go to the change password page
            startActivity(new Intent(this, ResetPasswordActivity.class));
        } else {
            // Go to the UserRegister page
            startActivity(new Intent(this, UserRegister.class));
        }
    }

    /**
     * Used to check if the credentials for username, password are valid or not
     * @return if user is valid user or not
     */
    private boolean CheckLogin() {
        dbHelper = new UserDBHelper(this);
        String username = username_login_text.getText().toString().trim();
        String password = password_login_text.getText().toString().trim();
        // Check if username and password match the database
        return dbHelper.verifyUserData(username, password);
    }
}