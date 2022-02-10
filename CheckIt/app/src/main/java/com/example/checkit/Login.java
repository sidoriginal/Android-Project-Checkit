package com.example.checkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText musername;
    EditText mpassword;
    TextView join;
    Button mlogin;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener aut;
    ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        auth = FirebaseAuth.getInstance();
        aut=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= auth.getCurrentUser();
                if(user!=null){
                    Intent i=new Intent(Login.this,Screenmain.class);
                    startActivity(i);
                    finish();
                }

            }
        };


        loader=new ProgressDialog(this);

        musername=findViewById(R.id.UserName);
        mpassword=findViewById(R.id.Password);
        mlogin=findViewById(R.id.button);
        join=findViewById(R.id.textView);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login.this,Home.class);
                startActivity(i);
            }
        });
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=musername.getText().toString().trim();
                String password=mpassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    musername.setError("Email is required");return;
                }
                if(TextUtils.isEmpty(password)){
                    musername.setError("Please enter your password");return;
                }else{
                    loader.setMessage("Logging you in...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i =new Intent(Login.this,Screenmain.class);
                                startActivity(i);
                                finish();
                                loader.dismiss();
                            }
                            else{
                                String error=task.getException().toString();
                                Toast.makeText(Login.this, "Login Failed "+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }

   @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(aut);

    }

//   @Override
//    protected void onStop() {
//        super.onStop();
//        auth.addAuthStateListener(aut);
//    }
}