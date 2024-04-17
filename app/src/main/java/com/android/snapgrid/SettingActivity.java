package com.android.snapgrid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button btnLogout;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        auth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void gotoConfigUser(View view) {
        Intent intent = new Intent(SettingActivity.this, UserConfigActivity.class);
        startActivity(intent);
    }

    public void gotoConfigAccount(View view) {
        Intent intent = new Intent(SettingActivity.this, ConfigAccountActivity.class);
        startActivity(intent);
    }

    public void gotoSignup(View view) {
        Intent intent = new Intent(SettingActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}