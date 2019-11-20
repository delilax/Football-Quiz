package com.dendell.studio.footballquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Questions  extends AppCompatActivity {

    private ProgressBar[]  questions_progress=new ProgressBar[25];
    private TextView[] questions_number= new TextView[25];
    private Integer[] levelprogress= new Integer[25];
    private LinearLayout[] layouts= new LinearLayout[25];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        for (int i=0;i< questions_progress.length;i++){
            String progbarString="progresbarquestionslvl"+(i+1);
            int prbarid = getResources().getIdentifier(progbarString, "id", getPackageName());
            questions_progress[i]=(ProgressBar) findViewById(prbarid);
        }

        for (int i=0;i< questions_number.length;i++){
            String textscore="scorequestionslvl"+(i+1);
            int texscoreid = getResources().getIdentifier(textscore, "id", getPackageName());
            questions_number[i]=(TextView) findViewById(texscoreid);
        }

        for (int i=0;i< layouts.length;i++){
            String textscore="questionsLayout"+(i+1);
            int texscoreid1 = getResources().getIdentifier(textscore, "id", getPackageName());
            layouts[i]=(LinearLayout) findViewById(texscoreid1);
        }

        SharedPreferences sharedPref = getSharedPreferences("score",MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPref.edit();



        try {
            for(int i=1; i<26; i++)
                levelprogress[i-1]=sharedPref.getInt("score_Q" + i, 0);

        } catch (NullPointerException e) {
            for(int i=1;i<26; i++)
                mEditor.putInt("score_Q" + i, 0);
            mEditor.commit();
        }


        for (int i=0;i<  questions_progress.length;i++) {
            questions_progress[i].setProgress(levelprogress[i]);
            questions_number[i].setText(String.valueOf(levelprogress[i]));
        }

        for (int i=0;i<levelprogress.length;i++) {
            if (i+1<levelprogress.length) {
                if (i <= 14 && levelprogress[i] < 15) {

                    layouts[i + 1].setEnabled(false);
                    layouts[i + 1].setBackgroundResource(R.drawable.border_disable);

                } else if ((i > 14 && levelprogress[i] < 10)) {
                    if (i + 1 < levelprogress.length) {
                        layouts[i + 1].setEnabled(false);
                        layouts[i + 1].setBackgroundResource(R.drawable.border_disable);
                    }

                } else if (i + 1 < levelprogress.length) {
                    layouts[i + 1].setEnabled(true);
                    if (i <= 4) layouts[i].setBackgroundResource(R.drawable.button_bggreen);
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

    public void pitanjeopen(View view) {
        Intent intent = new Intent(this, Suhapitanja.class);
        intent.putExtra("pitanjeLevel", view.getId());
        startActivity(intent);
        finish();
    }
}


