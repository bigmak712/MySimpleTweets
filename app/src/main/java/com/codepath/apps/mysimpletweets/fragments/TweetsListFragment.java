package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.TweetsAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bigmak712 on 5/31/17.
 */

public class TweetsListFragment extends Fragment implements TweetsAdapter.TweetAdapterListener{

    public interface TweetSelectedListener {
        // handle tweet selection
        public void onTweetSelected(Tweet tweet);
    }

    private ArrayList<Tweet> tweets;
    private TweetsAdapter tweetsAdapter;
    private RecyclerView rvTweets;
    private SwipeRefreshLayout swipeContainer;
    private int page = 0;
    private TwitterClient client;

    // inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(page);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Lookup the RecyclerView in activity layout
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        // Attach the adapter to the RecyclerView to populate the items
        rvTweets.setAdapter(tweetsAdapter);
        // Set layout manager to position the items
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    // creation lifecycle event

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the arraylist (data source)
        tweets = new ArrayList<>();
        // Create adapter passing in the data
        tweetsAdapter = new TweetsAdapter(getActivity(), tweets, this);

        client = TwitterApplication.getRestClient();
    }

    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        tweetsAdapter.notifyDataSetChanged();
    }

    public void tweetAdded(Tweet tweet) {
        tweets.add(0, tweet);
        tweetsAdapter.notifyItemInserted(0);
        rvTweets.getLayoutManager().scrollToPosition(0);
    }
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        Log.d("DEBUG", "Refreshing Timeline");

        if(page == 0) {
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("DEBUG", response.toString());
                    // Remember to CLEAR OUT old items before appending in the new ones
                    tweets = new ArrayList<Tweet>();
                    // ...the data has come back, add new items to your adapter...
                    addAll(Tweet.fromJSONArray(response));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
        else if(page == 1) {
            client.getMentionsTimeline(new JsonHttpResponseHandler() {
                // SUCCESS
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("DEBUG", response.toString());
                    // Remember to CLEAR OUT old items before appending in the new ones
                    tweets = new ArrayList<Tweet>();
                    // ...the data has come back, add new items to your adapter...
                    addAll(Tweet.fromJSONArray(response));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
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

    public void setPage(int page){
        this.page = page;
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener)getActivity()).onTweetSelected(tweet);
    }
}
