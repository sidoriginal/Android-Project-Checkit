package com.example.checkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class  Screenmain extends AppCompatActivity {
    RecyclerView liste;
    Button add;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser muser;
    String museid;
    Button done;
    ProgressDialog loader;
    Button m;

    String key="";
    String content,title,time,place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenmain);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        FirebaseMessaging.getInstance().subscribeToTopic("noti");
        auth = FirebaseAuth.getInstance();
        loader=new ProgressDialog(this);
        muser=auth.getCurrentUser();
        museid=muser.getUid();
        ref= FirebaseDatabase.getInstance().getReference().child("tasks").child(museid);
        liste= findViewById(R.id.liste);
        add=findViewById(R.id.button3);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Screenmain.this,addmain.class);
                startActivity(i);
                finish();
            }
        });
        liste.setLayoutManager(new LinearLayoutManager(this));
        liste.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<model> options=new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(ref,model.class)
                .build();
        FirebaseRecyclerAdapter<model,vholder> adapter = new FirebaseRecyclerAdapter<model, vholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  vholder holder, int position, @NonNull  model model) {
              holder.settitle(model.getTitle());
              holder.done.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      key=getRef(position).getKey();
                      title=model.getTitle();
                      content=model.getContent();
                      place=model.getPlace();
                      time=model.getTime();

                      ref.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull  Task<Void> task) {
                              if(task.isSuccessful()){
                                  Toast.makeText(Screenmain.this, "Congrats on doing the job ", Toast.LENGTH_SHORT).show();
                              }
                              else {
                                  String error=task.getException().toString();
                                  Toast.makeText(Screenmain.this, "Cannot update it "+error, Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
                  }
              });
              holder.item.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      key=getRef(position).getKey();
                      title=model.getTitle();
                      content=model.getContent();
                      place=model.getPlace();
                      time=model.getTime();
                    det();

                  }
              });
            }

            @NonNull
            @Override
            public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
                return new vholder(view);
            }
        };
        liste.setAdapter(adapter);
        adapter.startListening();

    }

 class vholder extends RecyclerView.ViewHolder{
    TextView item;
    Button done;
    public vholder(@NonNull View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.textView3);
        done = itemView.findViewById(R.id.button4);
    }
    public void settitle(String title){
        TextView titl=item.findViewById(R.id.textView3);
        titl.setText(title);
    }
}
    private void det(){
        AlertDialog.Builder dia =new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.detailsshow,null);
        dia.setView(view);
        AlertDialog mdia = dia.create();

        TextView a =view.findViewById(R.id.textView9);
        TextView b =view.findViewById(R.id.textView11);
        TextView c=view.findViewById(R.id.textView13);
        TextView d =view.findViewById(R.id.textView15);
        Button done=view.findViewById(R.id.button6);
        m=view.findViewById(R.id.button8);
        a.setText(title);
        b.setText(content);
        c.setText(place);
        d.setText(time);
        String as=c.getText().toString().trim();
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:0,0?q="+as));
                    Intent chooser=Intent.createChooser(intent,"Launch Maps");

                    if(chooser.resolveActivity(getPackageManager())!=null){
                        startActivity(chooser);
                    }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Screenmain.this, "Congrats on doing the job ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String error=task.getException().toString();
                            Toast.makeText(Screenmain.this, "Cannot update it "+error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mdia.dismiss();
            }
        });



        mdia.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.it:
                auth.signOut();
                Intent i = new Intent(Screenmain.this,Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



