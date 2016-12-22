package com.example.tim.commonintents;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_alarm:
                createAlarm("下班买菜", 10, 40);
                break;
            case R.id.button_create_timer:
                startTimer("下班倒计时", 60);
                break;
            case R.id.button_show_alarms:
                showAllAlarms();
                break;
            case R.id.button_add_calendar_event:
                long beginTime = System.currentTimeMillis() + 10*60*1000;// 10 minutes later
                long endTime = System.currentTimeMillis() + 24*60*60*1000; // a day later
                addCalendarEvent("TestCalendarEventAdd", null, beginTime, endTime);
                break;
            case R.id.button_start_camera_in_still_image_mode:
                startCameraInStillImageMode();
                break;
            case R.id.button_start_camera_in_video_mode:
                startCameraInVideoMode();
                break;
        }
    }

    private void startCameraInVideoMode() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    private void startCameraInStillImageMode() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void addCalendarEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showAllAlarms() {
        Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);  // Vendor like Xiaomi support alarm create ui skip too.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "createAlarmIntent resolved.");
            }
        }
    }

    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        // The ui skip support implementation varies from vendor to vendor, may be no visual tip for timer while it succeed like Xiaomi's device does.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "startTimerIntent resolved.");
            }
        }
    }
}
