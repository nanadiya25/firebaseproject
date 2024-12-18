package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteDiaryActivity extends AppCompatActivity {

    private EditText diaryText;
    private FirebaseFirestore db;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        // Ambil email dari Intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User email not found!", Toast.LENGTH_SHORT).show();
            finish(); // Kembali jika email tidak ditemukan
            return;
        }

        // Inisialisasi Firebase
        db = FirebaseFirestore.getInstance();

        diaryText = findViewById(R.id.diary_text);
        Button buttonSaveDiary = findViewById(R.id.button_save_diary);

        // Tombol untuk menyimpan diary
        buttonSaveDiary.setOnClickListener(view -> saveDiary());
    }

    private void saveDiary() {
        String text = diaryText.getText().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, "How are you today?", Toast.LENGTH_SHORT).show();
            return;
        }

        saveDiaryToFirestore(text);
    }

    private void saveDiaryToFirestore(String text) {
        Map<String, Object> diary = new HashMap<>();
        diary.put("text", text);
        diary.put("timestamp", new com.google.firebase.Timestamp(new Date()));
        diary.put("email", userEmail);

        db.collection("diaries")
                .add(diary)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(WriteDiaryActivity.this, "Diary saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(WriteDiaryActivity.this, "Failed to save diary", Toast.LENGTH_SHORT).show());
    }
}