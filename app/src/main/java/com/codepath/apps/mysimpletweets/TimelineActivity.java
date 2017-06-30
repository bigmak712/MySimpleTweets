package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsPagerAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    private final int COMPOSE_REQUEST_CODE = 10;
    private final int COMPOSE_RESULT_CODE = 20;

    private ViewPager vpPager;
    private TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the viewpager
        vpPager = (ViewPager)findViewById(R.id.viewpager);

        // initialize the pager adapter
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);

        // set the viewpager adapter for the pager
        vpPager.setAdapter(tweetsPagerAdapter);

        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu which adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically
        // handle clicks on the Home/Up button, so long as you specify a parent
        // activity in AndroidManifest.xml

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_REQUEST_CODE);
    }

    public void onProfileView(MenuItem mi) {
        // Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == COMPOSE_RESULT_CODE && requestCode == COMPOSE_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("tweet");
            HomeTimelineFragment fragment = (HomeTimelineFragment)tweetsPagerAdapter.getItem(0);
            fragment.tweetAdded(tweet);
            vpPager.setCurrentItem(0);
        }
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        Toast.makeText(this, tweet.getBody(), Toast.LENGTH_SHORT).show();
    }
}
