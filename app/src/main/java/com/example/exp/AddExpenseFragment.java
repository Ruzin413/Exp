package com.example.exp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText nameEditText, amountEditText;
    private Button addButton;
    private byte[] imageBytes;
    private String userName;
    private DatabaseHelper db;

    public static AddExpenseFragment newInstance(String userName) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putString("username", userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userName = getArguments() != null ? getArguments().getString("username") : "Unknown";
        db = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        nameEditText = view.findViewById(R.id.expenseNameEditText);
        amountEditText = view.findViewById(R.id.expenseAmountEditText);
        imageView = view.findViewById(R.id.expenseImageView);
        addButton = view.findViewById(R.id.addExpenseButton);

        imageView.setOnClickListener(v -> openImagePicker());

        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String amountStr = amountEditText.getText().toString();
            if (name.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amountStr);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            boolean inserted = db.insertExpense(name, amount, date, imageBytes, userName);
            if (inserted) {
                Toast.makeText(getContext(), "Expense Added", Toast.LENGTH_SHORT).show();
                nameEditText.setText("");
                amountEditText.setText("");
                imageView.setImageResource(R.drawable.img_1); // Replace with your default image
            } else {
                Toast.makeText(getContext(), "Insert Failed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                // Check size before decoding
                int maxSizeInBytes = 1024 * 1024; // 1MB
                int imageSize = inputStream.available();
                if (imageSize > maxSizeInBytes) {
                    Toast.makeText(getContext(), "Image too large! Please select an image under 1MB.", Toast.LENGTH_LONG).show();
                    imageView.setImageResource(R.drawable.img); // reset image
                    imageBytes = null;
                    return;
                }

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

