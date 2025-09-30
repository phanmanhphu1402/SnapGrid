package com.jackphan.snapgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Splash extends AppCompatActivity {
    private static final String TAG = "Activity_Splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_snapgrid);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.motionlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Kiểm tra trạng thái trong SharedPreferences
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser != null){
                String userId = currentUser.getUid();
                Log.d(TAG, "User đã đăng nhập: " + userId);
                Intent intent = new Intent(Activity_Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d(TAG, "User chưa đăng nhập");
                if (isFirstRun) {
                    Log.d(TAG, "Lần chạy đầu tiên -> NavigationActivity");
                    startActivity(new Intent(Activity_Splash.this, NavigationActivity.class));
                    prefs.edit().putBoolean("isFirstRun", false).apply();
                } else {
                    Log.d(TAG, "Không phải lần chạy đầu tiên -> MainActivity");
                    startActivity(new Intent(Activity_Splash.this, MainActivity.class));
                }
                finish();
            }
        }, 2000);

    }
}