package com.example.exp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText, newPasswordEditText;
    private  TextView backtologin;
    private Button resetPasswordButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        db = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.forgotEmailEditText);
        newPasswordEditText = findViewById(R.id.forgotNewPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backtologin = findViewById(R.id.backToLoginText);
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            if (db.updatePassword(email, newPassword)) {
                Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show();
                finish(); // Return to LoginActivity
            } else {
                Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
            }
        });
        backtologin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
