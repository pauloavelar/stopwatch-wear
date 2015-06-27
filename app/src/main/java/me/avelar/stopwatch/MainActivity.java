package me.avelar.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView mTimer;

    private boolean isRunning = false;
    private long startTime = 0L, timeSwap = 0L, updatedTime = 0L;
    private long timeInMilliseconds = 0L;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = (TextView) findViewById(R.id.timer);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_start:
                if (isRunning) {
                    timeSwap += timeInMilliseconds;
                    handler.removeCallbacks(timerUpdater);
                    ((Button) v).setText("Start");
                    isRunning = false;
                } else {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(timerUpdater, 0);
                    ((Button) v).setText("Pause");
                    isRunning = true;
                }
                break;
            case R.id.btn_reset:
                if (!isRunning) {
                    mTimer.setText("00:00:00");
                    updatedTime = timeSwap = 0L;
                }
                break;
        }
    }

    private Runnable timerUpdater = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwap + timeInMilliseconds;

            int seconds = (int) (updatedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updatedTime % 1000);
            milliseconds = milliseconds / 10;

            mTimer.setText(
                String.format("%02d", minutes) + ":" +
                String.format("%02d", seconds) + ":" +
                String.format("%02d", milliseconds)
            );
            handler.postDelayed(this, 0);
        }
    };

}
