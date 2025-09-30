package com.jackphan.snapgrid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ConfigAccountActivity extends AppCompatActivity {
    ImageButton btnBack;
    EditText editTextEmail, editTextPassword, editTextNewPassword;
    Button btnUpdateAccount;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;
    DatabaseReference userRef;
    TextInputLayout layoutPassword, layoutEmail, layoutNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_account);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnBack = findViewById(R.id.btnBack_Config);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutNewPassword = findViewById(R.id.layoutNewPassword);
        btnBack.setOnClickListener(view -> finish());

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    layoutEmail.setError(null); // layoutEmail là TextInputLayout
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    layoutPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    layoutNewPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Người dùng chưa đang nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentUserId = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    editTextEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnUpdateAccount.setOnClickListener(view -> {
            UpdateAccount();
        });

    }

    private void UpdateAccount() {
        String newEmail = editTextEmail.getText().toString().trim();
        String currentPassword = editTextPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        if (newEmail.isEmpty()) {
            layoutEmail.setError("Vui lòng nhập email");
            editTextEmail.requestFocus();
            return;
        }
        if (currentPassword.isEmpty()) {
            layoutPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            editTextPassword.requestFocus();
            return;
        }
        if (newPassword.isEmpty()) {
            layoutNewPassword.setError("Vui lòng nhập mật khẩu mới");
            editTextNewPassword.requestFocus();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(currentUser.getEmail()), currentPassword);
        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!newEmail.equals(currentUser.getEmail())) {
                    currentUser.updateEmail(newEmail).addOnCompleteListener(emailTask -> {
                        if (emailTask.isSuccessful()) {
                            userRef.child("email").setValue(newEmail);
                            Toast.makeText(ConfigAccountActivity.this, "Cập nhật email thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ConfigAccountActivity.this, "Cập nhật email thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (!TextUtils.isEmpty(newPassword)) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener(passwordTask -> {
                        if (passwordTask.isSuccessful()) {
                            userRef.child("password").setValue(newPassword);
                            Toast.makeText(ConfigAccountActivity.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Lỗi đổi mật khẩu: " + passwordTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Xác thực thất bại: mật khẩu hiện tại không đúng ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}