package com.fourlines.doctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.fourlines.adapter.TabsPagerAdapter;
import com.fourlines.data.DatabaseChat;
import com.fourlines.data.DatabaseNotif;
import com.fourlines.data.Var;
import com.fourlines.view.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {
    //tab layout
    private ViewPager viewPager;
    private TabsPagerAdapter mTabsAdapter;
    private PagerSlidingTabStrip slideTabs;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private DatabaseChat dbChat;
    private DatabaseNotif dbNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbChat = new DatabaseChat(getApplicationContext());
        dbNotif = new DatabaseNotif(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        slideTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        mTabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mTabsAdapter);

        //registerReceiver(broadcastReceiver, new IntentFilter(Var.INTERNET_CHECK));
        //ve fragment hien tai
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (getIntent().getStringExtra("Page") != null) {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("Page")));
            Log.d("FRAG", "page" + viewPager.getCurrentItem());
        }
        slideTabs.setViewPager(viewPager);
    }

//    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
//                final ConnectServer con = new ConnectServer(getApplicationContext());
//                con.getDataAccount(accessToken, new VolleyCallback() {
//                    @Override
//                    public void onSuccess(JSONObject respond) {
//                        try {
//                            if (respond.getString("status").equals("fail")) {
//                                //sai token
//                                sharedPreferences.edit().remove(Var.ACCESS_TOKEN).commit();
//                                Intent intent = new Intent(MainActivity.this, SqlashScreen.class);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                //dung token
//                                JSONObject object = respond.getJSONObject("result");
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString(Var.FULLNAME, object.getString(Var.FULLNAME));
//                                editor.putString(Var.EMAIL, object.getString(Var.EMAIL));
//                                editor.putString(Var.AVATAR, object.getString(Var.AVATAR));
//                                editor.commit();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }
//
//    };
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(broadcastReceiver);
//    }
}