package com.android.snapgrid;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

<<<<<<< HEAD
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

=======
>>>>>>> f4dde289555e3eb4cb9c098f31a09b678717f67f
import com.android.snapgrid.databinding.ActivityMainBinding;
import com.android.snapgrid.fragments.Chat_Notify_Fragment;
import com.android.snapgrid.fragments.SearchFragment;
import com.android.snapgrid.fragments.UserInformationFragment;
import com.android.snapgrid.fragments.fragment_home_page;

public class MainActivity extends AppCompatActivity {



    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new fragment_home_page());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.home){
                replaceFragment(new fragment_home_page());
            }
            if(id == R.id.search){
                replaceFragment(new SearchFragment());
            }
            if(id == R.id.createPost){
//                replaceFragment(new AddPostFragment());
                Intent intent = new Intent(this, PostAddingActivity.class);
                startActivity(intent);
            }
            if(id == R.id.notifyAndChat){
                replaceFragment(new Chat_Notify_Fragment());
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

    //Chat



    //Chat
}