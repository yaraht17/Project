package com.fourlines.doctor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.SickListAdapter;
import com.fourlines.connection.ConnectionDetector;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.model.SickItem;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SickListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<SickItem> sickList;
    private ListView sickListView;
    private SickListAdapter sickListAdapter;
    private Button btnBack;
    private ProgressBar progressBar;
    private TextView txtSickListName;
    private SwipeRefreshLayout swipeContainer;
    private int index;
    private LinearLayout alertConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_list);
        alertConnection = (LinearLayout) findViewById(R.id.alertConnection);
        sickListView = (ListView) findViewById(R.id.listSick);
        btnBack = (Button) findViewById(R.id.btnBack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        txtSickListName = (TextView) findViewById(R.id.txtSickListName);

        btnBack.setOnClickListener(this);
        sickListView.setOnItemClickListener(this);

        index = getIntent().getExtras().getInt(Var.SICK_TYPE_KEY);
        txtSickListName.setText(Var.SICKTYPE[index]);

        Log.d("TienDH", getLocalClassName());
        sickList = new ArrayList<>();
        if (Data.sicks[index] != null) {
            progressBar.setVisibility(View.GONE);
            sickList = Data.sicks[index];
            sickListAdapter = new SickListAdapter(getApplicationContext(), R.layout.item_sick_list, sickList);
            sickListView.setAdapter(sickListAdapter);
        } else {
            if (ConnectionDetector.isNetworkConnected(this)) {

                (new SickAsynTask()).execute();
            } else {
                alertConnection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

        }

        sickListAdapter = new SickListAdapter(getApplicationContext(), R.layout.item_sick_list, sickList);
        sickListView.setAdapter(sickListAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
                    (new SickAsynTask()).execute();

                } else {
                    alertConnection.setVisibility(View.VISIBLE);
                    swipeContainer.setRefreshing(false);
                }

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, SickDetailActivity.class);
        SickItem sickItem = sickList.get(position);
        intent.putExtra(Var.SICK_KEY, sickItem);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            finish();
        }
    }

    public class SickAsynTask extends AsyncTask<Void, Void, List<SickItem>> {


        @Override
        protected List<SickItem> doInBackground(Void... params) {
            ArrayList list = new ArrayList();
            try {
                getSick("type", Var.SICKTYPE[index], new VolleyCallback() {

                    //wait request success
                    @Override
                    public void onSuccess(JSONObject response) {
                        sickListAdapter.clear();
                        sickList = convertResponseToArray(response);
                        Data.sicks[index] = sickList;
                        swipeContainer.setRefreshing(false);
//                        sickListAdapter.setSickList(sickList);
//                        sickListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<SickItem> sickItems) {
            super.onPostExecute(sickItems);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //type = name or type or all
        //name = type_of_sick or name_of_sick
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

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("charset", "utf-8");
                    return headers;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sickListAdapter.add(sickItem);
                            sickListAdapter.notifyDataSetChanged();
                            sickListView.setAdapter(sickListAdapter);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
    }

}
