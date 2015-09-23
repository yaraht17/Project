package com.fourlines.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.NotificationAdapter;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.model.NotificationItem;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {


    private ArrayList<NotificationItem> notifList;
    private ListView notifListView;
    private NotificationAdapter notifListAdapter;
    private View rootView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_notification,
                container, false);

        notifListView = (ListView) rootView.findViewById(R.id.listNotif);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        loading();

        notifList = new ArrayList<>();
        if (Data.notifList != null) {
            progressBar.setVisibility(View.GONE);
            notifList = Data.notifList;
            notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
            notifListView.setAdapter(notifListAdapter);
        } else {
            loading();
        }


        notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
        notifListView.setAdapter(notifListAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                loading();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }

    private void loading() {
        getNotif("1", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject respond) {
                notifListAdapter.clear();
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                notifList = convertResponseToArray(respond);
                Data.notifList = notifList;
                notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
                notifListView.setAdapter(notifListAdapter);
            }
        });
    }

    public void getNotif(String page, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.URL_GET_TIPS + page, null, new Response.Listener<JSONObject>() {

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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

    //convert json object to array list
    public ArrayList<NotificationItem> convertResponseToArray(JSONObject response) {
        ArrayList<NotificationItem> list = new ArrayList<>();

        try {
            JSONArray array = response.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                final NotificationItem item = new NotificationItem(object.getString(Var.ID),
                        0, object.getString(Var.NOTIF_TITLE),
                        object.getString(Var.NOTIF_TOPIC), object.getString(Var.NOTIF_CONTENT), object.getString(Var.NOTIF_TOPIC));
                list.add(item);
                Log.d("TienDH", item.getTitle());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


}
