package com.hike.wa.fragments;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hike.wa.R;
import com.hike.wa.hikes.Hike;

import java.util.List;

// Used to store each hikes front end data
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> milesData;
    private List<String> drivingDistance;
    private List<Hike> hikeList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    ImageView imageView;
    Context context;
    int id;




    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<String> data, List<String> miles, List<String> drivingDistance, List<Hike> hikeList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
        this.milesData = miles;
        this.drivingDistance = drivingDistance;
        this.hikeList = hikeList;
        setHasStableIds(true);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    public void setImage(Context context, String title){
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/hikewa-d6eda.appspot.com/o/" + title + ".png?alt=media&token=f8ba60d1-180f-4108-846a-8dba94adc4f5").into(imageView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hike chosenHike = hikeList.get(position);
        String animal = chosenHike.getTitle();
        String difficulty = chosenHike.getDifficulty();
        String distance = chosenHike.getDistanceToHike();
        holder.myTextView.setText(animal);
        holder.milesView.setText(difficulty);
        holder.drivingView.setText("ETA: " + distance);
        setImage(this.context,mData.get(position).toLowerCase().replace(" ", ""));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);




    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView milesView;
        TextView drivingView;


        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.title);
            milesView = itemView.findViewById(R.id.miles);
            drivingView = itemView.findViewById(R.id.driving);
            imageView = itemView.findViewById(R.id.rowView);
            itemView.setOnClickListener(this);
        }

        //StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        //StorageReference pathReference = storageReference.child("lake22.png");



        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

        @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }
}