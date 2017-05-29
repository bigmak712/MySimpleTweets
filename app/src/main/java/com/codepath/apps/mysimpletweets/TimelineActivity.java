package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    //private TweetsArrayAdapter aTweets;
    //private ListView lvTweets;
    private TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Lookup the RecyclerView in activity layout
        RecyclerView rvTweets = (RecyclerView)findViewById(R.id.rvTweets);
        // Create the arraylist (data source)
        tweets = new ArrayList<>();
        // Create adapter passing in the data
        tweetsAdapter = new TweetsAdapter(this, tweets);
        // Attach the adapter to the RecyclerView to populate the items
        rvTweets.setAdapter(tweetsAdapter);
        // Set layout manager to position the items
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();

        /*
        // Find the listview
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        // Create the arraylist (data source)
        tweets = new ArrayList<>();
        // Construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);
        // Connect adapter to list view
        lvTweets.setAdapter(aTweets);
        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();
        */
    }

    // Send an API request to get the timeline json
    // Fill the listview by creating the tweet objects from the json
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                Log.d("DEBUG", json.toString());
                // DESERIALIZE JSON
                // CREATE MODELS + ADD THEM TO ADAPTER
                // LOAD THE MODEL DATA INTO LISTVIEW
                tweets.addAll(Tweet.fromJSONArray(json));
                Log.d("DEBUG", tweets.toString());
                tweetsAdapter.notifyDataSetChanged();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }


        });
    }
}
