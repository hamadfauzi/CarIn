 package com.example.carinver1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

     private void initialize()
     {
         mAuth = FirebaseAuth.getInstance();
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

     private void sentToLoginActivity()
     {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
     }

 }
