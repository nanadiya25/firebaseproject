package com.example.firebase;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.databinding.ActivityHomePageBinding;

public class HomePageActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityHomePageBinding binding;

    // Variabel sensor
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate;
    private float last_x, last_y, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ambil email dari Intent
        String email = getIntent().getStringExtra("USER_EMAIL");

        // Set teks email di TextView
        if (email != null && !email.isEmpty()) {
            binding.emailTextView.setText("Welcome, " + email);
        } else {
            binding.emailTextView.setText("Welcome, Guest"); // Penanganan jika email kosong
        }

        // Tombol untuk menulis diary
        binding.buttonWriteDiary.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, WriteDiaryActivity.class);
            intent.putExtra("USER_EMAIL", email); // Kirim email ke Write_Diary_Activity
            startActivity(intent);
        });

        // Tombol untuk melihat diary
        binding.buttonSeeDiary.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, SeeDiaryActivity.class);
            intent.putExtra("USER_EMAIL", email);
            startActivity(intent);
        });

        // Tombol untuk kamera
        binding.btncamera.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, cameraActivity.class);
            intent.putExtra("USER_EMAIL", email);
            startActivity(intent);
        });

        // Inisialisasi sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometer != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > 100) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // Shake terdeteksi, tampilkan notifikasi
                    Toast.makeText(this, "Shake detected!", Toast.LENGTH_SHORT).show();
                    binding.emailTextView.setText("Shake Detected!");

                    // Tampilkan gambar shake memenuhi layar
                    binding.imageShakeDetected.setVisibility(View.VISIBLE);

                    // Sembunyikan gambar setelah 5 detik
                    binding.imageShakeDetected.postDelayed(() ->
                            binding.imageShakeDetected.setVisibility(View.INVISIBLE), 5000);
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak diperlukan untuk fitur ini
    }
}
