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
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPassword;
    private Button btnRegister;
    private ImageView signUpButton; // Thêm biến này để tham chiếu đến ImageView
    private TextView Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Khởi tạo view
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        signUpButton = findViewById(R.id.SignUpButton); // Khởi tạo ImageView bằng findViewById
        Login = findViewById(R.id.Login); // Thêm dòng này

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng ký người dùng
                registerUser();
            }
        });


        // Thiết lập OnClickListener cho signUpButton
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để chuyển từ SignUpActivity sang LoginActivity
                Intent intent = new Intent(SignUpActivity.this, Login.class);
                startActivity(intent);
                finish(); // Kết thúc SignUpActivity để người dùng không quay lại khi bấm nút back
            }
        });


        // Đặt OnClickListener cho TextView
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến LoginActivity
                Intent intent = new Intent(SignUpActivity.this, Login.class);
                startActivity(intent);
                finish(); // Kết thúc SignUpActivity để người dùng không quay lại khi bấm nút back
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
                            // Đăng ký thành công, lấy UID và lưu thông tin người dùng vào Firestore
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser != null) {
                                ArrayList<String> followingList = new ArrayList<String>();
                                ArrayList<String> followerList = new ArrayList<String>();
                                LocalDate currentDate = LocalDate.now();
                                String uid = firebaseUser.getUid(); // Lấy UID
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("FullName", fullName);
                                user.put("Email", email);
                                user.put("ID", uid); // UID từ Firebase Auth được lưu như là ID trong document
                                user.put("Followers", followerList);
                                user.put("Followings", followingList);
                                user.put("Avatar", "");
                                user.put("Decription", "");
                                user.put("DateJoin", currentDate);
                                // Không nên lưu mật khẩu. Bỏ dòng này:
                                user.put("Password", password);

                                // Lưu thông tin người dùng vào Firestore
                                db.collection("User").document(uid).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignUpActivity.this, "User information saved to Firestore.", Toast.LENGTH_SHORT).show();
                                                // Chuyển hướng người dùng hoặc cập nhật giao diện người dùng
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpActivity.this, "Error saving user information.", Toast.LENGTH_SHORT).show();
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

    private void saveUserInformation(String fullName, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy UID của người dùng hiện tại

        Map<String, Object> user = new HashMap<>();
        user.put("FullName", fullName);
        user.put("Email", email);

        // Sử dụng UID làm document ID cho collection User
        db.collection("User").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        // Chuyển đến activity khác hoặc cập nhật UI tại đây
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}