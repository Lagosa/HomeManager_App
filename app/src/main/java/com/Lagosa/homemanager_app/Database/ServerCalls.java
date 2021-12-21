package com.Lagosa.homemanager_app.Database;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Lagosa.homemanager_app.MainActivity;
import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.Chores.Chore;
import com.Lagosa.homemanager_app.ui.Reports.Report;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void getAllNotDoneChores(ChoreNotDoneListCallback callback, UUID userId){
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

    public void getChoreTypes(ChoreTypesListCallback callback){
        String url =  SERVER_URL + "chore/getChoreTypes";
        Log.w("CreateChore","Fetching data from DB");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<String> types = new ArrayList<>();
                Log.w("CreateChore","Got data, size: " + response.length());
                try {
                    for(int i = 0; i < response.length();i++){
                        types.add(response.getJSONObject(i).get("type").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.setChoreTypesList(types);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void createChore(UUID userId, Date deadline, String type, String title, String description){
        String url = SERVER_URL + "chore/create/"+userId+"/"+deadline.toString()+"/"+type+"/"+title;
        Log.w("CreateChore","Creating chore: " + url);

        JSONObject object = new JSONObject();
        try {
            object.put("description",description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("CreateChore","Chore created!  " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void deleteChore(int choreId, UUID userId){
        String url = SERVER_URL + "chore/delete/" + choreId + "/" + userId;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("DeleteChore","Chore deleted!");
                Toast.makeText(context,"Chore deleted!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Can't delete chore! You are not the owner!",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    public void takeUpChore(int choreId, UUID userId){
        String url = SERVER_URL + "chore/take/" + choreId + "/" + userId;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Chore took up!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    public void listMyChores(ChoreMyChoresCallback callback, UUID userId){
        String url = SERVER_URL + "chore/tookUpChores/" + userId;
        List<Chore> myChores=  new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Chore chore = new Chore(jsonObject.getString("submitterName"),Date.valueOf(jsonObject.getString("submissionDate")),Date.valueOf(jsonObject.getString("dueDate")),
                                jsonObject.getString("typeName"),jsonObject.getString("description"),jsonObject.getString("title"));
                        chore.setId(jsonObject.getInt("id"));
                        chore.setDoneByName(jsonObject.getString("doneByName"));
                        myChores.add(chore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.gotMyChores(myChores);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void markChoreAsDone(int choreId, UUID userId){
        String url = SERVER_URL + "chore/markDone/" + choreId + "/" + userId;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Chore marked as done!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void getReport(ReportCallback callback, UUID userId){
        String url = SERVER_URL + "chore/getReport/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Report> reportList = new ArrayList<>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject userObject = response.getJSONObject(i);

                        List<Map<String,Object>> choresDoneList = fetchReportChore(userObject.getJSONArray("choresDone"));

                        List<Map<String,Object>> choresNotDoneList = fetchReportChore(userObject.getJSONArray("choresTookUpAndNotDone"));

                        JSONObject user = userObject.getJSONObject("user");
                        Report report = new Report(user.getString("nickName"),userObject.getInt("nrOfDoneChores"),userObject.getInt("nrOfNotFinishedChores"),
                                choresDoneList,choresNotDoneList);
                        reportList.add(report);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.gotReport(reportList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void getMementos(MementoCallback callback, UUID userId){
        String url = SERVER_URL + "chore/getMementos/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Map<String,Object>> mementoList = new ArrayList<>();

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Map<String,Object> memento = new HashMap<>();
                        memento.put("title",object.getString("title"));
                        memento.put("dueDate",Date.valueOf(object.getString("dueDate")));
                        mementoList.add(memento);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                callback.gotMementos(mementoList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void createMemento(UUID userId,String title, Date dueDate){
        String url = SERVER_URL + "chore/addMemento/"+userId+"/"+title+"/"+dueDate;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Memento created!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void getAllDishes(DishCallback callback, UUID userId){
        String url = SERVER_URL + "dish/getDishes/"+userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Map<String,Object>> dishList = new ArrayList<>();

                for(int i = 0; i<response.length();i++){
                    try {
                        JSONObject dishObject = response.getJSONObject(i);

                        Map<String,Object> dishMap = new HashMap<>();
                        dishMap.put("id",dishObject.getString("id"));
                        dishMap.put("name",dishObject.getString("name"));
                        dishMap.put("submitterName",dishObject.getString("submitterName"));
                        dishMap.put("type",dishObject.getString("typeName"));
                        dishMap.put("recipe",dishObject.getString("recipe"));
                        dishMap.put("visibility",dishObject.getString("visibility"));
                        dishMap.put("nrTimesMade",dishObject.getString("nrTimesMade"));

                        List<Map<String,Object>> ingredientsList = new ArrayList<>();
                        JSONArray ingredientsListJson = dishObject.getJSONArray("ingredients");
                        for(int j = 0; j<ingredientsListJson.length(); j++){
                            JSONObject ingredientObject = ingredientsListJson.getJSONObject(j);
                            Map<String,Object> ingredient = new HashMap<>();
                            ingredient.put("name",ingredientObject.getString("name"));
                            ingredient.put("quantity",ingredientObject.getString("quantity"));
                            ingredient.put("measurementUnit",ingredientObject.getString("measurementUnit"));

                            ingredientsList.add(ingredient);
                        }

                        dishMap.put("ingredients",ingredientsList);
                        dishList.add(dishMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.gotAllDishes(dishList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void getIngredients(IngredientCallback callback){
        String url = SERVER_URL + "dish/getAllIngredients";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Map<String,String>> ingredientList = new ArrayList<>();
                for(int i = 0; i< response.length();i++){
                    try {
                        JSONObject ingredientObject = response.getJSONObject(i);
                        Map<String,String> ingredientMap = new HashMap<>();
                        ingredientMap.put("name",ingredientObject.get("name").toString());
                        ingredientMap.put("mu",ingredientObject.get("measurementUnit").toString());

                        ingredientList.add(ingredientMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.gotIngredients(ingredientList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void getDishTypes(DishCallback callback){
        String url = SERVER_URL + "dish/getTypes";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Map<String,Object>> dishTypes = new ArrayList<>();
                for(int i = 0; i< response.length(); i++){
                    try {
                        String type= response.getString(i);
                        Map<String,Object> typeMap = new HashMap<>();
                        typeMap.put("type",type);
                        dishTypes.add(typeMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.gotAllDishes(dishTypes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void insertDish(UUID userId, String name, String type, String visibility, String recipe, List<Map<String, Object>> selectedIngredients) {
        String url = SERVER_URL + "dish/insert/"+userId+"/"+name+"/"+type+"/"+visibility;
        JSONArray ingredientListObject = new JSONArray(selectedIngredients);
        Map<String,Object> mapToSend = new HashMap<>();
        mapToSend.put("recipe",recipe);
        mapToSend.put("ingredients",selectedIngredients);
        Log.w("CreateDish",selectedIngredients.toString());
        JSONObject objectToSend = new JSONObject(mapToSend);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, objectToSend,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context,"Dish created!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void markDone(int dishId){
        String url = SERVER_URL + "dish/dishMade/" + dishId;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Increased number of times made!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void modifyVisibility(int dishId, String visibility) {
        String url = SERVER_URL + "dish/changeVisibility/" + dishId + "/" + visibility;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Visibility modified to: " + visibility,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void getRandomDish(DishCallback callback,UUID userId, String type){
        String url = SERVER_URL + "dish/getRandom/"+userId+"/"+type;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Map<String,Object>> dishList = new ArrayList<>();
                Map<String,Object> dish = new HashMap<>();
                try {
                    dish.put("name",response.getString("name"));
                    dish.put("type",response.getString("typeName"));
                    dish.put("recipe",response.getString("recipe"));
                    dish.put("ingredients",response.get("ingredients"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dishList.add(dish);
                callback.gotAllDishes(dishList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void getPlannedDishes(PlannedDishList callback,UUID userId, Date startDate, Date endDate){
        String url = SERVER_URL + "dish/getPlan/"+userId+"/"+startDate+"/"+endDate;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Map<String,Object>> listPlannedDishes = new ArrayList<>();
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject plannedDishObject = response.getJSONObject(i);
                        Map<String,Object> plannedDish = new HashMap<>();
                        plannedDish.put("dishName",plannedDishObject.getString("dishName"));
                        plannedDish.put("typeName",plannedDishObject.getString("typeName"));
                        plannedDish.put("day",plannedDishObject.getString("day"));
                        listPlannedDishes.add(plannedDish);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.gotPlannedDishes(listPlannedDishes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public void createPlan(UUID userId, String dishId, Date day,int i, int total){
        String url = SERVER_URL + "dish/plan/"+userId+"/"+dishId+"/"+day;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Plan saved: " + i + "/" + total,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong at plan: " + i + "/" + total,Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    private List<Map<String,Object>> fetchReportChore(JSONArray jsonChoreList){
        List<Map<String,Object>> reportChores = new ArrayList<>();
        for(int j = 0; j < jsonChoreList.length(); j++){
            try {
                JSONObject taskObject = jsonChoreList.getJSONObject(j);
                taskObject = jsonChoreList.getJSONObject(j);

                Map<String,Object> taskData = new HashMap<>();
                taskData.put("title",taskObject.getString("title"));
                taskData.put("deadline",Date.valueOf(taskObject.getString("dueDate")));

                reportChores.add(taskData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reportChores;
    }


}
