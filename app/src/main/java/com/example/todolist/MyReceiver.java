package com.example.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // perform the desired action every 5 minutes when the alarm goes off
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startService(serviceIntent);
    }
}