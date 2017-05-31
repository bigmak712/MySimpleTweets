package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final int COMPOSE_REQUEST_CODE = 10;
    private final int COMPOSE_RESULT_CODE = 20;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    //private TweetsArrayAdapter aTweets;
    //private ListView lvTweets;
    private TweetsAdapter tweetsAdapter;
    private RecyclerView rvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Lookup the RecyclerView in activity layout
        rvTweets = (RecyclerView)findViewById(R.id.rvTweets);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu which adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
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

    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == COMPOSE_RESULT_CODE && requestCode == COMPOSE_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("tweet");
            tweets.add(0, tweet);
            tweetsAdapter.notifyItemInserted(0);
            rvTweets.getLayoutManager().scrollToPosition(0);
        }
    }
}
