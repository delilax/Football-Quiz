package com.dendell.studio.footballquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.crash.FirebaseCrash;


public class ShowScore extends AppCompatActivity implements RewardedVideoAdListener {

    private int showScore,showLevel,oldScore=0;
    private int nextLevel;
    private TextView score,percent,newhints,scoretext;

    private int sc=0, hintsQuestions,newanswer,razScore;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;

    private AdView mAdView;

    private RewardedVideoAd mAd;

    private Button btnAdd_yes;

    private boolean soundToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.showscore);

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-6229589484617184~3409426273");

        mAdView = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();


        MediaPlayer fansgoal= MediaPlayer.create(ShowScore.this,R.raw.fansgoal);
        MediaPlayer fansfail= MediaPlayer.create(ShowScore.this,R.raw.fansfail);

        score=(TextView) findViewById(R.id.scoreEnd);
        percent=(TextView) findViewById(R.id.scoreEndPercent);
        Button nlvl=(Button) findViewById(R.id.nextlvl);
        btnAdd_yes = (Button) findViewById(R.id.ad_buttonQ_Yes);
        newhints=(TextView) findViewById(R.id.showNewHints);
        scoretext=(TextView) findViewById(R.id.scoreText);

        Intent btnId = getIntent();
        showScore = btnId.getIntExtra("showScore", 0);
        showLevel = btnId.getIntExtra("showLevel", 0);
        oldScore = btnId.getIntExtra("oldscore", 0);

        nextLevel=showLevel+1;

        double  per=((double) showScore/20)*100;
        int perInt= (int) per;

        score.setText(String.valueOf(showScore)+"/20");
        percent.setText(String.valueOf(perInt)+"%");

        sharedPref = getSharedPreferences("score",MODE_PRIVATE);
        mEditor = sharedPref.edit();

        hintsQuestions=sharedPref.getInt("hints_questions", Global.hintsStart);

        try {
            sc=sharedPref.getInt("score_Q" + showLevel, 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

        if (showLevel<=15 && sc>=15) nlvl.setEnabled(true);
        else if(showLevel>15 && showLevel<25 && sc>10) nlvl.setEnabled(true);
        else nlvl.setEnabled(false);

        if (oldScore>=showScore){
            btnAdd_yes.setEnabled(false);
            btnAdd_yes.setBackgroundResource(R.drawable.border_disable);
        }
        else{
            btnAdd_yes.setEnabled(true);
            btnAdd_yes.setBackgroundResource(R.drawable.button_bg_green);
        }


        newanswer=showScore-oldScore;
        razScore=newanswer*Global.hintCorrectQuestion;
        hintsQuestions = hintsQuestions + razScore;
        if (newanswer>=0) {
            mEditor.putInt("hints_questions", hintsQuestions);
            mEditor.commit();
            scoretext.setText(String.valueOf(newanswer));
            if (razScore==1) newhints.setText(razScore+"hint");
            else newhints.setText("You got "+razScore+" hints");
        }
        else {
            scoretext.setText(String.valueOf(0));
            newhints.setText("You got "+0+" hints");
        }

        try{
            soundToggle=sharedPref.getBoolean("soundToggle",true);
        } catch (NullPointerException e) {
            mEditor.putBoolean("soundToggle", true);
            mEditor.commit();
        }

        if (soundToggle) {
            if (showLevel < 16) {
                if (showScore >= 15) fansgoal.start();
                else fansfail.start();
            } else if (showLevel >= 16) {
                if (showScore >= 10) fansgoal.start();
                else fansfail.start();
            }
        }


        btnAdd_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAdd(v);
            }
        });
    }


    private  void runAdd(View view) {
        if (newanswer>=0){
            if (mAd.isLoaded()) {
                mAd.show();
                btnAdd_yes.setEnabled(false);
                btnAdd_yes.setBackgroundResource(R.drawable.border_disable);
            }
            else{
                Toast.makeText(getApplicationContext(),"Video is loading...\n" +"Please try again in couple of seconds.",Toast.LENGTH_SHORT).show();
                btnAdd_yes.setEnabled(true);
                btnAdd_yes.setBackgroundResource(R.drawable.button_bg_green);
            }
        }
    }


    public void backtoLevels(View view) {
        Intent intent = new Intent(this, Questions.class);
        startActivity(intent);
        finish();
    }

    public void replayLevel(View view) {
        Intent intent = new Intent(this, Suhapitanja.class);
        intent.putExtra("level",showLevel);
        startActivity(intent);
        finish();
    }

    public void nextLevel(View view) {
        Intent intent = new Intent(this, Suhapitanja.class);
        intent.putExtra("level",nextLevel);
        startActivity(intent);
        finish();
    }

    public void openResults(View view) {
        Intent intent = new Intent(this, Results.class);
        startActivity(intent);
    }


    private void loadRewardedVideoAd() {
        //mAd.loadAd("ca-app-pub-6229589484617184/3251931023", new AdRequest.Builder().build());
        mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); //test AD
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        Log.v("videoad","loadRewardedVideoAd");
    }

    @Override
    public void onRewardedVideoAdOpened(){}

    @Override
    public void onRewardedVideoStarted(){}

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        loadRewardedVideoAd();
        hintsQuestions = hintsQuestions + razScore;
        mEditor.putInt("hints_questions", hintsQuestions);
        mEditor.commit();

        if (razScore == 1) newhints.setText(razScore * 2 + "hint");
        else newhints.setText("You got " + razScore * 2 + " hints");


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardedVideoAd();
    }

    @Override
    public void onResume() {
        mAd.resume(this);
        super.onResume();

    }

    @Override
    public void onPause() {
        mAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAd.destroy(this);
        super.onDestroy();
    }
}
