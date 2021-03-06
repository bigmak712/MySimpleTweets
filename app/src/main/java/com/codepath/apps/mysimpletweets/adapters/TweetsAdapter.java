package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.mysimpletweets.R.id.ivProfileImage;

/**
 * Created by bigmak712 on 5/29/17.
 */

// Create the basic adapter that extends the RecyclerView.Adapter
// NOTE: we specify the custom ViewHolder which gives us access to our views
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    // Store a member variable for the tweets
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;
    private TweetAdapterListener mListener;

    // define an interface required by the ViewHolder
    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }

    // Pass in the tweets array into the constructor
    public TweetsAdapter(Context context, List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mContext = context;
        mListener = listener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Holder should contain a member variable for any view that will be set as you render a row
        public ImageButton profileImage;
        public TextView userName;
        public TextView body;
        public TextView timeStamp;
        public TextView screenName;

        // Constructor that accepts the entire item row and does the view lookups to find each subview
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used to access the
            // context from any ViewHolder instance
            super(itemView);

            profileImage = (ImageButton) itemView.findViewById(ivProfileImage);
            userName = (TextView) itemView.findViewById(R.id.tvUserName);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            timeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            screenName = (TextView) itemView.findViewById(R.id.tvScreenName);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        // get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mListener.onItemSelected(v, position);
                    }
                }
            });
        }
    }

    public List<Tweet> getTweets() {
        return mTweets;
    }
    // Easy access to the context object in the RecyclerView
    private Context getContext() { return mContext; }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        Tweet tweet = mTweets.get(position);

        // Set item views based on your views and data model
        //TextView userName = viewHolder.userName;
        //TextView body = viewHolder.body;
        //TextView timeStamp = viewHolder.timeStamp;
        //ImageButton profileImage = viewHolder.profileImage;

        viewHolder.userName.setText(tweet.getUser().getName());
        viewHolder.body.setText(tweet.getBody());
        viewHolder.timeStamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.screenName.setText("@" + tweet.getUser().getScreenName());

        // clear out the old image for a recycled view
        viewHolder.profileImage.setImageResource(android.R.color.transparent);

        Glide.with(mContext)
                .load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 5, 0))
                .into(viewHolder.profileImage);
        addListenerOnImageButton(tweet, viewHolder.profileImage);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void addListenerOnImageButton(final Tweet tweet, ImageButton ib) {
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the profile view
                Intent i = new Intent(mContext, ProfileActivity.class);
                i.putExtra("user_id", tweet.getUser().getUid());
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                mContext.startActivity(i);
            }
        });
    }
}
