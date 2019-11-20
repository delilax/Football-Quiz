package com.dendell.studio.footballquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private Button btn;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;

    private int countStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.exitbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitpop();
            }
        });

        sharedPref = getSharedPreferences("score", MODE_PRIVATE);
        mEditor = sharedPref.edit();

        try{
            countStart=sharedPref.getInt("count_starting_app",0);

            if (countStart<3) {
                countStart = countStart + 1;
            }
            mEditor.putInt("count_starting_app",countStart);
            mEditor.commit();

        } catch (NullPointerException e) {
            mEditor.putInt("count_starting_app",0);
            mEditor.commit();
            FirebaseCrash.logcat(Log.INFO, "MainActivity", "count_starting_app");

        }
    }


    @Override
    public void onBackPressed() {
            exitpop();
    }


    private void exitpop() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.exit_icon));

        builder.setTitle("Exit Football Quiz");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                long stoptime=System.currentTimeMillis();
                mEditor.putLong("time_stop_1", stoptime);
                mEditor.commit();

                mEditor.putLong("ptime_stop_1", stoptime);
                mEditor.commit();

                mEditor.putLong("qptime_stop_1", stoptime);
                mEditor.commit();

                mEditor.putLong("timer_video_1_left", Global.timerVideo1);
                mEditor.commit();

                mEditor.putLong("timer_video_2_left", Global.timerVideo2);
                mEditor.commit();

                mEditor.putLong("timer_video_3_left", Global.timerVideo3);
                mEditor.commit();

                mEditor.putLong("ptimer_video_1_left", Global.ptimerVideo1);
                mEditor.commit();

                mEditor.putLong("ptimer_video_2_left", Global.ptimerVideo2);
                mEditor.commit();

                mEditor.putLong("ptimer_video_3_left", Global.ptimerVideo3);
                mEditor.commit();

                mEditor.putLong("qtimer_video_1_left", Global.qtimerVideo1);
                mEditor.commit();

                mEditor.putLong("qtimer_video_2_left", Global.qtimerVideo2);
                mEditor.commit();

                mEditor.putLong("qtimer_video_3_left", Global.qtimerVideo3);
                mEditor.commit();

                finish();
                System.exit(0);
            }

        });

        if (countStart==3) {
            builder.setNeutralButton("Rate Our App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.dendell.studio.myapplication")));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.dendell.studio.myapplication")));
                    }
                }

            });
        }

        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }


    public void onclickplayers(View view) {
        Intent intent1 = new Intent(this, Players.class);
        startActivity(intent1);
    }

    public void onclicksettings(View view) {
        Intent intent1 = new Intent(this, Setting.class);
        startActivity(intent1);
    }

    public void onclickstatistics(View view) {
        Intent intent1 = new Intent(this, Stats.class);
        startActivity(intent1);
    }

    public void onclicklogos (View view){
        Intent intent1 = new Intent (this, Logos.class);
        startActivity(intent1);
    }


    public void onclickquestions (View view){
        Intent intent1 = new Intent (this, Questions.class);
        startActivity(intent1);
    }
}