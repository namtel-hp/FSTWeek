package com.qerat.fstweek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<EventClass> itemList;
    private Context context;
    private EventsContainerFragment frag;

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, speakerNameTextView, timeTextView, locationTextView, dateTextView;
        private ImageView noImageImageView, speakerImageView;
        private EventClass item;
        private CardView parent;
        private CheckBox checkBox;

        //todo: add checkbox


        public EventViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.titleTextView);
            speakerNameTextView = view.findViewById(R.id.speakerNameTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            noImageImageView = view.findViewById(R.id.noImageImageView);
            speakerImageView = view.findViewById(R.id.speakerImageView);
            dateTextView = view.findViewById(R.id.dateTextView);
            checkBox = view.findViewById(R.id.selectCheckBox);

            parent = view.findViewById(R.id.parent);
        }

        public EventClass getItem() {
            return item;
        }

        public void setItem(EventClass item) {
            this.item = item;
        }
    }


    public EventsAdapter(List<EventClass> moviesList, Context context, EventsContainerFragment frag) {
        this.itemList = moviesList;
        this.context = context;
        this.frag = frag;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_conference, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        final EventClass item = itemList.get(position);
        holder.titleTextView.setText(item.getEventTitle());
        holder.locationTextView.setText(item.getEventLocation());
        holder.timeTextView.setText(item.getEventTime());
        holder.speakerNameTextView.setText(item.getSpeakerName());
        holder.dateTextView.setText(item.getEventDate());

        if (item.getParticipantsMap() != null) {


            if (item.getParticipantsMap().get(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                holder.checkBox.setChecked(true);
                frag.addToList(item.getPushId());
                //  holder.parent.setBackgroundColor(context.getResources().getColor(R.color.very_light_green));

            } else {
                holder.checkBox.setChecked(false);
                frag.addToNotSelectedPushId(item.getPushId());
            }
        } else {
            holder.checkBox.setChecked(false);
            frag.addToNotSelectedPushId(item.getPushId());
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                if(holder.checkBox.isChecked()){
                    frag.addToList(item.getPushId());
                    frag.removeFromNotSelectedPushId(item.getPushId());
                }else {
                    frag.removeFromList(item.getPushId());
                    frag.addToNotSelectedPushId(item.getPushId());
                }
            }
        });
        holder.setItem(item);

        if(item.getSpeakerPushId()!=null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Speakers").child(item.getSpeakerPushId());


            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    holder.noImageImageView.setVisibility(View.GONE);
                    holder.speakerImageView.setVisibility(View.VISIBLE);
                    Uri downloadUrl = uri;

                    Picasso.get().load(downloadUrl.toString()).into(holder.speakerImageView);
                    holder.speakerImageView.invalidate();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //  holder.machineImage.setVisibility(View.GONE);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
