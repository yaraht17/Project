package com.fourlines.doctor;

import android.accounts.Account;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.connection.ConnectionDetector;
import com.fourlines.data.Var;
import com.fourlines.volley.ConnectServer;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

//35:5C:B9:0E:DC:32:5A:30:A0:DE:3D:B7:7D:86:EE:3D:16:31:03:79
public class SqlashScreen extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private static final String SERVER_CLIENT_ID = "191481179195-a0i8umlb5o1bl1dc8r878mh91hdnul4f.apps.googleusercontent.com";
    private SharedPreferences sharedPreferences;
    private static final String TAG = "TienDH";
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnLoginGoogle;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
    private String accessToken;
    private ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlash_screen);
        connectionDetector = new ConnectionDetector(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
        btnLoginGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        btnLoginGoogle.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        checkLogin();


//        Thread background = new Thread() {
//            public void run() {
//                try {
//                    // Thread will sleep for 2 seconds
//                    sleep(2 * 1000);
//                    checkLogin();
//                    // Remove activity
//                    finish();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        background.start();

        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    public void checkLogin() {


        if (!accessToken.equals("")) {
            //co token
            if (connectionDetector.isNetworkConnected()) {


                final ConnectServer con = new ConnectServer(this);
                con.getDataAccount(accessToken, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject respond) {
                        try {
                            if (respond.getString("status").equals("fail")) {
                                //sai token
                                sharedPreferences.edit().remove(Var.ACCESS_TOKEN).commit();
                                showLogin();
                            } else {
                                //dung token
                                showSignedInUI();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                showSignedInUI();
            }
        } else {
            //khong token
            showLogin();
        }
    }

    private void showLogin() {

        btnLoginGoogle.setVisibility(View.VISIBLE);
    }

    private void showSignedInUI() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhoto = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();
            String personBirthday = currentPerson.getBirthday();
            int personGender = currentPerson.getGender();
            Log.d(TAG, "Info: " + personName + "  " + personGooglePlusProfile + " " + personBirthday);
            if (accessToken.equals("")) {
                new GetIdTokenTask().execute();
                showSignedInUI();
            }
        }
        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        Log.d(TAG, "Info: " + email);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Lỗi", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        if (connectionDetector.isNetworkConnected()) {
            mShouldResolve = true;
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(getApplicationContext(), "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                showErrorDialog(connectionResult);
            }
        } else {
            if (!mGoogleApiClient.isConnected()) {
                btnLoginGoogle.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }
            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                        }
                    }).show();
        } else {
            Toast.makeText(this, "Google Play Service xảy ra lỗi", Toast.LENGTH_SHORT).show();
            mShouldResolve = false;
        }
    }

    private class GetIdTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "get token");
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            String scopes = "audience:server:client_id:" + SERVER_CLIENT_ID; // Not the app's client ID.
            try {
                return GoogleAuthUtil.getToken(getApplicationContext(), account, scopes);
            } catch (IOException e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            } catch (GoogleAuthException e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //send to server
                Log.i(TAG, "ID token: " + result);
                loginAction(result);
            } else {
                onSignOut();
                Toast.makeText(getApplicationContext(), "Xảy ra lỗi, vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
                Log.i(TAG, "ID token: null");
            }
        }
    }

    private void loginAction(String idToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            login(idToken, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject respond) {
                    Log.d("TienDH", respond.toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    try {
                        editor.putString(Var.ACCESS_TOKEN, respond.getString(Var.ACCESS_TOKEN));
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void login(final String idToken, final VolleyCallback callback) throws JSONException {

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put(Var.TOKEN, idToken);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Var.URL_LOGIN, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LinhTh", error.toString());
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void onSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }
}
