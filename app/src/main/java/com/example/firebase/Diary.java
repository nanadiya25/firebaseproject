package com.example.firebase;

public class Diary {
    private String text;
    private String timestamp;

    public Diary() {
        // Empty constructor diperlukan oleh Firestore
    }

    public Diary(String text, String timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
