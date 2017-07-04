package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;

/**
 * Created by bigmak712 on 6/28/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private String tabTitles[] = {"Home", "Mentions"};
    private Context context;

    // Adapter gets the manager insert or remove fragment from activity
    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
        return NUM_ITEMS;
    }
}
