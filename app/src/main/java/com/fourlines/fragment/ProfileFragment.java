package com.fourlines.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fourlines.data.Data;
import com.fourlines.data.DatabaseChat;
import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.doctor.SqlashScreen;
import com.fourlines.model.InfoItem;
import com.fourlines.model.SickItem;
import com.fourlines.model.UserItem;
import com.fourlines.volley.ConnectServer;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "TienDH";
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
    private LinearLayout content;
    private ImageView imageProfile;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 9001;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnLoginGoogle;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

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
        content = (LinearLayout) rootView.findViewById(R.id.content);
        imageProfile = (ImageView) rootView.findViewById(R.id.imageUser);

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(rootView.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            Log.d("TienDH", "ProfileFrag: isConnected");
        } else {
            Log.d("TienDH", "ProfileFrag: notConnected");
        }

        content.setVisibility(View.GONE);
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
        if (Data.user != null) {
            content.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            user = Data.user;
            txtEmail.setText(user.getEmail());
            txtUserName.setText(user.getFullname());
            if (user.getAvatarUrl() != null) {
                new GetImageFromUrl().execute(user.getAvatarUrl());
            }
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
                    if (respond.getString("status").equals("fail")) {
                        Intent intent = new Intent(rootView.getContext(), SqlashScreen.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        UserItem userItem = con.responseToObject(respond);
                        user = new UserItem(userItem.getId(), userItem.getEmail(), userItem.getFullname(), userItem.getAvatarUrl(), userItem.getList());
                        Data.user = user;
                        txtEmail.setText(user.getEmail());
                        txtUserName.setText(user.getFullname());
                        if (user.getAvatarUrl() != null) {
                            new GetImageFromUrl().execute(user.getAvatarUrl());
                        }
                    }
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
            onSignOutClicked();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhoto = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();
            String personBirthday = currentPerson.getBirthday();
            int personGender = currentPerson.getGender();
            Log.d("TienDH", "Info: " + personName + "  " + personGooglePlusProfile);
        }
        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
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

    private void onSignOutClicked() {
        //logout
        logout(accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject respond) {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                DatabaseChat db = new DatabaseChat(getContext());
                db.dropTable();
                db.closeBD();

                sharedPreferences.edit().remove(Var.ACCESS_TOKEN).commit();
                Data.user = null;
                Data.notifList = null;
                Data.sickList = null;
                Data.sicks = null;
                Intent intent = new Intent(rootView.getContext(), SqlashScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
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
        MySingleton.getInstance(rootView.getContext()).addToRequestQueue(jsObjRequest);
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
}