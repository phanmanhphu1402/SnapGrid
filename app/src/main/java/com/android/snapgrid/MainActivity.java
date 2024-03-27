package com.android.snapgrid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDetailPost(View view) {
        Intent intent = new Intent(this, DetailPost.class);
        startActivity(intent);
    }

    public void goToUser(View view) {
        Intent intent = new Intent(this, UserInformation.class);
        startActivity(intent);
    }

    public void gotoNavigation(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    public void gotoSlider(View view) {
        Intent intent = new Intent(this, DetailPost.class);
        startActivity(intent);
    }

    public void gotoSignIn(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void gotoSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}