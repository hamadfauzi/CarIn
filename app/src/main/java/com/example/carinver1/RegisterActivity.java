package com.example.carinver1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button toLogin, register;
    FirebaseAuth mAuth;
    EditText name,email,password,noTelp;
    private String currentUserID;
    DatabaseReference usersRefs;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize()
    {
        toLogin = (Button) findViewById(R.id.btnToLoginActivity);
        register = (Button) findViewById(R.id.btnRegister);
        name = (EditText) findViewById(R.id.daftarUsername);
        email = (EditText) findViewById(R.id.daftarEmail);
        password = (EditText) findViewById(R.id.daftarPassword);
        noTelp = (EditText) findViewById(R.id.daftarNoTelp);
        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToLoginActivity();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buatAkun();
            }
        });
    }

    private void buatAkun() {
        String ema = email.getText().toString();
        String pass = password.getText().toString();
        final String userName = name.getText().toString();
        final String phone = noTelp.getText().toString();
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Tunggu sebentar ... ");
        progressDialog.setTitle("Message");
        progressDialog.show();
        if(TextUtils.isEmpty(userName))
        {
            Toast.makeText(this, "Masukkan Nama Lengkap Anda", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ema))
        {
            Toast.makeText(this, "Masukkan Email Anda", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Masukkan Password Anda", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Masukkan No Telephone Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(ema,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent profileIntent = new Intent(RegisterActivity.this,SetupActivity.class);
                        profileIntent.putExtra("name", userName);
                        profileIntent.putExtra("phone", phone);
                        startActivity(profileIntent);
                        finish();
                        Toast.makeText(RegisterActivity.this, "Create Account Success", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void sentToLoginActivity()
    {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
