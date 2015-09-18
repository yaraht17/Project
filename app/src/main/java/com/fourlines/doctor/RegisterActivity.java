package com.fourlines.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.data.Var;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etxtEmail, etxtPassword, etxtFullname;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etxtEmail = (EditText) findViewById(R.id.txtEmail);
        etxtPassword = (EditText) findViewById(R.id.txtPassword);
        etxtFullname = (EditText) findViewById(R.id.txtFullName);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);

    }

    public void register(final String email, final String password,
                         final String fullname, final VolleyCallback callback) throws JSONException {
        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", email);
        jsonBody.put("password", password);
        jsonBody.put("fullname", fullname);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Var.URL_REGISTER, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            String email = etxtEmail.getText().toString();
            String password = etxtPassword.getText().toString();
            String fullname = etxtFullname.getText().toString();
            try {
                register(email, password, fullname, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject respond) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
