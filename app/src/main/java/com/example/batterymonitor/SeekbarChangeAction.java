package com.example.batterymonitor;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

class SeekbarChangeAction implements SeekBar.OnSeekBarChangeListener{
   TextView percentView;
   SeekbarChangeAction(TextView percentView){
      this.percentView = percentView;
   }
   @Override
   public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
      String percentSet = String.valueOf(seekBar.getProgress())+"%";
      this.percentView.setText(percentSet);
   }

   @Override
   public void onStartTrackingTouch(SeekBar seekBar) {

   }

   @Override
   public void onStopTrackingTouch(SeekBar seekBar) {
      seekBar.getProgress();
   }
}
