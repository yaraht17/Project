package com.fourlines.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.CustomDrawerAdapter;
import com.fourlines.adapter.TabsPagerAdapter;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.model.DrawerItem;
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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //tab layout
    private ViewPager viewPager;
    private TabsPagerAdapter mTabsAdapter;
    private PagerSlidingTabStrip slideTabs;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CustomDrawerAdapter drawerAdapter;
    private List<DrawerItem> drawerDataList;
    private String accessToken;

    SharedPreferences sharedPreferences;
    private Button btnNavigation;

    private UserItem user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        btnNavigation = (Button) findViewById(R.id.btnMenuNavigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        slideTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //get token
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        getDataAccount(accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject respond) {
                Log.d("TienDH", respond.toString());
                try {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    UserItem userItem = responseToObject(respond);
                    Log.d("TienDH", "fullname: " + userItem.getFullname());
                    user = new UserItem(userItem.getId(), userItem.getEmail(), userItem.getFullname(), userItem.getList());
                    Log.d("TienDH", "fullname: " + user.getFullname());
                    Data.user = user;
                    drawerDataList = initDrawerData(user);
                    drawerAdapter = new CustomDrawerAdapter(getApplicationContext(), R.layout.custom_drawer_item,
                            drawerDataList);
                    mDrawerList.setAdapter(drawerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        if (savedInstanceState == null) {
//            selectItem(0);
//        }

        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
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


    }


    private ArrayList initDrawerData(UserItem user) {
        ArrayList list = new ArrayList();
        list.add(new DrawerItem(user.getFullname(), "Tiểu đường", R.drawable.avatar_user));
        list.add(new DrawerItem("Chỉnh sửa thông tin", R.drawable.ic_edit_profile));
        list.add(new DrawerItem("Thông báo mới", R.drawable.ic_notif));
        list.add(new DrawerItem("Cài đặt", R.drawable.ic_setting));
        list.add(new DrawerItem("Đăng xuất", R.drawable.ic_setting));
        return list;
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch (position) {
                case 0:
                    intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    //logout
                    logout(accessToken, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject respond) {
                            sharedPreferences.edit().remove(Var.ACCESS_TOKEN).commit();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            }

        }
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
