package com.example.checkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class Signup extends AppCompatActivity {
    EditText musername;
    EditText mpassword;
    EditText mcnfpassword;
    EditText memail;
    Button msignup;
    private FirebaseAuth auth;
    ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        auth =FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);
        musername =findViewById(R.id.editTextTextPersonName);
        mpassword=findViewById(R.id.editTextTextPassword);
        mcnfpassword=findViewById(R.id.editTextTextPassword2);
        memail=findViewById(R.id.editTextTextEmailAddress);
        msignup=findViewById(R.id.button2);
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=musername.getText().toString().trim();
                String password=mpassword.getText().toString().trim();
                String cnfpassword=mcnfpassword.getText().toString().trim();
                String email=memail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    memail.setError("Email is required");
                }
                if(TextUtils.isEmpty(user)){
                    musername.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mpassword.setError("Password is required");return;
                }
                if(TextUtils.isEmpty(cnfpassword)){
                   mcnfpassword.setError("Please confirm your password");return;
                }
                if(password.equals(cnfpassword)){
                    loader.setMessage("Signup in progress ");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                  auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){
                          Intent i =new Intent(Signup.this,Screenmain.class);
                          startActivity(i);
                          finish();
                          loader.dismiss();
                          }
                          else{
                              String error=task.getException().toString();
                              Toast.makeText(Signup.this, "Signup Failed "+error, Toast.LENGTH_SHORT).show();
                              loader.dismiss();
                          }
                      }
                  });
                }
                else {
                    Toast.makeText(Signup.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                   }
            }
        });
    }
}