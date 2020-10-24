package com.example.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ViewQuestion extends Activity implements MyRecyclerViewAdapter.ItemClickListener{
    MyRecyclerViewAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);
        String url = "https://api.stackexchange.com/2.2/questions?pagesize=50&site=stackoverflow";
        sendRequest(url);
    }

    private void sendRequest(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response: "+response);
                        buildQuestionList(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Ya");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void buildQuestionList(String response){
        ArrayList<JSONObject> questions = new ArrayList<>();
        JSONObject JsonResponse = null;
        JSONArray items = null;

        int itemLength = 0;
        int itemCounter;
        try {
            JsonResponse = new JSONObject(response);
            items = JsonResponse.getJSONArray("items");
            itemLength = items.length();
        }catch (Exception e){
            e.printStackTrace();
        }

        //Build The Question Objects
        for (itemCounter = 0; itemCounter < itemLength; itemCounter++) {
            try {
                JSONObject Item = items.getJSONObject(itemCounter);
                Item.remove("link");
                //System.out.println(">>>: "+Item.getString("link"));
                questions.add(Item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        viewQuestionInRecyclerView(questions);
    }
    private void viewQuestionInRecyclerView(ArrayList<JSONObject> questions){
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, questions);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}