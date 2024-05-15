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
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.snapgrid.databinding.ActivityMainBinding;
import com.android.snapgrid.fragments.AddPostFragment;
import com.android.snapgrid.fragments.LoadingFragment;
import com.android.snapgrid.models.Comments;
import com.android.snapgrid.models.Notify;
import com.android.snapgrid.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostAddingActivity extends AppCompatActivity {
    String[] items = {"Kì ảo", "Phiêu lưu", "Chilling",
            "Sắc đẹp", "Hành động", "Xã hội",
            "Hoạt hình", "Buồn", "Vui",
            "Hỗn loạn", "Sắc màu", "Cảm hứng",
            "Giấc mơ", "Game", "Thực tế",
            "Thiên nhiên", "Đời thường", "Học đường"};
    private LoadingFragment loadingFragment;
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    ShapeableImageView btnDelete;

    TextView heading;
    Uri uriImage;
    ImageView postImage;
    Button btnAddPost, btnReup;
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
        btnDelete = findViewById(R.id.btnDelete);
        btnReup = findViewById(R.id.btnReup);
        heading = findViewById(R.id.createText);
        autoCompleteTxt = (AutoCompleteTextView) findViewById(R.id.auto_complete_txt);
        String image = getIntent().getStringExtra("dataImage");
        String title = getIntent().getStringExtra("dataTitle");
        String content = getIntent().getStringExtra("dataContent");
        String tag = getIntent().getStringExtra("dataTag");
        String idPost = getIntent().getStringExtra("dataIdPost");
        if(image != null) {
//            Picasso.get().load(image).into(postImage);
            editTitle.setText(title);
            editContent.setText(content);
            autoCompleteTxt.setText(tag);
            btnAddPost.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnReup.setVisibility(View.VISIBLE);
            heading.setText("Edit post");

        }
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
        btnReup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                reUpLoadToFireBase(uriImage);
                if(uriImage != null){
                    reUpLoadToFireBase(uriImage, image, idPost);
                }else{
                    Toast.makeText(getApplicationContext(),"Please select an Image",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(image, idPost);
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

    private void deletePost(String image, String idPost) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference1 = storage.getReferenceFromUrl(image);
        storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child(idPost).removeValue();
                Toast.makeText(PostAddingActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void reUpLoadToFireBase(Uri uri, String image, String idPost) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference1 = storage.getReferenceFromUrl(image);
        storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child(idPost).removeValue();
            }
        });
        uploadToFireBase(uri);
    }

    private void createNotify(String idUser, String currentDate, String idPost){
        DatabaseReference notifyRef = FirebaseDatabase.getInstance().getReference("Notifies");
        String key = notifyRef.push().getKey();
        Notify notify = new Notify(idUser, "Vừa mới đăng bài viết", currentDate, idPost);
        notifyRef.child(key).setValue(notify);
    }

    private void uploadToFireBase(Uri uri) {
        showLoading();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(dateFormatter);
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String userPostId = currentUser.getUid();
        String tag = autoCompleteTxt.getText().toString();
        String datePost = formattedDate;
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        ArrayList<Comments> commentsArrayList = new ArrayList<>();
        System.out.println(uri);
        System.out.println(imageReference.toString());
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String key = databaseReference.push().getKey();
                        Post post = new Post(key, userPostId, content, formattedDate, 0,commentsArrayList.size(), commentsArrayList,uri.toString(), title, tag);
                        databaseReference.child(key).setValue(post);
                        createNotify(userPostId, formattedDate, key);
                        hideLoading();
                        Toast.makeText(PostAddingActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostAddingActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(PostAddingActivity.this, "Failed to upload file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void showLoading() {
        loadingFragment = new LoadingFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }
    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
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