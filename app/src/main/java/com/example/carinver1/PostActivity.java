package com.example.carinver1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    ImageButton upload;
    Button post;
    EditText judul,alamat,harga,deskripsi;
    RadioButton mobil,motor;
    DatabaseReference usersRef,postRef;
    StorageReference ImagesRef;
    private Uri imageUri;
    FirebaseAuth mAuth;
    private String saveCurrentDate, saveCurrentTime, postRandomName,downloadUrl, currentUserID;
    private final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initilize();
    }

    private void initilize() {
        upload = (ImageButton) findViewById(R.id.btnUploadImage);
        post = (Button) findViewById(R.id.btnPost);
        judul = (EditText) findViewById(R.id.inputJudulPostingan);
        alamat = (EditText) findViewById(R.id.inputAlamat);
        harga = (EditText) findViewById(R.id.inputHarga);
        deskripsi = (EditText) findViewById(R.id.inputDeskripsiPostingan);
        mobil = (RadioButton) findViewById(R.id.rbMobil);
        motor = (RadioButton) findViewById(R.id.rbMotor);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        postRef = FirebaseDatabase.getInstance().getReference().child("post").child(currentUserID);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePostUser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }
    private void openGallery(){
        Intent galleryIntent =  new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            upload.setImageURI(imageUri);
        }
    }

    private void saveImageToFirebaseStorage()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar1.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        final StorageReference filepath = ImagesRef.child("Post Images").child(imageUri.getLastPathSegment() + postRandomName + ".jpg");
        filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            savePostUser();

                        }
                    });
                    Toast.makeText(PostActivity.this, "Upload Image Success", Toast.LENGTH_SHORT).show();
                }else{
                    String m = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error : "+m, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePostUser() {
        final String jud = judul.getText().toString();
        final String ala = alamat.getText().toString();
        final String har = harga.getText().toString();
        final String desk = deskripsi.getText().toString();
        final String type;
        
        if(mobil.isSelected())
        {
            type = "mobil";
        }
        else if(motor.isSelected())
        {
            type = "motor";
        }
        else
        {
            type = "null";
        }

        if(TextUtils.isEmpty(jud))
        {
            Toast.makeText(this, "Masukkan Judul Post Anda", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(desk))
        {
            Toast.makeText(this, "Masukkan Deskripsi Postingan", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ala))
        {
            Toast.makeText(this, "Masukkan Alamat Anda", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(har))
        {
            Toast.makeText(this, "Masukkan Harga Sewa", Toast.LENGTH_SHORT).show();
        }
        else if (type.equals("null"))
        {
            Toast.makeText(this, "Pilih Kategori Postingan", Toast.LENGTH_SHORT).show();
        }
        else if(imageUri == null)
        {
            Toast.makeText(this, "Masukkan Foto Kendaraan", Toast.LENGTH_SHORT).show();
        }
        else
        {
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String userName = dataSnapshot.child("namalengkap").getValue().toString();
                        String phone = dataSnapshot.child("phonenumber").getValue().toString();

                        HashMap postMap = new HashMap();
                        postMap.put("namalengkap",userName);
                        postMap.put("judul",jud);
                        postMap.put("deskripsi",desk);
                        postMap.put("harga",har);
                        postMap.put("kategori",type);
                        postMap.put("alamat",ala);
                        postMap.put("phonenumber",phone);
                       
                        postRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful())
                                {
                                    sentToHomeActivity();
                                    Toast.makeText(PostActivity.this, "Postingan berhasil dibuat", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(PostActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sentToHomeActivity() {
        Intent homeIntent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sentToLoginActivity();
        }
    }

    private void sentToLoginActivity() {
        Intent loginIntent = new Intent(PostActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
