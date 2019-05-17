package com.example.carinver1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class EditProfileActivity extends AppCompatActivity {


    ImageButton upload;
    EditText newName,newStatus,newPhoneNumber;
    Button updateProfile;
    Toolbar mToolbar;
    DatabaseReference userRefs;
    FirebaseAuth mAuth;
    StorageReference mStorage;
    ProgressDialog progressDialog;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initialize();
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("namalengkap").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String phone = dataSnapshot.child("phonenumber").getValue().toString();
                    String image = dataSnapshot.child("profileimage").getValue().toString();


                    newName.setText(name);
                    newStatus.setText(status);
                    newPhoneNumber.setText(phone);

                    Picasso.with(EditProfileActivity.this).load(image).placeholder(R.drawable.profile).into(upload);

                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openGallery();
                        }
                    });

                    updateProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateProfileUser();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfileUser() {

        final String name = newName.getText().toString();
        final String status = newStatus.getText().toString();
        final String phone= newPhoneNumber.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Edit Profile");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userRefs.child("namalengkap").setValue(name);
                userRefs.child("phonenumber").setValue(phone);
                userRefs.child("status").setValue(status);
                Toast.makeText(EditProfileActivity.this, "Account has been updated", Toast.LENGTH_SHORT).show();
                sentToProfileActivity();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);
    }

    private void initialize()
    {
        upload = (ImageButton) findViewById(R.id.btnUploadEditProfile);
        newName = (EditText) findViewById(R.id.inputNewProfileName);
        newStatus = (EditText) findViewById(R.id.inputNewStatus);
        progressDialog = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile Image");
        newPhoneNumber = (EditText) findViewById(R.id.inputNewPhoneNumber);
        updateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        setToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRefs = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
    }

    private void setToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.editProfile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final StorageReference filepath = mStorage.child(currentUserID + " .jpg");

                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Uploading ... ");
                progressDialog.setCancelable(true);
                progressDialog.show();

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    userRefs.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfileActivity.this, "Your picture Saved successfully", Toast.LENGTH_SHORT).show();

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfileActivity.this, "Problem occurred while tryng to save your picture..", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Your picture did NOT saved", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void sentToProfileActivity()
    {
        Intent profileIntent = new Intent(EditProfileActivity.this,ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    private void openGallery()
    {
        Intent galleryIntent =  new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),1);
    }

}
