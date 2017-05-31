package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigmak712 on 5/31/17.
 */

public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsAdapter tweetsAdapter;
    private RecyclerView rvTweets;

    // inflation logic

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        // Lookup the RecyclerView in activity layout
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        // Attach the adapter to the RecyclerView to populate the items
        rvTweets.setAdapter(tweetsAdapter);
        // Set layout manager to position the items
        rvTweets.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    // creation lifecycle event

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the arraylist (data source)
        tweets = new ArrayList<>();
        // Create adapter passing in the data
        tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
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
}
