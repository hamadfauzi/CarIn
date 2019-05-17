package com.example.carinver1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditProfileActivity extends AppCompatActivity {


    ImageButton upload;
    EditText newName,newStatus,newPhoneNumber;
    Button updateProfile;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initialize();

    }

    private void initialize()
    {
        upload = (ImageButton) findViewById(R.id.btnUploadEditProfile);
        newName = (EditText) findViewById(R.id.inputNewProfileName);
        newStatus = (EditText) findViewById(R.id.inputNewStatus);
        newPhoneNumber = (EditText) findViewById(R.id.inputNewPhoneNumber);
        updateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        setToolbar();
    }

    private void setToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.editProfile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
