package com.example.book_app.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_app.Models.RegisterHelperModel;
import com.example.book_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    RegisterHelperModel helperModel;
    EditText etRegEmail;
    EditText etRegPass, regName, regUserName;
    TextView txtRegLogin;
    Button btnRegister;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar1;
    DatabaseReference reference;
    FirebaseDatabase database;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar1 = findViewById(R.id.progressBarregister);
        etRegEmail = findViewById(R.id.txtEmail);
        etRegPass = findViewById(R.id.txtPasEmail);
        txtRegLogin = findViewById(R.id.txtRegLogin);
        btnRegister = findViewById(R.id.btnRegister);
        regName = findViewById(R.id.txtName);
        regUserName = findViewById(R.id.txtUserName);
        firebaseAuth = FirebaseAuth.getInstance();


        helperModel = new RegisterHelperModel();


        ImageView imageView = findViewById(R.id.image_show_hide);
        imageView.setImageResource(R.drawable.invisible);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("ttttt",s);
                token= s;
            }
        });




        //This click is use to see and hide the pass
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etRegPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {


                    etRegPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    imageView.setImageResource(R.drawable.invisible);
                } else {
                    etRegPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.show);
                }
            }
        });


        //This click is use to create the new user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();


            }
        });

        //This click is use to pass the intent........
        txtRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(RegisterActivity.this, LoginActivity.class)});

            }
        });

    }


    private void createUser() {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        //This is alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();


        //This code is only use for upload the image in firebase




                String name = regName.getText().toString().replace(" ", "");
                String username = regUserName.getText().toString().replace(" ", "");
                String email = etRegEmail.getText().toString().replace(" ", "");
                String password = etRegPass.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    regName.setError("Name cannot be empty!!");
                    regName.requestFocus();


                } else if (TextUtils.isEmpty(username)) {
                    regUserName.setError("UserName cannot be empty!!");
                    regUserName.requestFocus();


                } else if (TextUtils.isEmpty(email)) {
                    etRegEmail.setError("Email cannot be empty!!");
                    etRegEmail.requestFocus();

                } else if (TextUtils.isEmpty(password)) {
                    etRegPass.setError("Password cannot be empty!!");
                    etRegPass.requestFocus();
                } else {
                    btnRegister.setVisibility(View.GONE);
                    progressBar1.setVisibility(View.VISIBLE);


                    helperModel.setEmail(email);
                    helperModel.setName(name);
                    helperModel.setUsername(username);
                    helperModel.setPassword(password);
                    helperModel.setToken(token);

                    Log.d("TAGModel", "createUser: " + helperModel);
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            btnRegister.setVisibility(View.VISIBLE);
                            progressBar1.setVisibility(View.GONE);



                            if (task.isSuccessful()) {
                                reference.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(helperModel);
                                Toast.makeText(RegisterActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {

                                Toast.makeText(RegisterActivity.this, " Registration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }




    }



