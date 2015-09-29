package com.fourlines.volley;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.data.Var;
import com.fourlines.model.MedicalHistory;
import com.fourlines.model.HistoryItem;
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
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Res Error" + error.toString());
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
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

    public void validateToken(final String accessToken, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.URL_VALIDATE_TOKEN, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LinhTh", error.toString());
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
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

    public HistoryItem responseToObject(JSONObject response) {

        JSONObject object = null;
        String id = null;
        String email = null;
        JSONArray array = null;
        String fullname = null;
        String avatarUrl = null;
        HistoryItem sickHistoryItem = new HistoryItem();
        try {
            object = response.getJSONObject("result");
            id = object.getString(Var.ID);
            email = object.getString(Var.EMAIL);
            array = object.getJSONArray(Var.SICKS);
            sickHistoryItem = convertArrayToSickHistoryItem(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            fullname = object.getString(Var.FULLNAME);
        } catch (JSONException e) {
            fullname = null;
//            e.printStackTrace();
        }
        try {
            avatarUrl = object.getString(Var.AVATAR);
        } catch (JSONException e) {
            avatarUrl = null;
//            e.printStackTrace();
        }
        UserItem data = new UserItem(id, email, fullname, avatarUrl, arrayToArraylist(array));
        sickHistoryItem.setUserItem(data);

        return sickHistoryItem;
    }

    public HistoryItem convertArrayToSickHistoryItem(JSONArray array) {
        HistoryItem items = new HistoryItem();
        ArrayList<SickItem> sickItems = new ArrayList<>();
        ArrayList<String> datetimeItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                JSONObject object1 = object.getJSONObject("detail");
                SickItem sickItem = new SickItem(object1.getString(Var.ID), object1.getString(Var.SICK_NAME),
                        object1.getString(Var.SICK_TYPE), object1.getString(Var.SICK_REASON), object1.getString(Var.SICK_FOODS),
                        object1.getString(Var.SICK_BAN_FOODS), convertToList(object1.getJSONArray(Var.SICK_SYMPTOMS)),
                        object1.getString(Var.SICK_TREATMENT), object1.getString(Var.SICK_DESCRIPTION), object1.getString(Var.SICK_PREVENTION));
                String datetime = object.getString("date");
                sickItems.add(sickItem);
                datetimeItems.add(datetime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        items = new HistoryItem(new UserItem(), sickItems, datetimeItems);
        if (items.getSicks().size() != 0) {
            Log.d("TienDH", String.valueOf(items.getSicks().get(0).getName()));
            Log.d("TienDH", String.valueOf(items.getDatetimes().get(0)));
        }
        return items;
    }

    public ArrayList<MedicalHistory> arrayToArraylist(JSONArray array) {
        ArrayList<MedicalHistory> list = new ArrayList<>();
        return list;
    }
}

