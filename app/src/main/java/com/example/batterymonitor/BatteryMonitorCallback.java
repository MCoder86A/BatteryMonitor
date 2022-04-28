package com.example.batterymonitor;

interface BatteryMonitorCallback {
  void playNotification();
  boolean isPlaying();
  void stopNotification();
}
