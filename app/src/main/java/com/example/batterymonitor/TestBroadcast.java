package com.example.batterymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBroadcast extends BroadcastReceiver {
   private static final String TAG = "batteryMo";

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(TAG, "onReceive: triggered");
   }
}
