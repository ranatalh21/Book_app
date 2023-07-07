package com.example.book_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.example.book_app.R;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    TextView txtRegister,forgot;
    Button btnLogin;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private ImageView Google,Facebook;
//    private CallbackManager callbackManager;


    String token;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //This is views of layout
        email=findViewById(R.id.txtName);
        password=findViewById(R.id.txtPas);
        Google = findViewById(R.id.google);
        Facebook = findViewById(R.id.facebook);

        txtRegister=findViewById(R.id.txtReg);
        forgot=findViewById(R.id.forgot);
        btnLogin=findViewById(R.id.btnLogin);
        progressBar=findViewById(R.id.progressBarLog);
        firebaseAuth=FirebaseAuth.getInstance();


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("ttttt",s);
                        token=s;
                    }
                });
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        Log.d("debugss","HandleFacebookAccess Token:sicss ");
//                        handleFacebookAccessToken(loginResult.getAccessToken());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        Log.d("debugss","HandleFacebookAccess Token:" + exception.getLocalizedMessage());
//                    }
//                });
//
//        Facebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
//            }
//        });

                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        gsc = GoogleSignIn.getClient(this,gso);

        Google.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = gsc.getSignInIntent();
                                          startActivityForResult(intent, 100);
                                      }
        });






                //I use this button for forget the pass & pass the intent to one screen to another screen
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView=findViewById(R.id.image_show_hide_icon);
        imageView.setImageResource(R.drawable.invisible);


        //This click is use to see and hide the pass
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){


                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    imageView.setImageResource(R.drawable.invisible);
                }  else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.show);
                }
            }
        });


        //This click is use to login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        //This click is use to pass intent from one activity to another

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account= task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
//    private void handleFacebookAccessToken(AccessToken token) {
//
//
////        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            Toast.makeText(LoginActivity.this,""+task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }


    private void LoginUser() {

        String RegEmail= email.getText().toString().replace("","");
        String RegPass= password.getText().toString().replace("","");

        //This is for conditions that email and password are not empty
        if(TextUtils.isEmpty(RegEmail)){
            email.setError("Email cannot be empty!!");
            email.requestFocus();

        }else if (TextUtils.isEmpty(RegPass)) {
            password.setError("Password cannot be empty!!");
            password.requestFocus();
        }else {
            btnLogin.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // Meanwhile it is being registered from the firebase
            firebaseAuth.signInWithEmailAndPassword(RegEmail,RegPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("token");
                    usersRef.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(LoginActivity.this, "User Login is Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }


                    });
                }  else
                {

                    Toast.makeText(LoginActivity.this, "Login Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }


                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        //If the user is already login then pass the intent
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Please Login First", Toast.LENGTH_LONG).show();

        }
    }
}