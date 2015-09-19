package com.example.andre.instagramclient;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "4bb150286f6f4e9f8f6a238fa9204383";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdaptor aPhotos;

    private SwipeRefreshLayout swipeContainer;

    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // create the network client
        client = new AsyncHttpClient();


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
               // FetchPopularPhotos(); // may need to replace this function

                // Send request for popular photos
                //photos = new ArrayList<InstagramPhoto>();
                photos.clear();

                FetchPopularPhotos();
                aPhotos.notifyDataSetChanged();
           }
        });


        // Send request for popular photos
        photos = new ArrayList<InstagramPhoto>();
        // create the adaptor linking it to the source
        aPhotos = new InstagramPhotosAdaptor(this,photos);
        // find the list view
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        // set the adaptor binding it to the list view
        lvPhotos.setAdapter(aPhotos);
        FetchPopularPhotos();


    }

    // triggers the request
    public void FetchPopularPhotos(){
        /*Data maps to an array of objects
        - Type: { "data" => [x]=> "type"}("image" or "video"
        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;



        // trigger the get request
        client.get(url, null, new JsonHttpResponseHandler(){
            // on Success (worked 200)

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                // expecting data-URL :
                // Log.i("DEBUG", response.toString());

                // Iterate each of the photo items and decode into a java object
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data"); // array of posts
                    // itereate the array of posts
                    int n = photosJSON.length();
                    for(int i = 0; i < n; i++){
                        // get the json object
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributes of the jsom into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        // -Author name: : { "data" => [x]=> "user" => "username"}
                        photo.userName = photoJSON.getJSONObject("user").getString("username");
                        // - Caption: { "data" => [x]=> "caption" => "text"}
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");


                        // likes
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // photo.type = photoJSON.getJSONObject("type").getString("text");

                        // { "data" => [x]=> " image " => "standard_resolution" => URL}
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // height
                        photo.imageHieight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");

                        // creation time

                        long t = photoJSON.getJSONObject("caption").getInt("created_time");
                        Date df = new Date(t * 1000);
                        photo.time = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);

                        // get the profile image

                        photo.profileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");


                        photos.add(photo);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
            }
            // of failure

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
