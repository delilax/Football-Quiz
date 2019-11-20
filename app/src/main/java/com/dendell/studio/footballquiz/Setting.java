package com.dendell.studio.footballquiz;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.firebase.crash.FirebaseCrash;

public class Setting extends AppCompatActivity {

    private Button resetBtn;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;
    private  Boolean soundVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.setting);

        resetBtn = (Button) findViewById(R.id.resett);
        resetBtn.setOnClickListener(resetvariables(resetBtn));

        sharedPref = getSharedPreferences("score", MODE_PRIVATE);
        mEditor = sharedPref.edit();


        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButtonSound);

        try{
            soundVar=sharedPref.getBoolean("soundToggle",true);
        } catch (NullPointerException e) {
            mEditor.putBoolean("soundToggle", true);
            mEditor.commit();
        }

        toggle.setChecked(soundVar);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleSound(true);

                } else {
                    toggleSound(false);
                }
            }
        });
    }

    private void toggleSound(Boolean bl){
        try{
            mEditor.putBoolean("soundToggle", bl);
            mEditor.commit();
        } catch (NullPointerException e) {
            mEditor.putBoolean("soundToggle", true);
            mEditor.commit();
        }
    }



    View.OnClickListener resetvariables(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("score", MODE_PRIVATE);
                for (int i = 0; i < 500; i++) {
                    preferences.edit().remove("score_logos_" + i).commit();
                    preferences.edit().remove("score_players_" + i).commit();
                }

                for (int i = 1; i < 26; i++) {
                    preferences.edit().remove("score_Q" + i).commit();
                }

                try {
                    mEditor.putInt("hints_questions", Global.hintsStart);
                    mEditor.commit();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }

                try {
                    mEditor.putInt("hints_logo", Global.hintsStart);
                    mEditor.commit();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }

                try {
                    mEditor.putInt("hints_player", Global.hintsStart);
                    mEditor.commit();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }

                mEditor.putLong("timer_video_1_left", 0);
                mEditor.commit();

                mEditor.putLong("timer_video_2_left", 0);
                mEditor.commit();

                mEditor.putLong("timer_video_3_left",0);
                mEditor.commit();

                mEditor.putLong("ptimer_video_1_left", 0);
                mEditor.commit();

                mEditor.putLong("ptimer_video_2_left", 0);
                mEditor.commit();

                mEditor.putLong("ptimer_video_3_left", 0);
                mEditor.commit();

                mEditor.putLong("qtimer_video_1_left", 0);
                mEditor.commit();

                mEditor.putLong("qtimer_video_2_left",0);
                mEditor.commit();

                mEditor.putLong("qtimer_video_3_left", 0);
                mEditor.commit();

                Toast.makeText(getApplicationContext(),"The game is reseted.",Toast.LENGTH_SHORT).show();

            }
        };
    }
}
