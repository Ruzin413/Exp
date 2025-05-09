package com.example.exp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private TextView profileName, forgotPassword;
    public Button Logoff;
    private DatabaseHelper dbHelper;
    private String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        Logoff = view.findViewById(R.id.Logoff);
        dbHelper = new DatabaseHelper(requireContext());

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            userName = activity.loggedInUserName;
            profileName.setText("Welcome, " + userName + "!");
        }

        forgotPassword.setOnClickListener(v -> showResetPasswordDialog());
        Logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("data","signup");
                startActivity(intent);
            }
        });
        return view;
    }
    private void showResetPasswordDialog() {
        EditText inputEmail = new EditText(getContext());
        inputEmail.setHint("Enter your email");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reset Password")
                .setView(inputEmail)
                .setPositiveButton("Next", (dialog, which) -> {
                    String email = inputEmail.getText().toString().trim();
                    if (!email.isEmpty() && dbHelper.getNameByEmail(email) != null) {
                        showNewPasswordDialog(email);
                    } else {
                        Toast.makeText(getContext(), "Email not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showNewPasswordDialog(String email) {
        EditText newPasswordInput = new EditText(getContext());
        newPasswordInput.setHint("Enter new password");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set New Password")
                .setView(newPasswordInput)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newPassword = newPasswordInput.getText().toString().trim();
                    if (!newPassword.isEmpty()) {
                        boolean updated = dbHelper.updatePassword(email, newPassword);
                        if (updated) {
                            Toast.makeText(getContext(), "Password updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error updating password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
