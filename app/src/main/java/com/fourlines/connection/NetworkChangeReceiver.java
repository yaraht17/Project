package com.fourlines.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fourlines.data.Var;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        context.sendBroadcast(new Intent(Var.INTERNET_CHECK));
    }
}