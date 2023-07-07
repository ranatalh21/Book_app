package com.example.book_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.book_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {


    private Button btnForget;
    private EditText emailNumber;
    ProgressBar progressBar;
    private String email;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        auth=FirebaseAuth.getInstance();

        emailNumber=findViewById(R.id.txtForgetPass);
        btnForget=findViewById(R.id.btnForgetSubmit);
        progressBar=findViewById(R.id.progressBarDialogeLog);

        //this click is used to forgot pass

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validData();
            }
        });
    }

    private void validData() {
        email=emailNumber.getText().toString();
        if (email.isEmpty()){
            emailNumber.setError("Email can't be empty");
        }else {
            btnForget.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            forgetpass();
        }
    }

    private void forgetpass() {
        //If user provide valid email so this code is help to forgot the pass
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                btnForget.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(ForgotActivity.this, "Kindly check you Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotActivity.this,LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(ForgotActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}