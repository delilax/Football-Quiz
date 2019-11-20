package com.dendell.studio.footballquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Players extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;
    private Integer[] scores= new Integer[500];

    private ProgressBar[]  players_progress=new ProgressBar[25];
    private TextView[] players_number= new TextView[25];
    private Integer[] levelprogressplayers=new Integer[25];
    private LinearLayout[] layouts= new LinearLayout[25];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.players);

        sharedPref = getSharedPreferences("score",MODE_PRIVATE);
        mEditor = sharedPref.edit();

        try {
            for(int i=0;i<500; i++)
                scores[i]=sharedPref.getInt("score_players_" + i, 0);
        } catch (NullPointerException e) {
            for(int i=0;i<500; i++)
                mEditor.putInt("score_players_" + i, 0);
            mEditor.commit();
        }

        for (int i=0;i< players_progress.length;i++){
            String progbarString="progresbarplayerslvl"+(i+1);
            int prbarid = getResources().getIdentifier(progbarString, "id", getPackageName());
            players_progress[i]=(ProgressBar) findViewById(prbarid);
        }

        for (int i=0;i< players_number.length;i++){
            String textscore="scoreplayerslvl"+(i+1);
            int texscoreid = getResources().getIdentifier(textscore, "id", getPackageName());
            players_number[i]=(TextView) findViewById(texscoreid);
        }

        List<Integer> score1=new ArrayList<Integer>();
        List<Integer> score2=new ArrayList<Integer>();
        List<Integer> score3=new ArrayList<Integer>();
        List<Integer> score4=new ArrayList<Integer>();
        List<Integer> score5=new ArrayList<Integer>();

        List<Integer> score6=new ArrayList<Integer>();
        List<Integer> score7=new ArrayList<Integer>();
        List<Integer> score8=new ArrayList<Integer>();
        List<Integer> score9=new ArrayList<Integer>();
        List<Integer> score10=new ArrayList<Integer>();

        List<Integer> score11=new ArrayList<Integer>();
        List<Integer> score12=new ArrayList<Integer>();
        List<Integer> score13=new ArrayList<Integer>();
        List<Integer> score14=new ArrayList<Integer>();
        List<Integer> score15=new ArrayList<Integer>();

        List<Integer> score16=new ArrayList<Integer>();
        List<Integer> score17=new ArrayList<Integer>();
        List<Integer> score18=new ArrayList<Integer>();
        List<Integer> score19=new ArrayList<Integer>();
        List<Integer> score20=new ArrayList<Integer>();

        List<Integer> score21=new ArrayList<Integer>();
        List<Integer> score22=new ArrayList<Integer>();
        List<Integer> score23=new ArrayList<Integer>();
        List<Integer> score24=new ArrayList<Integer>();
        List<Integer> score25=new ArrayList<Integer>();


        for (int i=0;i<20;i++){

            score1.add(scores[i]);
            score2.add(scores[i+20]);
            score3.add(scores[i+20*2]);
            score4.add(scores[i+20*3]);
            score5.add(scores[i+20*4]);
            score6.add(scores[i+20*5]);
            score7.add(scores[i+20*6]);
            score8.add(scores[i+20*7]);
            score9.add(scores[i+20*8]);
            score10.add(scores[i+20*9]);
            score11.add(scores[i+20*10]);
            score12.add(scores[i+20*11]);
            score13.add(scores[i+20*12]);
            score14.add(scores[i+20*13]);
            score15.add(scores[i+20*14]);
            score16.add(scores[i+20*15]);
            score17.add(scores[i+20*16]);
            score18.add(scores[i+20*17]);
            score19.add(scores[i+20*18]);
            score20.add(scores[i+20*19]);
            score21.add(scores[i+20*20]);
            score22.add(scores[i+20*21]);
            score23.add(scores[i+20*22]);
            score24.add(scores[i+20*23]);
            score25.add(scores[i+20*24]);
        }


        levelprogressplayers[0] = Collections.frequency(score1, 1);
        levelprogressplayers[1] = Collections.frequency(score2, 1);
        levelprogressplayers[2] = Collections.frequency(score3, 1);
        levelprogressplayers[3] = Collections.frequency(score4, 1);
        levelprogressplayers[4] = Collections.frequency(score5, 1);

        levelprogressplayers[5] = Collections.frequency(score6, 1);
        levelprogressplayers[6] = Collections.frequency(score7, 1);
        levelprogressplayers[7] = Collections.frequency(score8, 1);
        levelprogressplayers[8] = Collections.frequency(score9, 1);
        levelprogressplayers[9] = Collections.frequency(score10, 1);

        levelprogressplayers[10] = Collections.frequency(score11, 1);
        levelprogressplayers[11] = Collections.frequency(score12, 1);
        levelprogressplayers[12] = Collections.frequency(score13, 1);
        levelprogressplayers[13] = Collections.frequency(score14, 1);
        levelprogressplayers[14] = Collections.frequency(score15, 1);

        levelprogressplayers[15] = Collections.frequency(score16, 1);
        levelprogressplayers[16] = Collections.frequency(score17, 1);
        levelprogressplayers[17] = Collections.frequency(score18, 1);
        levelprogressplayers[18] = Collections.frequency(score19, 1);
        levelprogressplayers[19] = Collections.frequency(score20, 1);

        levelprogressplayers[20] = Collections.frequency(score21, 1);
        levelprogressplayers[21] = Collections.frequency(score22, 1);
        levelprogressplayers[22] = Collections.frequency(score23, 1);
        levelprogressplayers[23] = Collections.frequency(score24, 1);
        levelprogressplayers[24] = Collections.frequency(score25, 1);


        for (int i=0;i<  players_progress.length;i++) {
            players_progress[i].setProgress(levelprogressplayers[i]);
            players_number[i].setText(String.valueOf(levelprogressplayers[i]));
        }

        for (int i=0;i< layouts.length;i++){
            String textscore="playersLayout"+(i+1);
            int texscoreid1 = getResources().getIdentifier(textscore, "id", getPackageName());
            layouts[i]=(LinearLayout) findViewById(texscoreid1);
        }

        for (int i=0;i<levelprogressplayers.length;i++) {
            if (i+1<levelprogressplayers.length) {
                if (i <= 14 && levelprogressplayers[i] < 15) {
                    layouts[i + 1].setEnabled(false);
                    layouts[i + 1].setBackgroundResource(R.drawable.border_disable);

                } else if ((i > 14 && levelprogressplayers[i] < 10)) {
                    layouts[i + 1].setEnabled(false);
                    layouts[i + 1].setBackgroundResource(R.drawable.border_disable);

                } else if (i + 1 < levelprogressplayers.length) {
                    layouts[i + 1].setEnabled(true);
                    if (i + 1 <= 4) layouts[i + 1].setBackgroundResource(R.drawable.button_bggreen);
                    else if (i + 1 > 4 && i + 1 <= 9)
                        layouts[i + 1].setBackgroundResource(R.drawable.button_bgyellow);
                    else if (i + 1 > 9 && i + 1 <= 14)
                        layouts[i + 1].setBackgroundResource(R.drawable.button_bgorange);
                    else if (i + 1 > 14 && i + 1 <= 19)
                        layouts[i + 1].setBackgroundResource(R.drawable.button_bgdarkorange);
                    else if (i + 1 > 19 && i + 1 <= 24)
                        layouts[i + 1].setBackgroundResource(R.drawable.button_bgred);
                }
            }
        }

    }

    public void playersopen(View view) {
        Intent intent1 = new Intent(this, PlayersPitanja.class);
        intent1.putExtra("playersLevel", view.getId());
        startActivity(intent1);
        finish();
    }
}