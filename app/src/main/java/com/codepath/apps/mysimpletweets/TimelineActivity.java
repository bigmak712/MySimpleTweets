package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
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

        // get the viewpager
        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
        // set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        // attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);
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
            fragmentTweetsList.tweetAdded(tweet);
        }
    }

    // Return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        // Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Controls the order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            }
            else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            else {
                return null;
            }
        }

        // Return the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // How many fragments there are to sweep between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
