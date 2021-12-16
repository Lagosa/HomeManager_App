package com.Lagosa.homemanager_app.Database;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Lagosa.homemanager_app.MainActivity;
import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.Chores.Chore;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerCalls extends AppCompatActivity {

    private final String SERVER_URL;
    private final RequestQueue queue;
    private final Context context;

    public ServerCalls(Context context){
        Log.w("Server","" + R.string.server_url);
        SERVER_URL = context.getString(R.string.server_url);
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public void wakeupCall(){
        String url = SERVER_URL + "authentication/wakeUp";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Server started!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Server NOT started! Restart the application",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    public void registerFamily(RegisterCallback callback,String email, String password){
        String url = SERVER_URL + "authentication/registerFamily/" + email + "/" + password;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UUID familyId = UUID.fromString(response.getString("UUID"));
                            Log.w("=> CreateFamily","Got familyId! " + familyId);

                            callback.onSucess(familyId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error! Check credentials provided!",Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

    public void getJoinCode(JoinCodeCallback callback,UUID familyId){

        String url = SERVER_URL + "authentication/registerFamily/"+ familyId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int joinCode = Integer.parseInt(response);
                callback.onSuccess(joinCode);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong while getting join code!",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    public void getUser(UUID userId){
        String url = SERVER_URL + "authentication/getUserData/" + userId.toString();

        Bundle user = new Bundle();
        user.putString("id","");
        user.putString("nickName","");
        user.putString("familyId","");
        user.putString("role","");
        user.putString("birthDate","");

        Log.w("ServerCalls","user created!");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.putString("id",response.getString("id"));
                    user.putString("nickName",response.getString("nickName"));
                    user.putString("familyId",response.getString("familyId"));
                    user.putString("role",response.getString("role"));
                  //  user.put("birthDate", Date.valueOf(response.getString("date")));
                    Log.w("ServerCalls","User got from server!");
                    new MainActivity().goToDrawer(user,context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("ServerCalls","Error occured on server!");
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void getAllNotDoneChores(ChoreListCallback callback,UUID userId){
        String url = SERVER_URL + "chore/listOfNotDone/" + userId.toString();

        List<Chore> chores = new ArrayList<>();
        Log.w("CHORES","Fetching data from: " + url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.w("CHORES","Got response!" + response.length());
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject choreObject = response.getJSONObject(i);
                        Chore chore = new Chore(choreObject.getString("submitterName"), Date.valueOf(choreObject.getString("submissionDate")),
                                Date.valueOf(choreObject.getString("dueDate")),choreObject.getString("typeName"),choreObject.getString("description"),
                                choreObject.getString("title"));
                        chore.setId(choreObject.getInt("id"));
                        chore.setStatus(choreObject.getString("status"));
                        chore.setDoneByName(choreObject.getString("doneByName"));
                        if(chore.getDoneByName().equals("null")){
                            chore.setDoneByName(" - ");
                        }
                        Log.w("CHORES","Chore got!" + chore.toString());
                        chores.add(chore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.setNotDoneChoreList(chores);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
}
