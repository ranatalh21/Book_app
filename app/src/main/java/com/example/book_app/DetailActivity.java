package com.example.book_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.book_app.Models.DataModel;
import com.example.book_app.activities.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView detailsDec,detailName;
    ImageView detailImage,back;
    Button btnAd, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        detailImage=findViewById(R.id.detailImage);
        detailName=findViewById(R.id.detailName);
        detailsDec=findViewById(R.id.detailDesc);
        back=findViewById(R.id.backPress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            detailName.setText(bundle.getString("Name"));
            detailsDec.setText(bundle.getString("Description"));
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }






    }
}
