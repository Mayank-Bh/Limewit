package com.example.bhotika.limewit;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Submit extends AppCompatActivity {

    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private EditText txtImageName;
    private Button gallery,camera,store,show;
    private Uri imgUri;
    public static final int GALLERY_INTENT = 2;
    public static final String STORAGE_PATH = "Submit/";
    public static final String DATABASE_PATH = "Submit";
    public static final int REQUEST_CODE=1234;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Photos");
        mStorage = FirebaseStorage.getInstance().getReference();
        // mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = mAuth.getCurrentUser();
        email = currUser.getEmail();

        imageView = (ImageView) findViewById(R.id.image);
        gallery = (Button) findViewById(R.id.btnGallery);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

        final Uri uri = data.getData();

        StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment() + email);
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageView.setImageURI(uri);
                Toast.makeText(Submit.this, "Photo Upload Successful !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Submit.this, "Phot upload Failed !", Toast.LENGTH_SHORT).show();
            }
        });

/*
            if (imgUri!=null){
                final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
                dialog.setTitle("Uploading Image");
                dialog.show();

                StorageReference ref = mStorage.child("Photos").child(imgUri.getLastPathSegment() + email); ;

                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                        ImageUpload imageUpload = new ImageUpload(txtImageName.getText().toString(),taskSnapshot.getDownloadUrl().toString());
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(imageUpload);
                        imageView.setImageBitmap(null);
                        txtImageName.setText("");

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        })

                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                double Progess = (100 * taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();
                                dialog.setMessage("Uploaded " + (int)Progess + "%");

                            }


                        });
            }

            else {
                Toast.makeText(getApplicationContext(),"Please Select Image",Toast.LENGTH_SHORT).show();

            }*/
 }
}

//
//    public String getImageExt(Uri uri){
//        ContentResolver contentResolver = getContext().getApplicationContext().getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
//


}
