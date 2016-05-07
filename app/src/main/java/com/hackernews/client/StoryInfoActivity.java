package com.hackernews.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class StoryInfoActivity extends AppCompatActivity {

    TextView storyIDTextView;
    ProgressBar pb2;
    TextView storyInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_info);

        storyInfoTextView = (TextView)findViewById(R.id.storyInfoTextView);
        storyIDTextView = (TextView)findViewById(R.id.storyIdTextView);
        pb2 = (ProgressBar)findViewById(R.id.pb2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String story_id = extras.getString("STORY_ID");
            storyIDTextView.setText(story_id);

            String URL = Constants.GET_STORY_DETAIL_URL + story_id + ".json?print=pretty";

            NetworkUtility networkUtility = new NetworkUtility(getApplicationContext());
            if (networkUtility.isNetworkAvailable()){
                getStoryDetails(URL);
            }
            else{
                Toast.makeText(getApplicationContext(), "Network Not Available !", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Invalid Story ID", Toast.LENGTH_SHORT).show();
        }

    }

    private void getStoryDetails(String URL) {
        pb2 = (ProgressBar) findViewById(R.id.progressBar);
        pb2.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pb2.setVisibility(View.INVISIBLE);

                        Log.d("Response : ", response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb2.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Error in getting Response", Toast.LENGTH_SHORT).show();
                Log.d("Error Occurred : ", error.toString());
            }
        });

        queue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
