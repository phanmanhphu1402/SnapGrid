package com.android.snapgrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtFullName, edtEmail, edtPassword;
    private Button btnRegister;
    private ImageView signUpButton; // Tham chiếu đến ImageView mũi tên


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Khởi tạo view
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        signUpButton = findViewById(R.id.SignUpButton);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng ký người dùng
                registerUser();
            }

        });

        // Thiết lập sự kiện chuyển hướng sang trang đăng nhập khi nhấn nút mũi tên
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void registerUser() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra điều kiện đầu vào
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tiến hành đăng ký người dùng với Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công, lấy UID và lưu thông tin người dùng vào Firestore và Realtime Database
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid(); // Lấy UID

                                // Tạo thông tin người dùng cho Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> userFirestore = new HashMap<>();
                                userFirestore.put("FullName", fullName);
                                userFirestore.put("Email", email);
                                userFirestore.put("ID", uid);
                                userFirestore.put("Followers", 0);
                                userFirestore.put("Followings", 0);
                                userFirestore.put("Avatar", "");
                                userFirestore.put("Description", "");
                                userFirestore.put("DateJoin", 0);

                                // Lưu thông tin người dùng vào Firestore
                                db.collection("User").document(uid).set(userFirestore)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignUpActivity.this, "User information saved to Firestore.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpActivity.this, "Error saving user information to Firestore.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                // Tạo thông tin người dùng cho Realtime Database
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference usersRef = database.getReference("users");
                                Map<String, Object> userRealtime = new HashMap<>();
                                userRealtime.put("email", email);
                                userRealtime.put("name", fullName);
                                userRealtime.put("followings", 0);
                                userRealtime.put("profile", "");

                                // Lưu thông tin người dùng vào Realtime Database
                                usersRef.child(uid).setValue(userRealtime)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignUpActivity.this, "User information saved to Realtime Database.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpActivity.this, "Error saving user information to Realtime Database.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // Xử lý lỗi khi đăng ký không thành công
                            Toast.makeText(SignUpActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Hàm chuyển hướng sang LoginActivity
    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
