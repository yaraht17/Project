package com.fourlines.doctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.TabsPagerAdapter;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.model.MedicalHistory;
import com.fourlines.model.UserItem;
import com.fourlines.view.PagerSlidingTabStrip;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //tab layout
    private ViewPager viewPager;
    private TabsPagerAdapter mTabsAdapter;
    private PagerSlidingTabStrip slideTabs;
    private String accessToken;
    Typeface font_awesome;

    SharedPreferences sharedPreferences;

    private UserItem user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        slideTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);


        //get token
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
//        String fullname = sharedPreferences.getString(Var.FULLNAME, "");
        getDataAccount(accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject respond) {
                Log.d("TienDH", respond.toString());
                try {
                    UserItem userItem = responseToObject(respond);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(Var.FULLNAME, userItem.getFullname());
                    //put status
//                    editor.commit();

                    user = new UserItem(userItem.getId(), userItem.getEmail(), userItem.getFullname(), userItem.getList());
                    Data.user = user;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mTabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mTabsAdapter);

        //ve fragment hien tai
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (getIntent().getStringExtra("Page") != null) {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("Page")));
        }
        slideTabs.setViewPager(viewPager);
//        slideTabs.setTextColorResource(R.color.white);
//        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
//        slideTabs.setTypeface(font_awesome, Typeface.NORMAL);

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
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
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
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
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
