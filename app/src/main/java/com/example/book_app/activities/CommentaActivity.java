package com.example.book_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.book_app.Adaptars.CommentsAdapter;
import com.example.book_app.Models.CommentsModel;
import com.example.book_app.Models.DataModel;
import com.example.book_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private ArrayList<CommentsModel> commentsModels;
    EditText addComments;
    private List<DataModel> dataClassList;
    ImageView imageUser, post, back;
    String postId;
    String publishedBy;
    FirebaseAuth auth;

    String commentRef;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenta);

        auth = FirebaseAuth.getInstance();
        addComments = findViewById(R.id.userPostComments);
        post = findViewById(R.id.uplaodComments);
        recyclerView = findViewById(R.id.recylerComments);
        back = findViewById(R.id.backBtnPress);

        // Click listener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Setup RecyclerView
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentsModels = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, commentsModels);
        recyclerView.setAdapter(commentsAdapter);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        publishedBy = intent.getStringExtra("publishedBy");
        Log.d("aadsd",publishedBy);
        readComment();

        // Click listener for the post button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addComments.getText().toString().isEmpty()) {
                    Toast.makeText(CommentaActivity.this, "You can't send an empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

        getTokenOfUser();
    }

    private void getTokenOfUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(publishedBy).child("token");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                token = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addComment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("comments").child(postId);

        String commentText = addComments.getText().toString();
        String currentUserId = auth.getCurrentUser().getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", commentText);
        hashMap.put("publisher", currentUserId);

        reference.push().setValue(hashMap);
        addComments.setText("");

        sendCommentNotification();

        recyclerView.scrollToPosition(commentsModels.size() - 1);
    }

    private void readComment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentsModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CommentsModel commentsModel = dataSnapshot.getValue(CommentsModel.class);
                    commentsModels.add(commentsModel);
                    Log.d("Comments", "onDataChange: " + commentsModel.toString());
                }

                recyclerView.scrollToPosition(commentsModels.size() - 1);
                commentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void sendCommentNotification() {
        Log.d("tokenvalue",token);
        FcmNotificationSender notificationSender = new FcmNotificationSender(token, "New Comment", "Someone commented on your post", getApplicationContext());
        notificationSender.sendNotifications();
    }
}
