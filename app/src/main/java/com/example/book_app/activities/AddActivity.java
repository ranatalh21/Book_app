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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.book_app.Models.DataModel;
import com.example.book_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadDescription,uploadName;
    String imageUrl;
    String desc;
    String nam;
    Uri uri;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        auth = FirebaseAuth.getInstance();


        uploadImage =findViewById(R.id.UploadImage);
        uploadName=findViewById(R.id.UploadName);
        uploadDescription =findViewById(R.id.UploadDesc);

        saveButton =findViewById(R.id.SaveButton);
        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()== Activity.RESULT_OK){
                            Intent data=result.getData();
                            uri=data.getData();
                            uploadImage.setImageURI(uri);

                        }else {
                            Toast.makeText(AddActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker=new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (imageUrl.isEmpty()){
//                    Toast.makeText(getActivity(), "Please select data", Toast.LENGTH_SHORT).show();
//                }else {

                nam=uploadName.getText().toString().replace("","");
                desc= uploadDescription.getText().toString().replace("","");


                if(TextUtils.isEmpty(nam)) {
                    uploadName.setError("Name cannot be empty!!");
                    uploadName.requestFocus();
                }else if(TextUtils.isEmpty(desc)) {
                    uploadDescription.setError("Description cannot be empty!!");
                    uploadDescription.requestFocus();

                }else if (uri == null) {
                    Toast.makeText(AddActivity.this, "Please select the image", Toast.LENGTH_SHORT).show();
                }else {
                    SaveData();
                }
//                }

            }
        });

    }

    public void SaveData(){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("BookDetails")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder=new AlertDialog.Builder(AddActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage=uriTask.getResult();
                imageUrl=urlImage.toString();
                upload();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void upload(){


        String key = FirebaseDatabase.getInstance().getReference("BookDetails").push().getKey();
//        DataModel dataClass=new DataModel(imageUrl,nam,desc,auth.getUid(),key);
        DataModel dataModel=new DataModel(imageUrl,nam,desc,auth.getUid(),key);

        dataModel.setKey(key);

        FirebaseDatabase.getInstance().getReference("BookDetails").child(key)
                .setValue(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AddActivity.this, MainActivity.class);
                            startActivity(intent);



//
//                            getActivity().finish();
                        }
                        else {task.getException();
                            Toast.makeText(AddActivity.this, "Please Upload data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

}
