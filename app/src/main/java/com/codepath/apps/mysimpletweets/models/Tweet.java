package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bigmak712 on 5/28/17.
 */

// Parse the JSON + store the data, encapsulate state logic or display logic
public class Tweet implements Parcelable{
    // list out the attributes
    private String body;
    private long uid; // unique id for the tweet
    private User user; // store the embedded user object
    private String createdAt;

    // Deserialize the JSON and build Tweet objects

    // Getter methods
    public String getBody() { return body; }
    public long getUid() { return uid; }
    public String getCreatedAt() { return createdAt; }
    public User getUser() { return user; }

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        // Extract the values from the json, store them
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        // Return the tweet object
        return tweet;
    }

    // Tweet.fromJSONArray({...}) -> List of Tweets
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        // Iterate the json array and create tweets
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null) {
                    tweets.add(tweet);
                }

            } catch (JSONException e){
                e.printStackTrace();
                continue;
            }
        }
        // Return the finished list
        return tweets;
    }

    // Parcelable Methods

    // Write the values you want to save to the Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(body);
        out.writeLong(uid);
        out.writeParcelable(user, 0);
        out.writeString(createdAt);
    }

    // Retrieve the values that we originally wrote into the Parcel
    private Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();
    }

    // Normal default constructor
    public Tweet(){}

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {

        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
