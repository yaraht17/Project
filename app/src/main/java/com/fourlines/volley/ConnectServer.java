package com.fourlines.volley;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.data.Var;
import com.fourlines.model.MedicalHistory;
import com.fourlines.model.SickItem;
import com.fourlines.model.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectServer {
    private Context context;

    public ConnectServer(Context context) {
        this.context = context;
    }

    public void getSick(String type, String tmp, final VolleyCallback callback) throws UnsupportedEncodingException {
        String url = "";
        String query = URLEncoder.encode(tmp, "utf-8");

        if (type.equals("name")) {
            url = Var.URL_GET_SICK_BY_NAME + query;
        } else if (type.equals("type")) {
            url = Var.URL_GET_SICK_BY_TYPE + query;
        } else if (type.equals("all")) {
            url = Var.URL_GET_ALL_SICK;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TienDH", "onRes : " + response.toString());
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Res Error" + error.toString());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    //convert json array to arraylist
    public ArrayList<String> convertToList(JSONArray a) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {
            try {
                list.add(a.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    //convert json object to array list
    public ArrayList<SickItem> convertResponseToArray(JSONObject response) {
        ArrayList<SickItem> list = new ArrayList<>();

        try {
            JSONArray array = response.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                final SickItem sickItem = new SickItem(object.getString(Var.ID), object.getString(Var.SICK_NAME),
                        object.getString(Var.SICK_TYPE), object.getString(Var.SICK_REASON), object.getString(Var.SICK_FOODS),
                        object.getString(Var.SICK_BAN_FOODS), convertToList(object.getJSONArray(Var.SICK_SYMPTOMS)),
                        object.getString(Var.SICK_TREATMENT), object.getString(Var.SICK_DESCRIPTION), object.getString(Var.SICK_PREVENTION));
                list.add(sickItem);
                Log.d("TienDH", sickItem.getName());
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        sickListAdapter.add(sickItem);
//                        sickListAdapter.notifyDataSetChanged();
//                        sickListView.setAdapter(sickListAdapter);
//                    }
//                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void logout(final String accessToken, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.URL_LOGOUT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LinhTh", error.toString());
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void getDataAccount(final String accessToken, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.URL_GET_DATA_ACCOUNT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LinhTh", error.toString());
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public UserItem responseToObject(JSONObject response) throws JSONException {

        JSONObject object = response.getJSONObject("result");
        String id = object.getString(Var.ID);
        String email = object.getString(Var.EMAIL);
        String fullname = object.getString(Var.FULLNAME);
        JSONArray array = object.getJSONArray(Var.SICKS);
        UserItem data = new UserItem(id, email, fullname, arrayToArraylist(array));

        return data;
    }

    public ArrayList<MedicalHistory> arrayToArraylist(JSONArray array) {
        ArrayList<MedicalHistory> list = new ArrayList<>();
        return list;
    }
}

