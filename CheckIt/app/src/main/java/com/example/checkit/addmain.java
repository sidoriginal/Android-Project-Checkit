package com.example.checkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class addmain extends AppCompatActivity {
    EditText mcontent;
    EditText mtitle;
    EditText mplace;
    EditText mtime;
    Button madd;
    Button mcancel;
    public String use;
    Login lg;


    DatabaseReference ref;

    FirebaseAuth auth;
    FirebaseUser muser;
    String museid;

    ProgressDialog loader;
    String content,title,time,place;
    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mcontent=findViewById(R.id.editTextTextPersonName3);
        mtitle=findViewById(R.id.editTextTextPersonName2);
        mplace=findViewById(R.id.editTextTextPersonName4);
        mtime=findViewById(R.id.editTextTextPersonName5);
        madd=findViewById(R.id.button5);
        mcancel=findViewById(R.id.button7);
        auth = FirebaseAuth.getInstance();
        loader=new ProgressDialog(this);
        muser=auth.getCurrentUser();
        museid=muser.getUid();
        ref= FirebaseDatabase.getInstance().getReference().child("tasks").child(museid);
        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(addmain.this,Screenmain.class);
                startActivity(i);
                finish();
            }
        });
        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content=mcontent.getText().toString().trim();
                title=mtitle.getText().toString().trim();
                time=mtime.getText().toString().trim();
                place=mplace.getText().toString().trim();
                String id =ref.push().getKey();
                String date= DateFormat.getDateInstance().format(new Date());
                if(TextUtils.isEmpty(title)){
                    mtitle.setError("Title required");
                    return;
                }else{
                    loader.setMessage("Adding your event");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    model mod=new model(title,content,place,time,id,date);
                    ref.child(id).setValue(mod).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 Toast.makeText(addmain.this, "Your event is added!!", Toast.LENGTH_SHORT).show();
                                 loader.dismiss();
                                 Intent i =new Intent(addmain.this,Screenmain.class);
                                 startActivity(i);
                                 finish();
                             }
                             else{
                                 String error=task.getException().toString();
                                 Toast.makeText(addmain.this, "Failed!!"+error, Toast.LENGTH_SHORT).show();
                                 loader.dismiss();
                             }
                        }
                    });
                }

            }
        });
    }

}