package com.qerat.fstweek;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyGroupViewHolder> {
    private List<UserDetails> itemList;
    private Context context;


    public class MyGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameTextView, emailTextView, phoneNoTextView;

        private UserDetails item;
        private CardView parent;


        //todo: add checkbox


        public MyGroupViewHolder(View view) {
            super(view);
            fullNameTextView = view.findViewById(R.id.fullNameTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            phoneNoTextView = view.findViewById(R.id.phoneNoTextView);


            parent = view.findViewById(R.id.parent);
        }

        public UserDetails getItem() {
            return item;
        }

        public void setItem(UserDetails item) {
            this.item = item;
        }
    }


    public MyGroupAdapter(List<UserDetails> moviesList, Context context) {
        this.itemList = moviesList;
        this.context = context;

    }

    @Override
    public MyGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_group_member, parent, false);

        return new MyGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyGroupViewHolder holder, int position) {
        final UserDetails item = itemList.get(position);
        holder.fullNameTextView.setText(item.getName());
        holder.emailTextView.setText(item.getEmail());
        holder.phoneNoTextView.setText(item.getPhnNo());

        holder.setItem(item);



    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
