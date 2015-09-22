package com.fourlines.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.data.Var;
import com.fourlines.model.AccountItem;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private EditText etxtEmail;
    private EditText etxtPassword;
    private EditText etxtFullname;
    private TextView txtForgetPass;
    private TextView txtRegister;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        etxtEmail = (EditText) findViewById(R.id.txtEmail);
        etxtPassword = (EditText) findViewById(R.id.txtPassword);
        etxtFullname = (EditText) findViewById(R.id.txtFullName);
        txtForgetPass = (TextView) findViewById(R.id.txtFogetPass);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);

        txtRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtForgetPass.setOnClickListener(this);

    }

    private void actionBtnLogin() {
        AccountItem account = new AccountItem(etxtEmail.getText().toString(), etxtPassword.getText().toString());
        if (account.getUser().equals("")) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
        } else if (account.getPassword().equals("")) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập Password", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                login(account, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject respond) {
                        try {
                            if (respond.getString("status").equals("fail")) {
                                Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                try {
                                    editor.putString(Var.ACCESS_TOKEN, respond.getString(Var.ACCESS_TOKEN));
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(final AccountItem account, final VolleyCallback callback) throws JSONException {

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put(Var.EMAIL, account.getUser());
        jsonBody.put(Var.PASSWORD, account.getPassword());
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnLogin:
                actionBtnLogin();
                break;
            case R.id.txtFogetPass:
                break;
            case R.id.txtRegister:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
