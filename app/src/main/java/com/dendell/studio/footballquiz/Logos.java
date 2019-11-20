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

public class Logos extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;
    private Integer[] scores= new Integer[500];

    private ProgressBar[]  logos_progress=new ProgressBar[25];
    private TextView[] logos_number= new TextView[25];
    private Integer[] levelprogresslogos=new Integer[25];

    private LinearLayout[] layouts= new LinearLayout[25];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.logos);


        sharedPref = getSharedPreferences("score",MODE_PRIVATE);
        mEditor = sharedPref.edit();

        try {
            for(int i=0;i<500; i++)
                scores[i]=sharedPref.getInt("score_logos_" + i, 0);
        } catch (NullPointerException e) {
            for(int i=0;i<500; i++)
                mEditor.putInt("score_logos_" + i, 0);
            mEditor.commit();
        }


        for (int i=0;i< logos_progress.length;i++){
            String progbarString="progresbarlogoslvl"+(i+1);
            int prbarid = getResources().getIdentifier(progbarString, "id", getPackageName());
            logos_progress[i]=(ProgressBar) findViewById(prbarid);
        }

        for (int i=0;i< logos_number.length;i++){
            String textscore="scorelogoslvl"+(i+1);
            int texscoreid = getResources().getIdentifier(textscore, "id", getPackageName());
            logos_number[i]=(TextView) findViewById(texscoreid);
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

        levelprogresslogos[0] = Collections.frequency(score1, 1);
        levelprogresslogos[1] = Collections.frequency(score2, 1);
        levelprogresslogos[2] = Collections.frequency(score3, 1);
        levelprogresslogos[3] = Collections.frequency(score4, 1);
        levelprogresslogos[4] = Collections.frequency(score5, 1);
        levelprogresslogos[5] = Collections.frequency(score6, 1);
        levelprogresslogos[6] = Collections.frequency(score7, 1);
        levelprogresslogos[7] = Collections.frequency(score8, 1);
        levelprogresslogos[8] = Collections.frequency(score9, 1);
        levelprogresslogos[9] = Collections.frequency(score10, 1);
        levelprogresslogos[10] = Collections.frequency(score11, 1);
        levelprogresslogos[11] = Collections.frequency(score12, 1);
        levelprogresslogos[12] = Collections.frequency(score13, 1);
        levelprogresslogos[13] = Collections.frequency(score14, 1);
        levelprogresslogos[14] = Collections.frequency(score15, 1);
        levelprogresslogos[15] = Collections.frequency(score16, 1);
        levelprogresslogos[16] = Collections.frequency(score17, 1);
        levelprogresslogos[17] = Collections.frequency(score18, 1);
        levelprogresslogos[18] = Collections.frequency(score19, 1);
        levelprogresslogos[19] = Collections.frequency(score20, 1);
        levelprogresslogos[20] = Collections.frequency(score21, 1);
        levelprogresslogos[21] = Collections.frequency(score22, 1);
        levelprogresslogos[22] = Collections.frequency(score23, 1);
        levelprogresslogos[23] = Collections.frequency(score24, 1);
        levelprogresslogos[24] = Collections.frequency(score25, 1);


        for (int i=0;i< logos_progress.length;i++) {
            logos_progress[i].setProgress(levelprogresslogos[i]);
            logos_number[i].setText(String.valueOf(levelprogresslogos[i]));
        }

        for (int i=0;i< layouts.length;i++){
            String textscore="logosLayout"+(i+1);
            int texscoreid1 = getResources().getIdentifier(textscore, "id", getPackageName());
            layouts[i]=(LinearLayout) findViewById(texscoreid1);
        }

        for (int i=0;i<levelprogresslogos.length;i++) {
            if (i + 1 < levelprogresslogos.length) {
                if (i <= 14 && levelprogresslogos[i] < 15) {
                    layouts[i + 1].setEnabled(false);
                    layouts[i + 1].setBackgroundResource(R.drawable.border_disable);

                } else if ((i > 14 && i + 1 < levelprogresslogos.length && levelprogresslogos[i] < 10)) {
                    layouts[i + 1].setEnabled(false);
                    layouts[i + 1].setBackgroundResource(R.drawable.border_disable);

                } else if (i + 1 < levelprogresslogos.length) {
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


    public void logosopen(View view) {
        Intent intent1 = new Intent(this, LogosPitanja.class);
        intent1.putExtra("logosLevel", view.getId());
        startActivity(intent1);
        finish();
    }
}