package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    //private final int COMPOSE_REQUEST_CODE = 10;
    private final int COMPOSE_RESULT_CODE = 20;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient(); // singleton client
    }

    public void sendTweet(View v) {
        EditText tweetBody = (EditText)findViewById(R.id.etTweetBody);
        String body = tweetBody.getText().toString();

        client.postTweet(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Tweet newTweet = Tweet.fromJSON(response);
                Log.d("DEBUG", newTweet.getBody());
                Toast.makeText(ComposeActivity.this, "Success", Toast.LENGTH_SHORT).show();


                Intent i = new Intent();
                i.putExtra("tweet", newTweet);
                setResult(COMPOSE_RESULT_CODE, i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("DEBUG", responseString);
                Toast.makeText(ComposeActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
            }

        });
        Toast.makeText(ComposeActivity.this, "end of method", Toast.LENGTH_SHORT).show();

    }
}
