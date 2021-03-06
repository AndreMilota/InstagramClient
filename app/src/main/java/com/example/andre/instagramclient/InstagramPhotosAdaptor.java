package com.example.andre.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import static com.example.andre.instagramclient.R.drawable.loading;

/**
 * Created by Andre on 9/15/2015.
 */
public class InstagramPhotosAdaptor extends ArrayAdapter<InstagramPhoto> {
    // pass in the data we need from the activity
    // context and data source

    private Transformation transformation; // this is used to draw the user

    public InstagramPhotosAdaptor(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);


        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .oval(false)
                .build();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

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

        ImageView ivUsrPhoto = (ImageView) convertView.findViewById(R.id.ivUserImg);


        // insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        tvUserName.setText(photo.userName);
        tvDate.setText(photo.time);
        String countString = String.valueOf(photo.likesCount);
        tvLikes.setText(countString);
        // clear out the image view
        ivPhoto.setImageResource(0);


        // insert the image using pacaso
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.loading).into(ivPhoto);

        // add the user profile image

        Picasso.with(getContext()).
                load(photo.profileImageUrl).
                into(ivUsrPhoto);

       // Picasso.with(getContext()).load(photo.profileImageUrl).into(ivUsrPhoto);

        /*Picasso.with(getContext())
                .load(photo.profileImageUrl)
                .fit()
                .transform(transformation)
                .into(ivUsrPhoto);*/


        // return the created item as a viwew

        TextView comment1 = (TextView) convertView.findViewById(R.id.comment1);
        TextView comment2 = (TextView) convertView.findViewById(R.id.comment2);

        comment1.setText(photo.comment1);
        comment2.setText(photo.comment2);

        return convertView;

    }
}
