package com.bitsolution.pln.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TrigerStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LocationService.class));
    }
}
