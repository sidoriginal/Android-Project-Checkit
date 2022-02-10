package com.example.checkit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adap extends RecyclerView.Adapter<adap.vholder> {
    @NonNull
    private String[] data;
    public adap(String[] data){
        this.data=data;
    }
    @Override
    public vholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_list,parent,false);
        return new vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  adap.vholder holder, int position) {
        String title = data[position];
        holder.item.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class vholder extends RecyclerView.ViewHolder{
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
}
