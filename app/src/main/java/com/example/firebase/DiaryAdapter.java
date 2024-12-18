package com.example.firebase;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<Diary> diaryList;

    public DiaryAdapter(List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        Diary diary = diaryList.get(position);
        holder.textTitle.setText(diary.getText());
        holder.textTimestamp.setText(diary.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textTimestamp;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(android.R.id.text1);
            textTimestamp = itemView.findViewById(android.R.id.text2);
        }
    }
}

