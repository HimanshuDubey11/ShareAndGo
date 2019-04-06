package com.horcrux.shareandgo.screens;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.horcrux.shareandgo.R;
import com.horcrux.shareandgo.models.Doc;
import com.horcrux.shareandgo.utils.DocsAdapter;

import java.util.ArrayList;

public class ManageContent extends AppCompatActivity {

    DatabaseReference reference;
    ArrayList<Doc> docs;
    RecyclerView recyclerView;
    TextView textView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_content);

        dialog = new ProgressDialog(ManageContent.this);

        reference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Docs");

        recyclerView = findViewById(R.id.docsrecycler);

        textView = findViewById(R.id.createdocs);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sharepad.titleSave = "Untitled" + (docs.size() + 1);
                startActivity(new Intent(ManageContent.this, Sharepad.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        dialog.setMessage("Loading..");
        dialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                docs = new ArrayList<Doc>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Doc userDoc = postSnapshot.getValue(Doc.class);
                    docs.add(userDoc);

                }

                dialog.dismiss();
                recyclerView.setLayoutManager(new LinearLayoutManager(ManageContent.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(new DocsAdapter(docs));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dialog.dismiss();

            }
        });

    }
}
