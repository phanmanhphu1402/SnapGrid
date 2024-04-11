package com.android.snapgrid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.snapgrid.databinding.ActivityMainBinding;
import com.android.snapgrid.fragments.AddPostFragment;
import com.android.snapgrid.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PostAddingActivity extends AppCompatActivity {
    String[] items = {"Kì ảo", "Phiêu lưu", "Chilling", "Sắc đẹp", "Hành động", "Xã hội", "Hoạt hình", "Buồn", "Vui", "Hỗn loạn", "Sắc màu", "Cảm hứng", "Giấc mơ"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    ActivityMainBinding binding = null;
    ImageButton btnAddImage;

    Uri uriImage;
    ImageView postImage;
    Button btnAddPost;
    EditText editTitle, editContent;
    FirebaseAuth mAuth;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("Post");
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_adding);
        postImage = findViewById(R.id.postImage);
        btnAddPost = findViewById(R.id.btnAddPost);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == Activity.RESULT_OK){
                            Intent data = o.getData();
                            uriImage = data.getData();
                            postImage.setImageURI(uriImage);
                        }else{
                            Toast.makeText(PostAddingActivity.this,"No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uriImage != null){
                    uploadToFireBase(uriImage);
                }else{
                    Toast.makeText(PostAddingActivity.this, "Please select Image",Toast.LENGTH_SHORT).show();
                }
            }
        });
        autoCompleteTxt = (AutoCompleteTextView) findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this,R.layout.item_tag,items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    private void uploadToFireBase(Uri uri) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        LocalDate currentDate = LocalDate.now();
        System.out.println("Current Date: " + currentDate);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(dateFormatter);
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String userPostId = currentUser.getUid();
        String datePost = formattedDate;
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Post post = new Post(1, userPostId, content, formattedDate,0,0,uri.toString(), title);
                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(post);
                        Toast.makeText(PostAddingActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostAddingActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data != null && data.getData() != null){
            Uri imageUri = data.getData();

        }
    }
}