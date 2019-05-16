package com.qerat.fstweek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<PostClass> itemList;
    private Context context;
    public static int CONF_DISC = 1, MENT_DISC = 2, MENT_CHAT = 3, PANEL = 4;
    private int type;
    private String mentGroup;

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView emailTextView, timeTextView, msgTextView;
        private PostClass item;
        private CardView parent;


        public PostViewHolder(View view) {
            super(view);

            emailTextView = view.findViewById(R.id.emailTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            msgTextView = view.findViewById(R.id.msgTextView);


            parent = view.findViewById(R.id.parent);
        }

        public PostClass getItem() {
            return item;
        }

        public void setItem(PostClass item) {
            this.item = item;
        }
    }


    public PostAdapter(List<PostClass> moviesList, Context context, int type) {
        this.itemList = moviesList;
        this.context = context;
        this.type = type;
    }

    public PostAdapter(List<PostClass> moviesList, Context context, int type, String mentGroup) {
        this.itemList = moviesList;
        this.context = context;
        this.type = type;
        this.mentGroup = mentGroup;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_post, parent, false);

        return new PostViewHolder(itemView);
    }

    private int convertDpToPx(float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final PostClass item = itemList.get(position);
        holder.emailTextView.setText(item.getEmail());
        holder.timeTextView.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", item.getMsgTime()));
        holder.msgTextView.setText(item.getMsg());

        if (item.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            params.setMargins(convertDpToPx(60), 0, convertDpToPx(8), convertDpToPx(7));
            holder.parent.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            params.setMargins(convertDpToPx(8), 0, convertDpToPx(60), convertDpToPx(7));
            holder.parent.setLayoutParams(params);
        }


        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (item.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {


                    String[] colors = {"Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {

                                case 0:


                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                    builder.setTitle("Confirm");
                                    builder.setMessage("Are you sure to delete?");

                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {

                                            deleteMe(item, holder.parent);
                                            dialog.dismiss();
                                        }
                                    });

                                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
                return false;
            }
        });
        holder.setItem(item);

    }

    private void deleteMe(PostClass item, final CardView cardView) {
        if (type == CONF_DISC) {
            FirebaseUtilClass.getDatabaseReference().child("Chats").child("conf").child(item.getPushId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to remove the message, try again!", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (type == MENT_DISC) {
            FirebaseUtilClass.getDatabaseReference().child("Chats").child("ment").child(mentGroup).child(item.getPushId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to remove the message, try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type == MENT_CHAT) {
            FirebaseUtilClass.getDatabaseReference().child("Chats").child("mentDirect").child(mentGroup).child(item.getPushId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to remove the message, try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type == PANEL) {
            FirebaseUtilClass.getDatabaseReference().child("PanelDisc").child("accepted").child(item.getPushId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to remove the message, try again!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
