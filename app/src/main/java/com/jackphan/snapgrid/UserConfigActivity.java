package com.jackphan.snapgrid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserConfigActivity extends AppCompatActivity {

    private ImageButton backBtn;

    private Button saveProfileBtn;

    private ShapeableImageView avatarImageView;

    private Button editAvatarBtn;

    private EditText editFullNameInput;

    private EditText editDescriptionInput;

    private  // Init database

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String currentUserId = currentUser.getUid();

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

    private final static int SELECT_PICTURE = 11;
    private final static int CAPTURE_PICTURE = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);
        // Init find view
        backBtn = findViewById(R.id.back_button);
        saveProfileBtn = findViewById(R.id.save_profile_button);
        avatarImageView = findViewById(R.id.avatar_image_view);
        editAvatarBtn = findViewById(R.id.edit_avatar_button);
        editFullNameInput = findViewById(R.id.edit_full_name_input);
        editDescriptionInput = findViewById(R.id.edit_description_input);
        backBtn.setOnClickListener(v -> {
            finish();
        });


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("profile").getValue(String.class);
                    String userDescription = dataSnapshot.child("description").getValue(String.class);
                    editFullNameInput.setText(userName);
                    editDescriptionInput.setText(userDescription);
                    if(userImageUrl.isEmpty()){
                        Drawable drawable = getResources().getDrawable(R.drawable.user_default);
                        avatarImageView.setImageDrawable(drawable);
                    }else{
                        Picasso.get().load(userImageUrl).placeholder(R.drawable.user_default).into(avatarImageView);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
            }
        });
        // Current user
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> userUpdates = new HashMap<>();
                if (editFullNameInput.getText().toString().length() == 0) {
                    Toast.makeText(UserConfigActivity.this, "Missing name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    userUpdates.put("name", editFullNameInput.getText().toString());
                }
                Log.d(UserConfigActivity.class.getName(), userUpdates.toString());
                userUpdates.put("description", editDescriptionInput.getText().toString());
                editFullNameInput.setInputType(InputType.TYPE_NULL);
                editDescriptionInput.setInputType(InputType.TYPE_NULL);
                userRef.updateChildren(userUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        // Toast notify when complete
                        Toast notify = Toast.makeText(UserConfigActivity.this, "Update profiles failed!", Toast.LENGTH_SHORT);

                        if (error == null) {
                            notify = Toast.makeText(UserConfigActivity.this, "Update profiles success!", Toast.LENGTH_SHORT);
                        }
                        notify.show();
                        editFullNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        editDescriptionInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                });
            }
        });


        editAvatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View uploadPhotoDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_upload_photo, null);
                BottomSheetDialog uploadPhotoDialog = new BottomSheetDialog(UserConfigActivity.this);
                uploadPhotoDialog.setContentView(uploadPhotoDialogView);
                uploadPhotoDialog.setCanceledOnTouchOutside(true);
                AppCompatButton closeUploadPhotoDialogBtn = uploadPhotoDialog.findViewById(R.id.close_btn);
                AppCompatButton chooseFromGalleryDialogBtn = uploadPhotoDialog.findViewById(R.id.choose_from_gallery_btn);
                AppCompatButton capturePhotoDialogBtn = uploadPhotoDialog.findViewById(R.id.capture_photo_btn);
                assert closeUploadPhotoDialogBtn != null;
                closeUploadPhotoDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadPhotoDialog.cancel();
                    }
                });
                assert chooseFromGalleryDialogBtn != null;
                chooseFromGalleryDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent selectPictureIntent = new Intent();
                        selectPictureIntent.setType("image/*");
                        selectPictureIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(selectPictureIntent, "Select Picture"), SELECT_PICTURE);
                        uploadPhotoDialog.cancel();
                    }
                });
                assert capturePhotoDialogBtn != null;
                capturePhotoDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(UserConfigActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAPTURE_PICTURE);
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(UserConfigActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(UserConfigActivity.this, "Feature need camera permission!", Toast.LENGTH_SHORT).show();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                        }
                        uploadPhotoDialog.cancel();
                    }
                });
                uploadPhotoDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            assert data != null;
            Uri uriData = data.getData();
            if (uriData != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserConfigActivity.this.getContentResolver(), uriData);
                    avatarImageView.setImageBitmap(bitmap);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                } catch (IOException e) {
                    Log.d(UserConfigActivity.class.getName(), Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(UserConfigActivity.this, "Read photo is error!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (requestCode == CAPTURE_PICTURE && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            assert extras != null;
            if (extras.get("data") != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                assert imageBitmap != null;
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                avatarImageView.setImageBitmap(imageBitmap);
            }
        }

        if ((requestCode == CAPTURE_PICTURE || requestCode == SELECT_PICTURE) && resultCode == RESULT_OK) {
            byte[] dataUpload = baos.toByteArray();
            String fileUploadName = UUID.randomUUID().toString();
            String fileUploadPath = String.format("images/%s/%s.jpg", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), fileUploadName);
            editAvatarBtn.setEnabled(false);

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference mountainsRef = storageRef.child(fileUploadPath);
            UploadTask uploadTask = mountainsRef.putBytes(dataUpload);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserConfigActivity.this, "Upload avatar failed!", Toast.LENGTH_SHORT).show();
                    editAvatarBtn.setEnabled(true);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(UserConfigActivity.class.getName(), uri.toString() + " ");

                            Map<String, Object> userUpdateProfile = new HashMap<>();
                            userUpdateProfile.put("profile", uri.toString());
                            userRef.updateChildren(userUpdateProfile, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    // Toast notify when complete
                                    Toast notify = Toast.makeText(UserConfigActivity.this, "Update URL avatar failed!", Toast.LENGTH_SHORT);
                                    if (error == null) {
                                        notify = Toast.makeText(UserConfigActivity.this, "Update URL avatar success!", Toast.LENGTH_SHORT);
                                    }
                                    notify.show();
                                    editAvatarBtn.setEnabled(true);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserConfigActivity.this, "Get URL avatar failed!", Toast.LENGTH_SHORT).show();
                            editAvatarBtn.setEnabled(true);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}