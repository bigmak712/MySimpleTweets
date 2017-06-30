package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bigmak712 on 6/2/17.
 */

public class UserTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();
    }

    // Creates a new fragment given an int and title
    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    // Send an API request to get the timeline json
    // Fill the listview by creating the tweet objects from the json
    private void populateTimeline() {
        // Comes from the activity
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                Log.d("DEBUG", json.toString());
                // DESERIALIZE JSON
                // CREATE MODELS + ADD THEM TO ADAPTER
                // LOAD THE MODEL DATA INTO LISTVIEW
                addAll(Tweet.fromJSONArray(json));
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
