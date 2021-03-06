package com.fourlines.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.fourlines.connection.ConnectionDetector;
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
    private LinearLayout alertConnection;
    private RelativeLayout layoutBg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sick_list,
                container, false);
        alertConnection = (LinearLayout) rootView.findViewById(R.id.alertConnection);
        edtSearchSick = (EditText) rootView.findViewById(R.id.edt_lsf_search);
        imgClearText = (ImageView) rootView.findViewById(R.id.img_lsf_remove_text);
        listResultSearch = (ListView) rootView.findViewById(R.id.lv_lsf_result_search);
        layoutMenu = (LinearLayout) rootView.findViewById(R.id.layout_menu);
        layoutResult = (RelativeLayout) rootView.findViewById(R.id.layout_result_search);
        gridView = (GridView) rootView.findViewById(R.id.sickTypeList);
        layoutBg = (RelativeLayout) rootView.findViewById(R.id.layoutBg);
        edtSearchSick.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }

            private void hideKeyboard() {
                if (edtSearchSick != null) {
                    InputMethodManager imanager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imanager.hideSoftInputFromWindow(edtSearchSick.getWindowToken(), 0);
                }
            }
        });
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        //cho nay nhe
        int width = (screenWidth - 20) / 2;
        int height = (screenHeight / 4 ) + 10;
        sickTypeList = createSickType();
        gridView.setAdapter(new ImageAdapter(rootView.getContext(), width, height, sickTypeList));
        imgClearText.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        if (Data.sickList != null) {
            sickResultItems = Data.sickList;
            adapterSickList = new SickListAdapter(rootView.getContext(),
                    R.layout.fragment_sick_list, sickResultItems);
            listResultSearch.setAdapter(adapterSickList);
        } else {
            loading();
        }
        action();
        return rootView;
    }

    private void loading() {
        final ConnectServer con = new ConnectServer(getContext());
        if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
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
        }
    }

    public static float convertDpToPixels(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static float convertPixelsToDp(int px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;

    }

    public void action() {
        listResultSearch.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                sendSickDetail(adapterSickList.getItem(arg2), rootView);// send

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
            public void afterTextChanged(Editable arg0) {
                String text = arg0.toString().toLowerCase(Locale.getDefault());
                if (text.equals("")) {
                    imgClearText.setVisibility(ImageView.INVISIBLE);
                    layoutMenu.setVisibility(RelativeLayout.VISIBLE);
                    layoutResult.setVisibility(RelativeLayout.INVISIBLE);
                } else {
                    if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
                        alertConnection.setVisibility(View.GONE);
                    } else {
                        alertConnection.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(delay, 6 * 1000);
                    }
                    if (sickResultItems != null) {
                        adapterSickList.setSickList(sickResultItems);
                        adapterSickList.filter(text);
                    }
                    imgClearText.setVisibility(ImageView.VISIBLE);
                    layoutResult.setVisibility(RelativeLayout.VISIBLE);
                    layoutMenu.setVisibility(RelativeLayout.INVISIBLE);

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imanager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imanager.hideSoftInputFromWindow(edtSearchSick.getWindowToken(), 0);
    }

    private void intentAction(int index) {
        Intent intent = new Intent(rootView.getContext(),
                SickListActivity.class);
        intent.putExtra(Var.SICK_TYPE_KEY, index);
        startActivity(intent);
    }

    private ArrayList<SickType> createSickType() {
        ArrayList list = new ArrayList();
        list.add(new SickType(0, R.drawable.tuanhoan, "Tuần Hoàn"));
        list.add(new SickType(1, R.drawable.xuongkhop, "Xương Khớp"));
        list.add(new SickType(2, R.drawable.tieuhoa, "Tiêu Hóa"));
        list.add(new SickType(3, R.drawable.ngoaida, "Ngoài Da"));
        list.add(new SickType(4, R.drawable.hohap, "Hô Hấp"));
        list.add(new SickType(5, R.drawable.thankinh, "Thần Kinh"));
        return list;

    }

    public void sendSickDetail(SickItem item, View rootView) {// send sick
        Intent intent = new Intent(rootView.getContext(), SickDetailActivity.class);
        intent.putExtra(Var.SICK_KEY, item);
        startActivity(intent);
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

    Handler mHandler = new Handler();

    private Runnable delay = new Runnable() {
        public void run() {
            alertConnection.setVisibility(View.GONE);
        }
    };
}
