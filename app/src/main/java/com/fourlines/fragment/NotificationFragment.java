package com.fourlines.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.NotificationAdapter;
import com.fourlines.connection.ConnectionDetector;
import com.fourlines.data.Data;
import com.fourlines.data.DatabaseNotif;
import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.model.NotificationItem;
import com.fourlines.view.EndlessListView;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment implements EndlessListView.EndlessListener {


    private ArrayList<NotificationItem> notifList;
    private EndlessListView notifListView;
    private NotificationAdapter notifListAdapter;
    private View rootView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private static int page = 1;
    private boolean flagScroll = false;
    private LinearLayout alertConnection;
    private DatabaseNotif dbNotif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_notification,
                container, false);
        alertConnection = (LinearLayout) rootView.findViewById(R.id.alertConnection);
        notifListView = (EndlessListView) rootView.findViewById(R.id.listNotif);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        notifListView.setLoadingView(R.layout.progress_bar_footer);
        notifListView.setListener(this); //listen for a scroll movement to the bottom


        notifList = new ArrayList<>();
        notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
        notifListView.setAdapter(notifListAdapter);
        //kiem tra list da dc luu chua;
        if (Data.notifList != null) {
            progressBar.setVisibility(View.GONE);
            notifList = Data.notifList;
            notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
            notifListView.setAdapter(notifListAdapter);
        } else {
            //chua thi load server
            if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
                loading(String.valueOf(page));
            } else {
                alertConnection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                mHandler.postDelayed(delay, 6 * 1000);
                Log.d("TienDH", "No Internet");
                dbNotif = new DatabaseNotif(getContext());
                notifList = dbNotif.getNotifList();
                dbNotif.closeBD();
                notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
                notifListView.setAdapter(notifListAdapter);

            }
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                flagScroll = false;
                notifListView.setListener(NotificationFragment.this);

                if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
                    page = 1;
                    loading(String.valueOf(page));
                } else {
                    Log.d("TienDH", "Hien thi alert");
                    alertConnection.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(delay, 6 * 1000);
                    swipeContainer.setRefreshing(false);

                }
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return rootView;
    }

    private void loading(String page) {
        final int pageNumber = Integer.parseInt(page);
        getNotif(page, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject respond) {
                        if (flagScroll == true) {
                            notifListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
                        } else {
                            notifListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                            notifListAdapter.clear();
                        }
                        progressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        ArrayList newData = convertResponseToArray(respond);
                        if (newData != null) {
                            notifListView.addNewData(newData);
                            Data.notifList = notifList;
                        } else {
                            notifListView.removeLoadingView();
                        }
                        if (pageNumber == 1) {
                            Log.d("TienDH", "Save DB");
                            dbNotif = new DatabaseNotif(getContext());
                            dbNotif.dropTable();
                            dbNotif = new DatabaseNotif(getContext());
                            for (int i = 0; i < notifList.size(); i++) {
                                dbNotif.insertNotifItem(notifList.get(i));
                            }
                            dbNotif.closeBD();
                        }
                    }
                }

        );
    }

    public void getNotif(String page, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.URL_GET_TIPS + page, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Res Error" + error.toString());
                        Toast.makeText(getContext(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
//        notifListView.setAdapter(notifListAdapter);
//    }

    //convert json object to array list
    public ArrayList<NotificationItem> convertResponseToArray(JSONObject response) {
        ArrayList<NotificationItem> list = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray("data");
            if (array.length() == 0) {
                return null;
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                final NotificationItem item = new NotificationItem(object.getString(Var.ID), 0,
                        object.getString(Var.NOTIF_TITLE), object.getString(Var.NOTIF_CONTENT), object.getString(Var.NOTIF_TOPIC));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void loadData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flagScroll = true;

        if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
            page++;
            loading(String.valueOf(page));
        } else {
            alertConnection.setVisibility(View.VISIBLE);
            mHandler.postDelayed(delay, 8 * 1000);
            notifListView.removeLoadingView();
        }
    }

    Handler mHandler = new Handler();

    private Runnable delay = new Runnable() {
        public void run() {
            alertConnection.setVisibility(View.GONE);
        }
    };
}
