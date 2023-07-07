package com.example.book_app.Adaptars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_app.Models.CommentsModel;
import com.example.book_app.Models.RegisterHelperModel;
import com.example.book_app.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.viewHolder> {


    private Context mContext;
    private ArrayList<CommentsModel> mCommentsModel;

    private FirebaseUser firebaseUser;

    public CommentsAdapter(Context mContext, ArrayList<CommentsModel> mCommentsModel) {
        this.mContext = mContext;
        this.mCommentsModel = mCommentsModel;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_comments, parent, false);

        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CommentsModel commentsModel = mCommentsModel.get(position);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.txtComment.setText(commentsModel.getComment());
         getUserInfo(holder.txtUser, mCommentsModel.get(holder.getAdapterPosition()).getPublisher());

    }
    @Override
    public int getItemCount() {
        return mCommentsModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public TextView txtUser;
        public TextView txtComment;

        public viewHolder(@NonNull View itemView) {

            super(itemView);


            txtUser = itemView.findViewById(R.id.userNameComments);
            txtComment = itemView.findViewById(R.id.userComments);

        }
    }


private void getUserInfo(TextView username, String PublisherId){
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(PublisherId);
    reference.addValueEventListener(new ValueEventListener() {


        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            RegisterHelperModel profileModel=snapshot.getValue(RegisterHelperModel.class);
            username.setText(profileModel.getUsername());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }
}