package com.fourlines.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fourlines.adapter.InfoAdapter;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.doctor.LoginActivity;
import com.fourlines.doctor.R;
import com.fourlines.model.InfoItem;
import com.fourlines.model.SickItem;
import com.fourlines.model.UserItem;
import com.fourlines.volley.ConnectServer;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ArrayList<InfoItem> infoList;
    private ArrayList<SickItem> sickList;
    private ListView infoListView, sickHistoryListView;
    private InfoAdapter infoAdapter;
    private TextView iconLogout;
    private String accessToken;
    Typeface font_awesome;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    private LinearLayout btnLogout;
    private TextView txtAlert;
    private UserItem user;
    private TextView txtUserName, txtAge, txtEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        infoListView = (ListView) rootView.findViewById(R.id.listInfo);
        iconLogout = (TextView) rootView.findViewById(R.id.iconLogout);
        txtUserName = (TextView) rootView.findViewById(R.id.userName);
        txtAge = (TextView) rootView.findViewById(R.id.ageUser);
        txtEmail = (TextView) rootView.findViewById(R.id.emailUser);
        btnLogout = (LinearLayout) rootView.findViewById(R.id.logout);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        sickHistoryListView = (ListView) rootView.findViewById(R.id.sickHistoryList);
        txtAlert = (TextView) rootView.findViewById(R.id.txtAlert);

        if (sickList == null) {
            ViewGroup.LayoutParams params = sickHistoryListView.getLayoutParams();
            params.height = 150;
            sickHistoryListView.setLayoutParams(params);
            sickHistoryListView.requestLayout();
            txtAlert.setVisibility(View.VISIBLE);
        } else {
            txtAlert.setVisibility(View.INVISIBLE);
        }
        btnLogout.setOnClickListener(this);

        font_awesome = Typeface.createFromAsset(rootView.getContext().getAssets(), "fontawesome-webfont.ttf");

        iconLogout.setTypeface(font_awesome);
        iconLogout.setText(getString(R.string.logout));

        infoList = createInfo();
        //get token
        sharedPreferences = rootView.getContext().getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
//        String fullname = sharedPreferences.getString(Var.FULLNAME, "");
        if (Data.user != null) {
            progressBar.setVisibility(View.GONE);
            user = Data.user;
            txtEmail.setText(user.getEmail());
            txtUserName.setText(user.getFullname());
        } else {
            loading();
        }

        infoAdapter = new InfoAdapter(rootView.getContext(), R.layout.item_info, infoList);
        infoListView.setAdapter(infoAdapter);
        return rootView;
    }

    private void loading() {
        final ConnectServer con = new ConnectServer(getContext());
        con.getDataAccount(accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject respond) {
                Log.d("TienDH", respond.toString());
                try {
                    progressBar.setVisibility(View.GONE);
                    UserItem userItem = con.responseToObject(respond);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(Var.FULLNAME, userItem.getFullname());
                    //put status
//                    editor.commit();
                    user = new UserItem(userItem.getId(), userItem.getEmail(), userItem.getFullname(), userItem.getList());
                    Data.user = user;
                    txtEmail.setText(user.getEmail());
                    txtUserName.setText(user.getFullname());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList createInfo() {
        ArrayList list = new ArrayList();
        list.add(new InfoItem(getString(R.string.height), "Chiều cao: 180cm"));
        list.add(new InfoItem(getString(R.string.weight), "Cân nặng: 60kg"));
        list.add(new InfoItem(getString(R.string.bmi), "BMI: 19.7"));
        list.add(new InfoItem(getString(R.string.heart), "Nhịp tim: 70"));
        list.add(new InfoItem(getString(R.string.foot), "Số bước chân: 1800"));
        return list;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
            final ConnectServer con = new ConnectServer(getContext());
            con.logout(accessToken, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject respond) {
                    sharedPreferences.edit().remove(Var.ACCESS_TOKEN).commit();
                    Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }
}
