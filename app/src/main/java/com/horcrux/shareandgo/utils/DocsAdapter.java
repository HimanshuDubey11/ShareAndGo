package com.horcrux.shareandgo.utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.horcrux.shareandgo.R;
import com.horcrux.shareandgo.models.Doc;
import com.horcrux.shareandgo.screens.Sharepad;

import java.util.ArrayList;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.DocsData> {

    ArrayList<Doc> docs;

    public DocsAdapter(ArrayList<Doc> docs) {
        this.docs = docs;
    }

    DatabaseReference reference;

    public DocsAdapter() {
    }

    @NonNull
    @Override
    public DocsAdapter.DocsData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocsData(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_docs_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull DocsAdapter.DocsData holder, final int position) {

        reference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Docs").child(docs.get(position).getTitle());

        holder.titleTextView.setText(docs.get(position).getTitle());
        holder.adapterlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sharepad.titleSave = docs.get(position).getTitle();
                view.getContext().startActivity(new Intent(view.getContext(), Sharepad.class));
            }
        });
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeValue();
            }
        });


    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    public class DocsData extends RecyclerView.ViewHolder {
        TextView titleTextView;
        RelativeLayout adapterlayout;
        ImageView deleteImage;
        public DocsData(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.adaptertitle);
            adapterlayout = itemView.findViewById(R.id.adapterlayout);
            deleteImage = itemView.findViewById(R.id.deleteimage);

        }
    }
}
