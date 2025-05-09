package com.example.exp;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exp.DatabaseHelper;
import com.example.exp.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String pass = passwordEditText.getText().toString().trim();
                String confirm = confirmPasswordEditText.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper db = new DatabaseHelper(RegisterActivity.this);
                    long result = db.addUser(name, email, pass, null);
                    if (result != -1) {
                        Toast.makeText(RegisterActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
