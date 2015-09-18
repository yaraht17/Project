package com.fourlines.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fourlines.adapter.ImageAdapter;
import com.fourlines.adapter.SickListAdapter;
import com.fourlines.adapter.SickTypeListAdapter;
import com.fourlines.data.Data;
import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.doctor.SickDetailActivity;
import com.fourlines.doctor.SickListActivity;
import com.fourlines.model.SickItem;
import com.fourlines.model.SickType;
import com.fourlines.volley.ConnectServer;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

public class SickListFragment extends Fragment implements OnClickListener, OnItemClickListener {

    private ListView listResultSearch;
    private EditText edtSearchSick;// search sick folow name
    private SickListAdapter adapterSickList;
    private ArrayList<SickItem> sickResultItems;// list sick in database
    private ImageView imgClearText;// delete all text in edittext search
    private LinearLayout layoutMenu;
    private RelativeLayout layoutResult;
    private ImageView img1, img2, img3, img4, img5, img6;
    private GridView gridView;
    private ArrayList<SickType> sickTypeList;
    private SickTypeListAdapter sickTypeAdapter;
    private View rootView;
    private int screenWidth, screenHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sick_list,
                container, false);
        edtSearchSick = (EditText) rootView.findViewById(R.id.edt_lsf_search);
        imgClearText = (ImageView) rootView.findViewById(R.id.img_lsf_remove_text);
        listResultSearch = (ListView) rootView.findViewById(R.id.lv_lsf_result_search);
        layoutMenu = (LinearLayout) rootView.findViewById(R.id.layout_menu);
        layoutResult = (RelativeLayout) rootView.findViewById(R.id.layout_result_search);
        gridView = (GridView) rootView.findViewById(R.id.sickTypeList);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        int cat_size = (screenWidth - 80) / 2;
        sickTypeList = createSickType();
        //sickTypeAdapter = new SickTypeListAdapter(rootView.getContext(), R.layout.item_sick_type, sickTypeList);
        gridView.setAdapter(new ImageAdapter(rootView.getContext(), cat_size, sickTypeList));
        imgClearText.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        final ConnectServer con = new ConnectServer(getContext());
        try {
            con.getSick("all", "", new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject respond) {
                    sickResultItems = con.convertResponseToArray(respond);
                    Data.sickList = sickResultItems;
                    adapterSickList = new SickListAdapter(rootView.getContext(),
                            R.layout.fragment_sick_list, sickResultItems);
                    listResultSearch.setAdapter(adapterSickList);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        action();
        return rootView;
    }

    public void action() {
        listResultSearch.setOnItemClickListener(new OnItemClickListener() {// action
            // when
            // click
            // item in
            // listview

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                sendSickDetail(sickResultItems.get(arg2), rootView);// send
                // id
                // sick
                // item to sick
                // detail
                // activity
            }
        });
        edtSearchSick.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {// change list view
                // when input text
                // to edittext
                // search
                String text = arg0.toString().toLowerCase(Locale.getDefault());
                if (text.equals("")) {
                    imgClearText.setVisibility(ImageView.INVISIBLE);
                    layoutMenu.setVisibility(RelativeLayout.VISIBLE);
                    layoutResult.setVisibility(RelativeLayout.INVISIBLE);
                } else {
//                    sickResultItems = loadData();
//                    adapterSickList = new SickListAdapter(rootView.getContext(),
//                            R.layout.fragment_sick_list, sickResultItems);
//                    listResultSearch.setAdapter(adapterSickList);
//                    adapterSickList.filter(text);
                    imgClearText.setVisibility(ImageView.VISIBLE);
                    layoutResult.setVisibility(RelativeLayout.VISIBLE);
                    layoutMenu.setVisibility(RelativeLayout.INVISIBLE);
                }
            }
        });


    }

    private void intentAction(int index) {
        Intent intent = new Intent(rootView.getContext(),
                SickListActivity.class);
        intent.putExtra(Var.SICK_TYPE_KEY, index);
        startActivity(intent);
    }

    private ArrayList<SickType> createSickType() {
        ArrayList list = new ArrayList();
        SickType sickType = new SickType(1, R.drawable.chude1, "Hô Hấp");
        list.add(sickType);
        list.add(new SickType(2, R.drawable.chude1, "Tuần Hoàn"));
        list.add(new SickType(3, R.drawable.chude1, "Tiêu Hóa"));
        list.add(new SickType(4, R.drawable.chude1, "Tiết Niệu"));
        list.add(new SickType(5, R.drawable.chude1, "Xương Khớp"));
        list.add(new SickType(6, R.drawable.chude1, "Thần Kinh"));
        return list;

    }

    public void sendSickDetail(SickItem item, View rootView) {// send sick
        Intent intent = new Intent(rootView.getContext(), SickDetailActivity.class);
        intent.putExtra(Var.SICK_KEY, item);
        startActivity(intent);
    }

    public ArrayList<SickItem> loadData() {// load data sick to
        // server
        ArrayList<SickItem> items = new ArrayList<>();
        items = Data.sickListAll;
        return items;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_lsf_remove_text:
                edtSearchSick.setText("");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intentAction(position);
    }
}
