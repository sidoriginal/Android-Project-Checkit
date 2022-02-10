package com.example.checkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.checkit.Login;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        auth = FirebaseAuth.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



    }

    public void log_in(View view) {
        Intent i=new Intent(Home.this,Login.class);
        startActivity(i);
    }



    public void sign_up(View view) {
        Intent i=new Intent(Home.this,Signup.class);
        startActivity(i);
        finish();
    }}

