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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button login,toRegister;
    EditText email,password;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private String NAME,PHONE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

    }
    public void initialize()
    {
        login = (Button) findViewById(R.id.btnLogin);
        toRegister = (Button) findViewById(R.id.btnToRegisterActivity);
        email = (EditText) findViewById(R.id.inputEmail);
        password = (EditText) findViewById(R.id.inputEmail);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmailandPassword();
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToRegisterActivity();
            }
        });

    }

    private void sentToRegisterActivity()
    {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }

    private void loginWithEmailandPassword()
    {
        String e = email.getText().toString();
        String p = password.getText().toString();
        if(TextUtils.isEmpty(e)){
            Toast.makeText(this, "Insert Your Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(p)){
            Toast.makeText(this, "Insert Your Password", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.setCancelable(true);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(e,p)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sentToHomeActivity();
                                progressDialog.dismiss();
                            }else{
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void sentToHomeActivity() {
        Intent homeIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sentToHomeActivity();
        }
    }
}
