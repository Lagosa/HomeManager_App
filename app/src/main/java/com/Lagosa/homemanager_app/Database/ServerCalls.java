package com.Lagosa.homemanager_app.Database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.Authenticate.CreateFamily;
import com.Lagosa.homemanager_app.ui.Authenticate.JoinFamily;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public class ServerCalls extends AppCompatActivity {

    private final String SERVER_URL;
    private final RequestQueue queue;
    private final Context context;

    public ServerCalls(Context context){
        Log.w("Server","" + R.string.server_url);
      //  SERVER_URL = getString(R.string.server_url);
        SERVER_URL = "https://lagosa-home-manager.herokuapp.com/";
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public void registerFamily(String email, String password){
        String url = SERVER_URL + "authentication/registerFamily/" + email + "/" + password;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UUID familyId = UUID.fromString(response.getString("UUID"));
                            Log.w("=> CreateFamily","Got familyId! " + familyId);

                            getJoinCode(familyId);
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

    public void getJoinCode(UUID familyId){

        String url = SERVER_URL + "authentication/registerFamily/"+ familyId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int joinCode = Integer.parseInt(response);
                goToNewPage(JoinFamily.class,joinCode);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong while getting join code!",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    public Map<String,Object> getUser(UUID userId){
        String url = SERVER_URL + "authentication/getUserData/" + userId.toString();

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONArray jsonArray = JSONArray
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
        return null;
    }

    private void goToNewPage(Class myClass,int joinCode) {
        Intent intent = new Intent(context, myClass);
        Bundle bundle = new Bundle();
        bundle.putInt("joinCode",joinCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
        finish();
    }
}
