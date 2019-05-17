package com.example.carinver1;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    CircleImageView setupImage;
    Button createAccount;
    EditText phoneNumber, namaLengkap;
    private String currentUserID;
    FirebaseAuth mAuth;
    private String isiName,isiPhone;
    StorageReference mStorage;
    ProgressDialog progressDialog;
    DatabaseReference userRef;
    Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        initialize();
        progressDialog = new ProgressDialog(this);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingUserInformation();
            }
        });
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent =  new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),1);
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("profileimage")){
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(SetupActivity.this).load(image).placeholder(R.drawable.profile).into(setupImage);
                    }else{
                        Toast.makeText(SetupActivity.this, "Please Select profile image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void savingUserInformation()
    {
        final String name = namaLengkap.getText().toString();
        final String phne = phoneNumber.getText().toString();

        if(resultUri == null)
        {
            Toast.makeText(this, "Masukkan foto anda terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap userMap = new HashMap();
            userMap.put("namalengkap",name);
            userMap.put("notelephone",phne);
            userMap.put("pekerjaan","belum bekerja");
            userMap.put("status","Avaiable");
            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SetupActivity.this, "Berhasil buat akun", Toast.LENGTH_SHORT).show();
                        sentToHomeActivity();
                    }
                    else
                    {
                        Toast.makeText(SetupActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sentToHomeActivity() {
        Intent homeIntent = new Intent(SetupActivity.this,MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1).start(this);

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                resultUri = result.getUri();
                final StorageReference filepath = mStorage.child(currentUserID + " .jpg");

                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Uploading ... ");
                progressDialog.setCancelable(true);
                progressDialog.show();

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    userRef.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(SetupActivity.this,"Your picture Saved successfully",Toast.LENGTH_SHORT) .show();

                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(SetupActivity.this,"Problem occurred while tryng to save your picture..",Toast.LENGTH_SHORT) .show();
                                            }
                                        }
                                    });
                                }
                            });
                        }else{
                            Toast.makeText(SetupActivity.this,"Your picture did NOT saved",Toast.LENGTH_SHORT) .show();

                        }
                    }
                });
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }


    private void initialize() {
        setupImage = (CircleImageView) findViewById(R.id.setupprofileImage);
        createAccount = (Button) findViewById(R.id.btnCreateAccount);
        phoneNumber = (EditText) findViewById(R.id.setupNoTelp);
        namaLengkap = (EditText) findViewById(R.id.setupNamaLengkap);
        mAuth = FirebaseAuth.getInstance();
        isiName = getIntent().getExtras().get("name").toString();
        isiPhone = getIntent().getExtras().get("phone").toString();
        namaLengkap.setText(isiName);
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile Image");
        phoneNumber.setText(isiPhone);
        namaLengkap.setEnabled(false);
        phoneNumber.setEnabled(false);
        currentUserID = mAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(this);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

    }
}
