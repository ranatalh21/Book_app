package com.example.book_app.Adaptars;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.book_app.DetailActivity;
import com.example.book_app.Models.DataModel;
import com.example.book_app.R;
import com.example.book_app.activities.AddActivity;
import com.example.book_app.activities.CommentaActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataModel> dataClassList;

    public MyAdapter(Context context, List<DataModel> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitsm, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(dataClassList.get(position).getDataImage()).into(holder.recImage);
        holder.recName.setText(dataClassList.get(position).getDataName());
        holder.recDesc.setText(dataClassList.get(position).getDataDesc());



        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataClassList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Name", dataClassList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("Description", dataClassList.get(holder.getAdapterPosition()).getDataDesc());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Box");
                builder.setMessage("Are you sure you want to delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemId = dataClassList.get(holder.getAdapterPosition()).getKey();
                        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("BookDetails").child(itemId);
                        itemRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                        //dataClassList.remove(position);
                                        //notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                                        Log.e("DeleteItem", "Error deleting item: " + e.getMessage());
                                    }
                                });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });

                builder.show();
            }
        });


         holder.btnAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final DialogPlus dialogPlus=DialogPlus.newDialog(holder.recImage.getContext())
                       .setContentHolder(new ViewHolder(R.layout.update_pop))
                       .setExpanded(true,1200)
                       .create();

               View view1=dialogPlus.getHolderView();
               ImageView image=view1.findViewById(R.id.editImage);
               EditText name=view1.findViewById(R.id.EditName);
               EditText descrip=view1.findViewById(R.id.EditDesc);
               Button btn_update=view1.findViewById(R.id.UpdateButton);

                Glide.with(context).load(dataClassList.get(position).getDataImage()).into(image);

                name.setText( dataClassList.get(holder.getAdapterPosition()).getDataName());
               descrip.setText( dataClassList.get(holder.getAdapterPosition()).getDataDesc());

               dialogPlus.show();

//                holder.circleImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        openImagePickerForResult(holder.activityResultLauncher);
//                    }
//                });



               btn_update.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {


                       AlertDialog.Builder builder=new AlertDialog.Builder(context);
                       builder.setCancelable(false);
                       builder.setView(R.layout.progress_layout);
                       AlertDialog dialog=builder.create();
                       dialog.show();



                       Map<String,Object> map=new HashMap<>();
                       map.put("dataDesc",descrip.getText().toString());
                       map.put("dataName",name.getText().toString());
                       String itemId1 = dataClassList.get(holder.getAdapterPosition()).getKey();
                       DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("BookDetails").child(itemId1);
                       itemRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Toast.makeText(holder.recName.getContext(), "Data Update Successfully", Toast.LENGTH_SHORT).show();
                           dialogPlus.dismiss();
                           dialog.dismiss();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(holder.recName.getContext(), "Error while Uploading", Toast.LENGTH_SHORT).show();

                           }
                       });

                   }
               });
            }
        });


        getComments(dataClassList.get(holder.getAdapterPosition()).getKey(),holder.com);

        holder.
    com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this code is pass the activity and data from the model
                Log.d("vaaaa",dataClassList.get(position).getPublishBY());
                Intent intent=new Intent(context, CommentaActivity.class);
                intent.putExtra("postId",dataClassList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("publishedBy",dataClassList.get(position).getPublishBY());
                context.startActivity(intent);
            }
        });


    }

//    private void openImagePickerForResult(ActivityResultLauncher<Intent> launcher) {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        launcher.launch(intent);
//    }



    @Override
    public int getItemCount() {
        return dataClassList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage,com;
        TextView recName, recDesc;
        CardView recCard;
        Button btnAd, btnDelete;
        CircleImageView circleImageView;
        ActivityResultLauncher<Intent> activityResultLauncher;
        Uri selectedImageUri;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.imageView);
            recName = itemView.findViewById(R.id.book_name);
            recDesc = itemView.findViewById(R.id.book_des);
            recCard = itemView.findViewById(R.id.cardRec);
            btnAd = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDell);
            circleImageView=itemView.findViewById(R.id.editImage);
            com=itemView.findViewById(R.id.Comment);




//            activityResultLauncher = context.registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    new ActivityResultCallback<ActivityResult>() {
//                        @Override
//                        public void onActivityResult(ActivityResult result) {
//                            if (result.getResultCode() == Activity.RESULT_OK) {
//                                Intent data = result.getData();
//                                Uri uri = data.getData();
//                                circleImageView.setImageURI(uri);
//                                selectedImageUri = uri;
//                            } else {
//                                Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }


    public void getComments(String postId,ImageView comments){
        //this code set the comment in given below ref
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                comments.setText("View All"+snapshot.getChildrenCount()+"comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

