package com.android.snapgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_snapgrid);
        if (getIntent().getExtras()!=null){
            String userId = getIntent().getExtras().getString("userId");
            System.out.println("It fucking Work");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("Users");
            usersRef.child(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(Activity_Splash.this, ChatToChatActivity.class);
                    intent.putExtra("hisName", "Tang Thuy");
                    intent.putExtra("hisUid", userId);
                    intent.putExtra("hisImage", userId);
                    startActivity(intent);
                }
            });
        }else{
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.motionlayout), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            // Kiểm tra trạng thái trong SharedPreferences
            SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFirstRun){
                        startActivity(new Intent(Activity_Splash.this, NavigationActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(Activity_Splash.this, MainActivity.class));
                        finish();
                    }
                }
            },2000);
        }




    }
}