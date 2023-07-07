package com.example.book_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.book_app.Adaptars.MyAdapter;
import com.example.book_app.Models.DataModel;
import com.example.book_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth auth;
    List<DataModel> dataClassList;
    DatabaseReference databaseReference;
    MyAdapter adapter;
    ValueEventListener valueEventListener;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=findViewById(R.id.rec_view);
        btnAdd=findViewById(R.id.Addbtn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                        startActivity(intent);
            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        auth = FirebaseAuth.getInstance();

//        Lin gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
//        recyclerView.setLayoutManager(gridLayoutManager);
//





        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();



        dataClassList= new ArrayList<>();

        adapter=new MyAdapter(MainActivity.this,dataClassList);
        recyclerView.setAdapter(adapter);

        databaseReference= FirebaseDatabase.getInstance().getReference("BookDetails");
//        dialog.show();

        valueEventListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataClassList.clear();
                for (DataSnapshot itemsSnap:snapshot.getChildren()){
                    DataModel dataClass=itemsSnap.getValue(DataModel.class);
                    //   dataClass.setDataDesc(itemsSnap.getKey());
                    dataClassList.add(dataClass);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }


        });

    }



}


