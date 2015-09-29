package com.fourlines.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.InfoAdapter;
import com.fourlines.adapter.SickHistoryListAdapter;
import com.fourlines.connection.ConnectionDetector;
import com.fourlines.data.Data;
import com.fourlines.data.DatabaseChat;
import com.fourlines.data.DatabaseNotif;
import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.doctor.SickDetailActivity;
import com.fourlines.doctor.SqlashScreen;
import com.fourlines.model.HistoryItem;
import com.fourlines.model.InfoItem;
import com.fourlines.model.SickHistoryItem;
import com.fourlines.model.SickItem;
import com.fourlines.model.UserItem;
import com.fourlines.volley.ConnectServer;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProfileFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {

    public static final String APP_TAG = "SimpleHealth";
    private final int MENU_ITEM_PERMISSION_SETTING = 1;

    private HealthDataStore mStore;
    private HealthConnectionErrorResult mConnError;
    private Set<HealthPermissionManager.PermissionKey> mKeySet;
    private ArrayList<String> listStepCount = new ArrayList<>();

    private static final String TAG = "TienDH";
    private View rootView;
    private ArrayList<InfoItem> infoList;
    private ArrayList<SickHistoryItem> sickList;
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
    private LinearLayout content;
    private ImageView imageProfile;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsResolving = false;
    private TextView txtAlertSHealth;
    private boolean mShouldResolve = false;
    private SickHistoryListAdapter sickHistoryListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Var.INTERNET_CHECK));
        infoListView = (ListView) rootView.findViewById(R.id.listInfo);
        iconLogout = (TextView) rootView.findViewById(R.id.iconLogout);
        txtUserName = (TextView) rootView.findViewById(R.id.userName);
        txtEmail = (TextView) rootView.findViewById(R.id.emailUser);
        btnLogout = (LinearLayout) rootView.findViewById(R.id.logout);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        sickHistoryListView = (ListView) rootView.findViewById(R.id.sickHistoryList);
        txtAlert = (TextView) rootView.findViewById(R.id.txtAlert);
        content = (LinearLayout) rootView.findViewById(R.id.content);
        imageProfile = (ImageView) rootView.findViewById(R.id.imageUser);
        txtAlertSHealth = (TextView) rootView.findViewById(R.id.txtAlertSHealth);
        btnLogout.setOnClickListener(this);
        font_awesome = Typeface.createFromAsset(rootView.getContext().getAssets(), "fontawesome-webfont.ttf");
        iconLogout.setTypeface(font_awesome);
        iconLogout.setText(getString(R.string.logout));
        sickHistoryListView.setOnItemClickListener(this);
        mKeySet = new HashSet<HealthPermissionManager.PermissionKey>();
        mKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE,
                HealthPermissionManager.PermissionType.READ));
        mKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.Weight.HEALTH_DATA_TYPE,
                HealthPermissionManager.PermissionType.READ));
        mKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE,
                HealthPermissionManager.PermissionType.READ));
        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(getContext(), mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(rootView.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
        mGoogleApiClient.connect();

        content.setVisibility(View.GONE);
        if (Data.sickHistoryList == null) {
            Log.d("TienDH", "sicks static null");
            ViewGroup.LayoutParams params = sickHistoryListView.getLayoutParams();
            params.height = 150;
            sickHistoryListView.setLayoutParams(params);
            sickHistoryListView.requestLayout();
            txtAlert.setText(getString(R.string.nosick));
            txtAlert.setVisibility(View.VISIBLE);
            sickHistoryListView.setVisibility(View.INVISIBLE);
        } else {
            Log.d("TienDH", "sicks static not null");
            sickHistoryListView.setVisibility(View.VISIBLE);
            txtAlert.setVisibility(View.INVISIBLE);
            //view listview
            sickList = Data.sickHistoryList;
            sickHistoryListAdapter = new SickHistoryListAdapter(getContext(), R.layout.item_sick_history, sickList);
            sickHistoryListView.setAdapter(sickHistoryListAdapter);
            ViewGroup.LayoutParams params = sickHistoryListView.getLayoutParams();
            params.height = sickHistoryListAdapter.getCount() * convertDpToPixels(40, getContext()) + 100;
            sickHistoryListView.setLayoutParams(params);
            sickHistoryListView.requestLayout();
        }
        infoList = new ArrayList<>();
        //get token
        sharedPreferences = rootView.getContext().getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        Log.d("TienDH", "acctoken: " + accessToken);
        if (Data.user != null) {
            content.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            user = Data.user;
            if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
                setInfoUser(user.getFullname(), user.getEmail(), user.getAvatarUrl());
            } else {
                setInfoUserOffline(user.getFullname(), user.getEmail());
            }

        } else {
            if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
                loading();
            } else {
                content.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                String email = sharedPreferences.getString(Var.EMAIL, "");
                String name = sharedPreferences.getString(Var.FULLNAME, "");
                //load in sharepre
                setInfoUserOffline(name, email);
                txtAlert.setText(getString(R.string.nointernet));
                txtAlert.setVisibility(View.VISIBLE);
            }
        }
        infoAdapter = new InfoAdapter(rootView.getContext(), R.layout.item_info, infoList);
        infoListView.setAdapter(infoAdapter);
        return rootView;
    }

    public static int convertDpToPixels(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectionDetector.isNetworkConnected(getContext())) {
            loading();
        }
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(getContext(), mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);

            try {
                // Check whether the permissions that this application needs are acquired
                Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(mKeySet);

                if (resultMap.containsValue(Boolean.FALSE)) {
                    // Request the permission for reading step counts if it is not acquired
                    pmsManager.requestPermissions(mKeySet).setResultListener(mPermissionListener);
                } else {
                    // Get the current step count and display it
                    start();
                }
            } catch (Exception e) {
                Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
                Log.e(APP_TAG, "Permission setting fails.");
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
        }
    };

    private final HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult> mPermissionListener =
            new HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult>() {

                @Override
                public void onResult(HealthPermissionManager.PermissionResult result) {
                    Log.d(APP_TAG, "Permission callback is received.");
                    Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = result.getResultMap();

                    if (resultMap.containsValue(Boolean.FALSE)) {
                        drawCalo("");
                        drawHeartRate("");
                        drawHeightWeight("");
                        showPermissionAlarmDialog();
                        txtAlertSHealth.setText("Không kết nối được SHealth");
                        txtAlertSHealth.setVisibility(View.VISIBLE);
                    } else {
                        // Get the current step count and display it
                        start();
                    }
                }
            };

    public void drawCalo(String count) {
        Log.d("TienDH", "so buoc chan: " + count);
        if (count.equals("")) return;
        infoList.add(new InfoItem(getString(R.string.foot), "Calo tiêu thụ: " + count + " Cal"));
        infoAdapter = new InfoAdapter(rootView.getContext(), R.layout.item_info, infoList);
        infoListView.setAdapter(infoAdapter);
    }

    public void drawHeartRate(String count) {
        if (count.equals("")) return;
        Log.d("TienDH", "nhip tim: " + count);
        infoList.add(new InfoItem(getString(R.string.heart), "Nhịp tim: " + count + " bpm"));
        infoAdapter = new InfoAdapter(rootView.getContext(), R.layout.item_info, infoList);
        infoListView.setAdapter(infoAdapter);
    }

    public void drawHeightWeight(String count) {
        if (count.equals("")) return;
        Log.d("TienDH", "cc+ cn: " + count);
        String s[] = count.split("-");
        if (s.length >= 3) {
            infoList.add(new InfoItem(getString(R.string.height), "Chiều cao: " + s[0] + " cm"));
            infoList.add(new InfoItem(getString(R.string.weight), "Cân nặng: " + s[1] + " kg"));
            infoList.add(new InfoItem(getString(R.string.bmi), "BMI: " + s[2]));
        }
        infoAdapter = new InfoAdapter(rootView.getContext(), R.layout.item_info, infoList);
        infoListView.setAdapter(infoAdapter);
    }

    private void showPermissionAlarmDialog() {
        Toast.makeText(getContext(), "Bạn chưa cấp đủ quyền truy cập SHealth", Toast.LENGTH_SHORT).show();
    }

    private void showConnectionFailureDialog(HealthConnectionErrorResult error) {

        mConnError = error;
        String message = "Không có kết nối với SHealth ";

        if (mConnError.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    message = "Vui lòng cài đặt S Health";
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    message = "Vui lòng cập nhật S Health";
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    message = "Vui lòng kích hoạt S Health";
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    message = "Vui lòng đồng ý với chính sách S Health";
                    break;
                default:
                    message = "Vui lòng tạo kết nói S Health";
                    break;
            }
        }
        txtAlertSHealth.setText(message);
        txtAlertSHealth.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == (MENU_ITEM_PERMISSION_SETTING)) {
            HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
            try {
                // Show user permission UI for allowing user to change options
                pmsManager.requestPermissions(mKeySet).setResultListener(mPermissionListener);
            } catch (Exception e) {
                Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
                Log.e(APP_TAG, "Permission setting fails.");
            }
        }

        return true;
    }

    private void setInfoUserOffline(String name, String email) {
        Log.d("TienDH", "set info off");
        txtEmail.setText(email);
        txtUserName.setText(name);
        String path = Environment.getExternalStorageDirectory() + "/SAM/pictures/avatar.png";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            Log.d("TienDH", "bitmap not null");
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            imageProfile.setImageBitmap(bitmap);
        }

    }

    private void setInfoUser(String name, String email, String avatar) {
        txtEmail.setText(email);
        txtUserName.setText(name);

        if (ConnectionDetector.isNetworkConnected(rootView.getContext())) {
            if (avatar != null) {
                new GetImageFromUrl().execute(avatar);
            }
        }
    }

    private void loading() {
        final ConnectServer con = new ConnectServer(getContext());
        con.getDataAccount(accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject respond) {
                Log.d("TienDH", respond.toString());
                try {
                    if (respond.getString("status").equals("fail")) {
                        Toast.makeText(rootView.getContext(), "Xảy ra lỗi, đăng nhập lại", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(rootView.getContext(), SqlashScreen.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        HistoryItem historyItem = con.responseToObject(respond);
                        user = new UserItem(historyItem.getUserItem().getId(), historyItem.getUserItem().getEmail(),
                                historyItem.getUserItem().getFullname(), historyItem.getUserItem().getAvatarUrl(),
                                historyItem.getUserItem().getList());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Var.FULLNAME, user.getFullname());
                        editor.putString(Var.EMAIL, user.getEmail());
                        editor.commit();
                        Data.user = user;
                        setInfoUser(user.getFullname(), user.getEmail(), user.getAvatarUrl());
                        sickList = getSickHistoryItem(historyItem);
                        //view listview
                        Data.sickHistoryList = sickList;
                        sickHistoryListAdapter = new SickHistoryListAdapter(getContext(), R.layout.item_sick_history, sickList);
                        sickHistoryListView.setAdapter(sickHistoryListAdapter);
                        if (sickHistoryListAdapter.getCount() > 0) {
                            ViewGroup.LayoutParams params = sickHistoryListView.getLayoutParams();
                            params.height = sickHistoryListAdapter.getCount() * convertDpToPixels(40, getContext()) + 100;
                            sickHistoryListView.setLayoutParams(params);
                            sickHistoryListView.requestLayout();
                            sickHistoryListView.setVisibility(View.VISIBLE);
                            txtAlert.setVisibility(View.INVISIBLE);
                        } else {
                            ViewGroup.LayoutParams params = sickHistoryListView.getLayoutParams();
                            params.height = 150;
                            sickHistoryListView.setLayoutParams(params);
                            sickHistoryListView.requestLayout();
                            txtAlert.setText(getString(R.string.nosick));
                            txtAlert.setVisibility(View.VISIBLE);
                            sickHistoryListView.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList<SickHistoryItem> getSickHistoryItem(HistoryItem items) {
        ArrayList<SickHistoryItem> sickHistoryItems = new ArrayList<SickHistoryItem>();
        for (int i = 0; i < items.getSicks().size(); i++) {
            sickHistoryItems.add(new SickHistoryItem(items.getSicks().get(i), items.getDatetimes().get(i)));
        }
        return sickHistoryItems;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
            createDialog();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Var.INTERNET_CHECK));
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            user = new UserItem(null, email, personName);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(rootView.getContext(), "Đăng nhập xảy ra lỗi.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private ProgressDialog progressDialog;

    private void onSignOutClicked() {
        //logout
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đăng xuất...");
        progressDialog.show();
        if (ConnectionDetector.isNetworkConnected(getContext())) {
            logout(accessToken, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject respond) {
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                    }
                    //xoa chat
                    DatabaseChat db = new DatabaseChat(getContext());
                    db.dropTable();
                    db.closeBD();
                    DatabaseNotif dbNotif = new DatabaseNotif(getContext());
                    dbNotif.dropTable();
                    dbNotif.close();
                    //xoa thong tin
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Var.ACCESS_TOKEN);
                    editor.remove(Var.FULLNAME);
                    editor.remove(Var.EMAIL);
                    editor.remove(Var.AVATAR);
                    editor.commit();
                    String path = Environment.getExternalStorageDirectory() + "/SAM/pictures/avatar.png";
                    File file = new File(path);
                    boolean deleted = file.delete();
                    Data.user = null;
                    Data.notifList = null;
                    Data.sickList = null;
                    Data.sicks = new ArrayList[6];
                    Data.sickHistoryList = null;
                    progressDialog.cancel();
                    Intent intent = new Intent(rootView.getContext(), SqlashScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        } else {
            progressDialog.cancel();
            Toast.makeText(getContext(), "Vui lòng kết nối internet để thực hiện", Toast.LENGTH_SHORT).show();
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
                        progressDialog.cancel();
                        Toast.makeText(getContext(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(rootView.getContext()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (view.getId() == R.id.sickHistoryList) {
        sendSickDetail(sickHistoryListAdapter.getItem(position).getSickItem());
//        }
    }

    private void sendSickDetail(SickItem sickItem) {
        Intent intent = new Intent(getContext(), SickDetailActivity.class);
        intent.putExtra(Var.SICK_KEY, sickItem);
        startActivity(intent);
    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageProfile.setImageBitmap(result);
            storeImage(result, "avatar.png");
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

    }

    private boolean storeImage(Bitmap imageData, String filename) {
        //get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory() + "/SAM/pictures/";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + "/" + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TienDH", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TienDH", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("\n Đăng xuất ngay bây giờ? \n");
        builder.setCancelable(false);

        builder.setPositiveButton("HỦY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton("ĐĂNG XUẤT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onSignOutClicked();
            }
        });
        builder.create().show();
    }

    public void start() {
        txtAlertSHealth.setVisibility(View.INVISIBLE);
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.HeartRate.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.Weight.HEALTH_DATA_TYPE, mObserver);
        readWeightAndHeight();
        readHeartRate();
        readDailyCalo();

    }

    private void readHeartRate() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE)
                .setProperties(new String[]{HealthConstants.HeartRate.HEART_RATE})
                .setFilter(null)
                .build();

        try {
            resolver.read(request).setResultListener(mListenerHeartRate);
        } catch (Exception e) {
            Log.e("LinhTh", e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private void readWeightAndHeight() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.Weight.HEALTH_DATA_TYPE)
                .setProperties(new String[]{HealthConstants.Weight.HEIGHT, HealthConstants.Weight.WEIGHT})
                .setFilter(null)
                .build();

        try {
            resolver.read(request).setResultListener(mListenerWeightHeight);
        } catch (Exception e) {
            Log.e("LinhTh", e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mListenerWeightHeight =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {
                @Override
                public void onResult(HealthDataResolver.ReadResult result) {
                    Cursor c = null;
                    int height = 0;
                    int weight = 0;
                    double bmi = 0;
                    try {
                        c = result.getResultCursor();
                        if (c != null) {
                            while (c.moveToNext()) {
                                height = c.getInt(c.getColumnIndex(HealthConstants.Weight.HEIGHT));
                                weight = c.getInt(c.getColumnIndex(HealthConstants.Weight.WEIGHT));
                                double tmp = (double) height / 100;
                                bmi = (double) weight / (tmp * tmp);
                                DecimalFormat df = new DecimalFormat("0.00");
                                String str = df.format(bmi);
                                bmi = Double.valueOf(str);
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                    drawHeightWeight(String.valueOf(height) + "-" +
                            String.valueOf(weight) + "-" + String.valueOf(bmi));
                }
            };


    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mListenerHeartRate =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {
                @Override
                public void onResult(HealthDataResolver.ReadResult result) {
                    int count = 0;
                    Cursor c = null;
                    try {
                        c = result.getResultCursor();
                        if (c != null) {
                            while (c.moveToNext()) {
                                count = c.getInt(c.getColumnIndex(HealthConstants.HeartRate.HEART_RATE));
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                    drawHeartRate(String.valueOf(count));
                }
            };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d("LinhTh", "Observer receives a data changed event");
            readDailyCalo();
            readWeightAndHeight();
            readHeartRate();
        }
    };

    private void readDailyCalo() {
        Log.d("TienDH", "start count ");
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        HealthDataResolver.AggregateRequest request = new HealthDataResolver.AggregateRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .addFunction(HealthDataResolver.AggregateRequest.AggregateFunction.SUM, HealthConstants.StepCount.CALORIE, "average")
                .setTimeGroup(HealthDataResolver.AggregateRequest.TimeGroupUnit.DAILY, 1, HealthConstants.StepCount.START_TIME,
                        HealthConstants.StepCount.TIME_OFFSET, "days")
                .build();
        try {
            resolver.aggregate(request).setResultListener(mAggregateResultListener);
        } catch (Exception e) {
// Error handling
            e.printStackTrace();
            Log.e("TienDH", "count failed ");
        }
    }

    int a_count = 0;
    private final HealthResultHolder.ResultListener<HealthDataResolver.AggregateResult> mAggregateResultListener = new
            HealthResultHolder.ResultListener<HealthDataResolver.AggregateResult>() {
                @Override
                public void onResult(HealthDataResolver.AggregateResult result) {
// Check and get result
                    if (HealthResultHolder.BaseResult.STATUS_SUCCESSFUL == result.getStatus()) {
                        Cursor c = null;
                        try {
                            c = result.getResultCursor();
                            if (c != null) {
                                while (c.moveToNext()) {
                                    String average = c.getString(c.getColumnIndex("average"));
                                    listStepCount.add(average);
                                }
                            }
                        } finally {
                            if (c != null) {
                                c.close();
                            }
                        }
                        drawCalo(listStepCount.get(listStepCount.size() - 2));
                        listStepCount = new ArrayList<>();
                    }
                }
            };
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectionDetector.isNetworkConnected(getContext())) {
                loading();
            } else {
                Log.d("TienDH", "no internt - set info off");
                String email = sharedPreferences.getString(Var.EMAIL, "");
                String name = sharedPreferences.getString(Var.FULLNAME, "");
                setInfoUserOffline(name, email);
            }
        }

    };


}