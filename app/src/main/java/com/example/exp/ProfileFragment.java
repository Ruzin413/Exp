package com.example.exp;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.ByteArrayOutputStream;
public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView profileName, forgotPassword, changeImageText;
    private ImageView profileImage;
    public Button Logoff;
    private DatabaseHelper dbHelper;
    private String userName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profileName);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        changeImageText = view.findViewById(R.id.changeImageText);
        profileImage = view.findViewById(R.id.profileImage);
        Logoff = view.findViewById(R.id.Logoff);
        dbHelper = new DatabaseHelper(requireContext());
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            userName = activity.loggedInUserName;
            profileName.setText("Welcome, " + userName + "!");
            loadProfileImage(userName);
        }
        forgotPassword.setOnClickListener(v -> showResetPasswordDialog());
        changeImageText.setOnClickListener(v -> openImagePicker());
        Logoff.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("data", "signup");
            startActivity(intent);
            requireActivity().finish();
        });
        return view;
    }
    private void loadProfileImage(String userName) {
        byte[] imageData = dbHelper.getUserImageByUsername(userName);
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            profileImage.setImageBitmap(bitmap);
        } else {
            // Set the default drawable if no image is stored in DB
            profileImage.setImageResource(R.drawable.profile); // your default image
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                profileImage.setImageBitmap(bitmap);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                boolean updated = dbHelper.updateUserImage(userName, imageBytes);
                if (updated) {
                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
