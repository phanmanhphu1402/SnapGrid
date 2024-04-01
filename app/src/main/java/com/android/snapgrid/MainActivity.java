package com.android.snapgrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.snapgrid.databinding.ActivityMainBinding;
import com.android.snapgrid.fragments.DetailPostFragment;
import com.android.snapgrid.fragments.UserInformationFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new UserInformationFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.home){
                startActivity(new Intent(MainActivity.this, HomePageActivity.class));
            }
            if(id == R.id.search){
                replaceFragment(new DetailPostFragment());
            }
            if(id == R.id.createPost){
                replaceFragment(new DetailPostFragment());
            }
            if(id == R.id.notifyAndChat){
                replaceFragment(new DetailPostFragment());
            }
            if(id == R.id.personality){
                replaceFragment(new UserInformationFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
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