package com.example.batterymonitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundBatteryMonitor extends Service {
   BroadcastReceiver br;
   boolean isServiceRunning = false;
   int thresh_lvl;
   public static final String CHANNEL_ID = "ForegroundServiceChannel";
   final String TAG = "batteryMonitorWorker";

   @Override
   public void onCreate() {
      super.onCreate();
      isServiceRunning = true;
   }
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      String input = intent.getStringExtra("inputExtra");
      this.thresh_lvl = intent.getIntExtra("progressLvl", 0);
      createNotificationChannel();
      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this,
              0, notificationIntent, 0);
      Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
              .setContentTitle("Battery monitor is active")
              .setContentText(input)
              .setSmallIcon(R.mipmap.batterybogo_foreground)
              .setContentIntent(pendingIntent)
              .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
              .build();
      startForeground(1, notification);

      new Thread(new Runnable() {
         @Override
         public void run() {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            BatteryManager bm = (BatteryManager) getApplicationContext()
                    .getSystemService(BATTERY_SERVICE);
            while(isServiceRunning) {
               int lvl = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
               if(lvl>=thresh_lvl && !r.isPlaying()){
                  r.play();
               }

               try {
                  Thread.sleep(30000);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            if(r.isPlaying()){
               r.stop();
            }
         }
      }).start();
      br = new TestBroadcast();
      IntentFilter If = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
      registerReceiver(br, If);
      //do heavy work on a background thread
      //stopSelf();
      return START_NOT_STICKY;
   }
   @Override
   public void onDestroy() {
      super.onDestroy();
      isServiceRunning = false;
      unregisterReceiver(br);
   }
   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }
   private void createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         NotificationChannel serviceChannel = new NotificationChannel(
                 CHANNEL_ID,
                 "Foreground Service Channel",
                 NotificationManager.IMPORTANCE_DEFAULT
         );
         NotificationManager manager = getSystemService(NotificationManager.class);
         manager.createNotificationChannel(serviceChannel);
      }
   }
}