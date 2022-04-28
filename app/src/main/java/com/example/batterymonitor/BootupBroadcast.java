package com.example.batterymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class BootupBroadcast extends BroadcastReceiver {
   @Override
   public void onReceive(Context context, Intent intent) {
      Intent i = new Intent(context, ForegroundBatteryMonitor.class);
      i.putExtra("inputExtra", "Threshold level set for "+80+"%");
      i.putExtra("progressLvl",80);
      ContextCompat.startForegroundService(context, i);
   }
}
