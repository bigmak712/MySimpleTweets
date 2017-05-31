package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private final int COMPOSE_REQUEST_CODE = 10;
    private final int COMPOSE_RESULT_CODE = 20;

    private TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu which adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == COMPOSE_RESULT_CODE && requestCode == COMPOSE_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("tweet");
            fragmentTweetsList.tweetAdded(tweet);
        }
    }
}
