package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.ivProfileImage;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ComposeActivity extends AppCompatActivity {

    //private final int COMPOSE_REQUEST_CODE = 10;
    private final int COMPOSE_RESULT_CODE = 20;

    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient(); // singleton client

        setUserHeader();
    }

    public void setUserHeader() {


        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", "Got the User Account Successfully");

                try {
                    user = User.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ImageView profileImage = (ImageView)findViewById(ivProfileImage);
                TextView name = (TextView)findViewById(R.id.tvUserName);

                name.setText(user.getName());
                profileImage.setImageResource(android.R.color.transparent); // clear out the old image for a recycled view
                Picasso.with(getContext()).load(user.getProfileImageUrl()).into(profileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", "Failure when getting the User Account");
            }

        });
    }

    public void sendTweet(View v) {
        EditText tweetBody = (EditText)findViewById(R.id.etTweetBody);
        String body = tweetBody.getText().toString();

        client.postTweet(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Tweet newTweet = null;
                try {
                    newTweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DEBUG", newTweet.getBody());

                Intent i = new Intent();
                i.putExtra("tweet", newTweet);
                setResult(COMPOSE_RESULT_CODE, i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());

            }

        });
    }
}
