package com.example.batterymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryChangedBroadcast extends BroadcastReceiver {
   private static final String TAG = "batteryMo";
   int thresh_lvl;
   BatteryMonitorCallback batteryService;


   BatteryChangedBroadcast(int thresh_lvl, ForegroundBatteryMonitor context){
      this.thresh_lvl = thresh_lvl;
      batteryService = (BatteryMonitorCallback)context;
   }

   @Override
   public void onReceive(Context context, Intent intent) {
      int batLvl = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
      int batScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
      int isCharging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
      batLvl = (int)(((float)batLvl/(float)batScale)*100);

      if((isCharging>0)&&(batLvl>=thresh_lvl)
              &&(batLvl<thresh_lvl+2)
              &&(!batteryService.isPlaying())){
         batteryService.playNotification();
      }
      else if((isCharging<=0)&&(batteryService.isPlaying())){
         batteryService.stopNotification();
      }
   }
}
