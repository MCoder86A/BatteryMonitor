package com.example.batterymonitor;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar sc = findViewById(R.id.percent_ctrlBar);
        TextView tv = findViewById(R.id.percent_view);
        sc.setOnSeekBarChangeListener(new SeekbarChangeAction(tv));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    public void stopAlarm(View view) {
        if(isMyServiceRunning(ForegroundBatteryMonitor.class)){
            Intent intent = new Intent(this, ForegroundBatteryMonitor.class);
            stopService(intent);
            Toast.makeText(getApplicationContext(),"Alarm stop",Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Alarm already stop",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void startAlarm(View view) {
        if(!isMyServiceRunning(ForegroundBatteryMonitor.class)){
            SeekBar sc = findViewById(R.id.percent_ctrlBar);
            Intent intent = new Intent(this, ForegroundBatteryMonitor.class);
            intent.putExtra("inputExtra", "Threshold level set for "+sc.getProgress()+"%");
            intent.putExtra("progressLvl",sc.getProgress());
            ContextCompat.startForegroundService(this, intent);

            Toast.makeText(getApplicationContext(),"Alarm started", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Alarm already started",Toast.LENGTH_SHORT)
                    .show();
        }
    }
}