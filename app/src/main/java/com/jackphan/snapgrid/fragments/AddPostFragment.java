package com.jackphan.snapgrid.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.models.Comments;
import com.jackphan.snapgrid.models.Notify;
import com.jackphan.snapgrid.models.Post;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class AddPostFragment extends Fragment {
    String[] items = {"Kì ảo", "Phiêu lưu", "Chilling", "Sắc đẹp", "Hành động", "Xã hội", "Hoạt hình", "Buồn", "Vui", "Hỗn loạn", "Sắc màu", "Cảm hứng", "Giấc mơ"};

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
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Post");
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

    private String image;
    private String title;
    private String content;
    private String tag;
    private String idPost;

    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_add_post, container, false);
        postImage = rootview.findViewById(R.id.postImage);
        postImage = rootview.findViewById(R.id.postImage);
        btnAddPost = rootview.findViewById(R.id.btnAddPost);
        editTitle = rootview.findViewById(R.id.editTitle);
        editContent = rootview.findViewById(R.id.editContent);
        btnDelete = rootview.findViewById(R.id.btnDelete);
        btnReup = rootview.findViewById(R.id.btnReup);
        heading = rootview.findViewById(R.id.createText);
        autoCompleteTxt = rootview.findViewById(R.id.auto_complete_txt);

        if (getArguments() != null) {
            image = getArguments().getString("dataImage");
            title = getArguments().getString("dataTitle");
            content = getArguments().getString("dataContent");
            tag = getArguments().getString("dataTag");
            idPost = getArguments().getString("dataIdPost");

            if (image != null) {
                editTitle.setText(title);
                editContent.setText(content);
                autoCompleteTxt.setText(tag);
                btnAddPost.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnReup.setVisibility(View.VISIBLE);
                heading.setText("Edit post");
            }
        }

        // launcher chọn ảnh
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        uriImage = data.getData();
                        postImage.setImageURI(uriImage);
                    } else {
                        Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        btnReup.setOnClickListener(view -> {
            if (uriImage != null) {
                reUpLoadToFireBase(uriImage, image, idPost);
            } else {
                Toast.makeText(requireContext(), "Please select an Image", Toast.LENGTH_SHORT).show();
            }
        });
        postImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent();
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });
        btnAddPost.setOnClickListener(view -> {
            if (uriImage != null) {
                uploadToFireBase(uriImage);
            } else {
                Toast.makeText(requireContext(), "Please select Image", Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(view -> {
            deletePost(image, idPost);
        });
        autoCompleteTxt = rootview.findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(requireContext(), R.layout.item_tag, items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
        });
        return rootview;
    }

    private void deletePost(String image, String idPost) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference1 = storage.getReferenceFromUrl(image);
        storageReference1.delete().addOnSuccessListener(unused -> {
            databaseReference.child(idPost).removeValue();
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void reUpLoadToFireBase(Uri uriImage, String image, String idPost) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference1 = storage.getReferenceFromUrl(image);
        storageReference1.delete().addOnSuccessListener(unused -> {
            databaseReference.child(idPost).removeValue();
            uploadToFireBase(uriImage);
        });
    }

    private void uploadToFireBase(Uri uriImage) {
        showLoading();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            hideLoading();
            return;
        }
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(dateFormatter);
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String userPostId = currentUser.getUid();
        String tag = autoCompleteTxt.getText().toString();
        String datePost = formattedDate;

        String ext = getFileExtension(uriImage);
        if (ext == null) ext = "jpg"; // fallback
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + ext);

        ArrayList<Comments> commentsArrayList = new ArrayList<>();
        System.out.println(uriImage);
        System.out.println(imageReference);
        imageReference.putFile(uriImage).addOnSuccessListener(taskSnapshot -> {
            imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String key = databaseReference.push().getKey();
                Post post = new Post(key, userPostId, content, formattedDate, 0, commentsArrayList.size(), commentsArrayList, uri.toString(), title, tag);
                databaseReference.child(key).setValue(post);
                createNotify(userPostId, formattedDate, key);
                hideLoading();
                Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new fragment_home_page()).commit();
            }).addOnFailureListener(e -> {
                hideLoading();
                Toast.makeText(requireContext(), "Failed to upload file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    private String getFileExtension(Uri uriImage) {
        if (uriImage == null) return null;
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = contentResolver.getType(uriImage);
        return type != null ? mime.getExtensionFromMimeType(type) : null;
    }

    private void createNotify(String userPostId, String formattedDate, String key) {
        DatabaseReference notifyRef = FirebaseDatabase.getInstance().getReference("Notifies");
        String key1 = notifyRef.push().getKey();
        Notify notify = new Notify(userPostId, "Vừa mới đăng bài viết", formattedDate, key);
        notifyRef.child(key1).setValue(notify);
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    private void showLoading() {
        loadingFragment = new LoadingFragment();
        loadingFragment.show(requireActivity().getSupportFragmentManager(), "loading");
    }

}