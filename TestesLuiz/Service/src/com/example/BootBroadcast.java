package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("log", "boot received");
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.example.MyService");
        context.startService(serviceIntent);

    }

}
