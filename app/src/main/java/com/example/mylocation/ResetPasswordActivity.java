package com.example.mylocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class for resetting password
 */
public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText password_text1;
    private EditText password_text2;
    private Button next;

    /**
     * Used to initialize the activity for reset password
     * @param savedInstanceState - current instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        password_text1 = (EditText) findViewById(R.id.password_reset_text);
        password_text2 = (EditText) findViewById(R.id.password_reset_text_2);
        next = (Button) findViewById(R.id.password_reset_confirm);
        next.setOnClickListener(this);
    }

    /**
     * Updates database with updated valid password. TODO can be refactored in future to properly authenticate the user before resetting password
     * @param v - current view
     */
    @Override
    public void onClick(View v) {
        if (checkPassword()) {
            // If valid, update the database and return to the login page
            //TODO: update the database with new passwords and go to the login page
            startActivity(new Intent(this, UserLogin.class));
        } else {
            Toast.makeText(this, "Please your password!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @return true if password entered is properly validated otherwise false
     */
    private boolean checkPassword() {
        String password1 = password_text1.getText().toString().trim();
        String password2 = password_text2.getText().toString().trim();
        if (password1.isEmpty()) {
            password_text1.setError("DO NOT Enter An Empty Password!");
            password_text2.requestFocus();
            return false;
        }

        if (password1.length() < 6) {
            password_text1.setError(("The Minimum Length For Password Is 6!"));
            password_text1.requestFocus();
            return false;
        }

        if (password2.isEmpty()) {
            password_text2.setError("Please Confirm Your Password!");
            password_text2.requestFocus();
            return false;
        }

        if (!password1.equals(password2)) {
            password_text2.setError("Please Enter The Same Password!");
            password_text2.requestFocus();
            return false;
        }

        return true;
    }
}