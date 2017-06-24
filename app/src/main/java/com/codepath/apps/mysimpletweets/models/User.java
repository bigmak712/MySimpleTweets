package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bigmak712 on 5/28/17.
 */

public class User implements Parcelable{
    // list the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followersCount;
    private int followingCount;

    public String getName() { return name; }
    public long getUid() { return uid; }
    public String getScreenName() { return screenName; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getTagline() { return tagline; }
    public int getFollowersCount() { return followersCount; }
    public int getFriendsCount() { return followingCount; }

    //deserialize the user json -> USER
    public static User fromJSON(JSONObject json) throws JSONException{
        User u = new User();

        // Extract and fill the values
        u.name = json.getString("name");
        u.uid = json.getLong("id");
        u.screenName = json.getString("screen_name");
        u.profileImageUrl = json.getString("profile_image_url");
        u.tagline = json.getString("description");
        u.followersCount = json.getInt("followers_count");
        u.followingCount = json.getInt("friends_count");

        //Return a user
        return u;
    }

    // Parcelable Methods

    // Write the values that you want to save to the Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeLong(uid);
        out.writeString(screenName);
        out.writeString(profileImageUrl);
    }

    // Retrieve the values that we originally wrote into the Parcel
    private User(Parcel in) {
        name = in.readString();
        uid = in.readLong();
        screenName = in.readString();
        profileImageUrl = in.readString();
    }

    // Normal default constructor
    public User(){}


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
