package com.dendell.studio.footballquiz;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stats extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private List<Integer> players_scores= new ArrayList<>();
    private List<Integer> logos_scores= new ArrayList<>();
    private List<Integer> questions_scores= new ArrayList<>();

    private ProgressBar  players_prbar,logos_prbar,questions_prbar,all_prbar;
    private TextView players_number,logos_number,questions_number,all_number,players_percent,logos_percent,questions_percent,all_percent;

    private Integer players,logos,questions=0;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.stats);

        sharedPref = getSharedPreferences("score", MODE_PRIVATE);

        players_prbar=(ProgressBar)findViewById(R.id.playerBar);
        logos_prbar=(ProgressBar)findViewById(R.id.logoBar);
        questions_prbar=(ProgressBar)findViewById(R.id.questionBar);
        all_prbar=(ProgressBar)findViewById(R.id.allBar);

        players_number=(TextView)findViewById(R.id.playerNumber);
        logos_number=(TextView)findViewById(R.id.logoNumber);
        questions_number=(TextView)findViewById(R.id.questionNumber);
        all_number=(TextView)findViewById(R.id.allNumber);

        players_percent=(TextView)findViewById(R.id.playerPercent);
        logos_percent=(TextView)findViewById(R.id.logoPercent);
        questions_percent=(TextView)findViewById(R.id.questionPercent);
        all_percent=(TextView)findViewById(R.id.allPercent);

        try {
            for (int i = 0; i < 500; i++)
                players_scores.add(sharedPref.getInt("score_players_" + i, 0));
        } catch (NullPointerException e) {
            FirebaseCrash.report(e);
        }

        try {
            for (int i = 0; i < 500; i++)
                logos_scores.add(sharedPref.getInt("score_logos_" + i, 0));
        } catch (NullPointerException e) {
            FirebaseCrash.report(e);

        }

        try {
            for (int i = 1; i < 25; i++)
                questions_scores.add(sharedPref.getInt("score_Q" + i, 0));
        } catch (NullPointerException e) {
            FirebaseCrash.report(e);
        }


        players = Collections.frequency(players_scores, 1);
        logos=Collections.frequency(logos_scores, 1);

        for(int i=0;i<questions_scores.size();i++){
            questions=questions+questions_scores.get(i);
        }



        players_number.setText(String.valueOf(players));
        players_prbar.setProgress(players);
        double  per1=((double) players/500)*100;
        int perInt1= (int) per1;
        players_percent.setText(String.valueOf(perInt1)+"%");

        logos_number.setText(String.valueOf(logos));
        logos_prbar.setProgress(logos);
        double  per2=((double) logos/500)*100;
        int perInt2= (int) per2;
        logos_percent.setText(String.valueOf(perInt2)+"%");

        questions_number.setText(String.valueOf(questions));
        questions_prbar.setProgress(questions);
        double  per3=((double) questions/500)*100;
        int perInt3= (int) per3;
        questions_percent.setText(String.valueOf(perInt3)+"%");

        int n=players+logos+questions;
        all_number.setText(String.valueOf(n));
        all_prbar.setProgress(n);

        double  per4=((double) (questions+logos+players)/1500)*100;
        int perInt4= (int) per4;
        all_percent.setText(String.valueOf(perInt4)+"%");


        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-6229589484617184~3409426273");

        mAdView = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

}
