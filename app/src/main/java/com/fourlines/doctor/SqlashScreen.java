package com.fourlines.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fourlines.data.Var;

public class SqlashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlash_screen);
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(2 * 1000);
                    checkLogin();
                    // Remove activity
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }

    public void checkLogin() {
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Var.ACCESS_TOKEN, "");

        if (token.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
