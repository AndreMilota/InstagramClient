package com.example.andre.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andre on 9/15/2015.
 */
public class InstagramPhotosAdaptor extends ArrayAdapter<InstagramPhoto> {
    // pass in the data we need from the activity
    // context and data source
    public InstagramPhotosAdaptor(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // get the data item for the position
        InstagramPhoto photo = getItem(position);
        // check if we are using a recycled view if not we need to inflate
        if(convertView == null){
            // create a new view from the template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // look up the views for populating in the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.ivUserName);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);

        // insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        tvUserName.setText(photo.userName);
        tvDate.setText(photo.time);
        String countString = String.valueOf(photo.likesCount);
        tvLikes.setText(countString);
        // clear out the image view
        ivPhoto.setImageResource(0);


        // insert the image using pacaso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);


        // return the created item as a viwew

        return convertView;

    }
}
