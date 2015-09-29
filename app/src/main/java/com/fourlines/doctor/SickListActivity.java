package com.fourlines.doctor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fourlines.adapter.SickListAdapter;
import com.fourlines.connection.ConnectionDetector;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.model.SickItem;
import com.fourlines.volley.ConnectServer;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
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

        sickList = new ArrayList<>();
        if (Data.sicks[index] != null) {
            progressBar.setVisibility(View.GONE);
            sickList = Data.sicks[index];
            sickListAdapter = new SickListAdapter(getApplicationContext(), R.layout.item_sick_list, sickList);
            sickListView.setAdapter(sickListAdapter);
        } else {
            if (ConnectionDetector.isNetworkConnected(this)) {
                loading();
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
                    loading();
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

    private void loading() {
        final ConnectServer con = new ConnectServer(this);
        try {
            con.getSick("type", Var.SICKTYPE[index], new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    sickListAdapter.clear();
                    sickList = con.convertResponseToArray(response);
                    Data.sicks[index] = sickList;
                    swipeContainer.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    sickListAdapter = new SickListAdapter(getApplicationContext(),
                            R.layout.item_sick_list, sickList);
                    sickListView.setAdapter(sickListAdapter);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
