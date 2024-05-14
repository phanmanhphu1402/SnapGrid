package com.android.snapgrid;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.snapgrid.databinding.ActivityMainBinding;
import com.android.snapgrid.fragments.Chat_Notify_Fragment;
import com.android.snapgrid.fragments.SearchFragment;
import com.android.snapgrid.fragments.UserInformationFragment;
import com.android.snapgrid.fragments.fragment_home_page;
import com.android.snapgrid.models.User;
import com.android.snapgrid.notifications.Token;
import com.android.snapgrid.service.APIService;
import com.android.snapgrid.service.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    String currentUserID;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new fragment_home_page());


        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }else{
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(MainActivity.this, Login.class);
//            startActivity(intent);
//            finish();
            currentUserID = currentUser.getUid();
        }

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
        if(currentUserID==null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            addFCMToken();
        }


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void addFCMToken(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("fcmToken");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task ->  {
            if(task.isSuccessful()){
                String token = task.getResult();
                userRef.setValue(token);
            }
        });

        //Chat
    }
}