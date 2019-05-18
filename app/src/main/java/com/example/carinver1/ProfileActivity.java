package com.example.carinver1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView profStatus,profName,profTelephone,profPekerjaan;
    CircleImageView profFoto;
    FirebaseAuth mAuth;
    Button editProfile,logout;
    DatabaseReference userRefs;
    private String currentUserID;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            Intent loginIntent = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else
        {
            currentUserID = mAuth.getCurrentUser().getUid();
            userRefs = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
            userRefs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String s = dataSnapshot.child("status").getValue().toString();
                        String n = dataSnapshot.child("namalengkap").getValue().toString();
                        String p = dataSnapshot.child("pekerjaan").getValue().toString();
                        String no = dataSnapshot.child("notelephone").getValue().toString();
                        String image = dataSnapshot.child("profileimage").getValue().toString();

                        profName.setText(n);
                        profPekerjaan.setText(p);
                        profStatus.setText(s);
                        profTelephone.setText(no);

                        Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile).into(profFoto);

                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAuth.signOut();
                                Intent mainnIntent = new Intent(ProfileActivity.this,MainActivity.class);
                                startActivity(mainnIntent);
                                finish();
                            }
                        });
                        editProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent editIntent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                                startActivity(editIntent);

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

    private void initialize() {
        profFoto = (CircleImageView) findViewById(R.id.profileImage);
        profStatus = (TextView) findViewById(R.id.profileStatus);
        profPekerjaan = (TextView) findViewById(R.id.profilePekerjaan);
        profTelephone = (TextView) findViewById(R.id.profileTelp);
        profName = (TextView) findViewById(R.id.profileName);
        editProfile = (Button) findViewById(R.id.btnEditProfile);
        logout = (Button) findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.profileNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.itemProfile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.itemHome:
                        Intent homeIntent = new Intent(ProfileActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;
                    case R.id.itemOrder:
                        Intent orderIntent = new Intent(ProfileActivity.this,OrderActivity.class);
                        startActivity(orderIntent);
                        finish();
                        break;
                    case R.id.itemProfile:
                        break;
                    case R.id.itemUpload:
                        sentToPostActivity();
                        break;
                }
                return true;
            }
        });
    }

    private void sentToPostActivity() {
        Intent postIntent = new Intent(ProfileActivity.this,PostActivity.class);
        startActivity(postIntent);
        finish();
    }
}
