package com.hackernews.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button getStoriesBtn;
    ListView storiesListView;
    ProgressBar pb;
    List<StoryListItem> rowItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getStoriesBtn = (Button)findViewById(R.id.getStoriesBtn);
        storiesListView = (ListView)findViewById(R.id.storiesListView);

        getStoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Get bTn Pressed", Toast.LENGTH_SHORT).show();

                String URL = Constants.GET_STORIES_URL;
//                String params = "token=" + Constants.API_TOKEN;

                NetworkUtility networkUtility = new NetworkUtility(getApplicationContext());
                if (networkUtility.isNetworkAvailable()){
//                    getAgencyList(URL, params);
                    getStoriesList(URL);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Network Not Available !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void getStoriesList(String URL) {
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URL;
        final String[] data = new String[1];

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pb.setVisibility(View.INVISIBLE);

                        Log.d("Response : ", response);

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String[] strArr = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                strArr[i] = jsonArray.getString(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.i("array is : ", "arr");
                        populateAgencyListView(strArr);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Error in getting Response", Toast.LENGTH_SHORT).show();
                Log.d("Error Occurred : ", error.toString());
            }
        });

        queue.add(stringRequest);
    }



    private void populateAgencyListView(String[] storyListResponse) {
        rowItems = new ArrayList<>();

        for (int i = 0; i < storyListResponse.length; i++) {
                String id = storyListResponse[i];

                StoryListItem item = new StoryListItem(id);
                rowItems.add(item);

        }

        if( rowItems.size() == 0 ){
            Toast.makeText(this, "No Results Found !", Toast.LENGTH_SHORT).show();
        }

        StoryListItemAdapter adapter = new StoryListItemAdapter(getApplicationContext(), rowItems);
        storiesListView.setAdapter(adapter);

        storiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String id = rowItems.get(position).getId();
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), StoryInfoActivity.class);
                intent.putExtra("STORY_ID", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
