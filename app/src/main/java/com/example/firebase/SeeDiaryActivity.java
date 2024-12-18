package com.example.firebase;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeeDiaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private List<Diary> diaryList;
    private FirebaseFirestore db;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_diary);

        recyclerView = findViewById(R.id.diaryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        diaryList = new ArrayList<>();
        adapter = new DiaryAdapter(diaryList);
        recyclerView.setAdapter(adapter);


        userEmail = getIntent().getStringExtra("USER_EMAIL");
        db = FirebaseFirestore.getInstance();

        if (userEmail != null && !userEmail.isEmpty()) {
            fetchDiaries();
        } else {
            Toast.makeText(this, "User email not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchDiaries() {
        db.collection("diaries")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        diaryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String text = document.getString("text");
                            String timestamp = document.getTimestamp("timestamp").toDate().toString();
                            diaryList.add(new Diary(text, timestamp));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SeeDiaryActivity.this, "Failed to fetch diaries", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
