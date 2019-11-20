package com.dendell.studio.footballquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelStartEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Suhapitanja extends AppCompatActivity implements RewardedVideoAdListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private String token="";
    private Scanner file= null;

    private AdView mAdView;
    private RewardedVideoAd mAd;
    private InterstitialAd mAdinter;

    private int btnIDint;
    private int levelFromScore;

    private List<String> temps=new ArrayList<>();
    private List<String> questions=new ArrayList<>();
    private List<String> answers=new ArrayList<>();
    private List<String> wronganswer1=new ArrayList<>();
    private List<String> wronganswer2=new ArrayList<String>();
    private List<String> wronganswer3=new ArrayList<String>();
    private List<String> hints = new ArrayList<>();
    private List<Button> wrtemp=new ArrayList<>();

    private TextView question,hintremain, qprogress;
    private List<Button> answer1234=new ArrayList<>();
    private ImageButton hint1,hint2, hint3, adsButton;

    private List<Integer> randomnumbers = new ArrayList<>();
    private List<String> randomAnswers = new ArrayList<>();
    private int count=0;
    private int countRandom;

    private String ans1,ans2,ans3,ans4;

    private int correctAnswertemp=0;
    private int countcorrectAnswer=0;

    private int numberLevel;
    private int countQuestion=1;

    private int hintsQuestions;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;

    private PopupWindow popupWindow1;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;

    private ConstraintLayout constraintLayout;

    private int oldScore;

    private  ViewGroup container1;
    private Button no, advideo1,advideo2,advideo3,apprate;
    private int numberOfVideo;

    private Handler handler1,handler2,handler3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.suhapitanja);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Questions Pitanja");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Questions Pitanja");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "start");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        constraintLayout = (ConstraintLayout) findViewById(R.id.relativeQ);

        sharedPref = getSharedPreferences("score",MODE_PRIVATE);
        mEditor = sharedPref.edit();

        Intent btnId = getIntent();
        btnIDint = btnId.getIntExtra("pitanjeLevel", 0);

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-6229589484617184~3409426273");

        mAdView = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdinter = new InterstitialAd(this);
        mAdinter.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //testni Ad
        mAdinter.loadAd(new AdRequest.Builder().build());

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        question= (TextView) findViewById(R.id.dryquestions);

        for (int i=1;i<5;i++){
            String textscore="dryanswer"+(i);
            int texscoreid = getResources().getIdentifier(textscore, "id", getPackageName());
            answer1234.add((Button) findViewById(texscoreid));
        }

        hint1 = (ImageButton) findViewById(R.id.questionsHint1);
        hint2 = (ImageButton) findViewById(R.id.questionsHint2);
        hint3 = (ImageButton) findViewById(R.id.questionsHint3);
        adsButton = (ImageButton) findViewById(R.id.adsButtonQ);

        hintremain=(TextView) findViewById(R.id.hintsQuestionAll);
        qprogress=(TextView) findViewById(R.id.questionProgress);

        for (int i = 0; i < 20; i++) {randomnumbers.add(i);}
        Collections.shuffle(randomnumbers);

        levelFromScore = btnId.getIntExtra("level", 0);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container1 = (ViewGroup) layoutInflater.inflate(R.layout.popup_layout, linearLayout, false);
        no = container1.findViewById(R.id.noad);
        advideo1 = container1.findViewById(R.id.advideo1);
        advideo2 = container1.findViewById(R.id.advideo2);
        advideo3 = container1.findViewById(R.id.advideo3);
        apprate = container1.findViewById(R.id.apprate);

        Global.questions_for_results=new ArrayList<>();
        Global.answer_for_results=new ArrayList<>();
        Global.true_false_for_results=new ArrayList<>();


        adsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickpopupAds();
            }
        });

        handler1=new Handler();
        handler2=new Handler();
        handler3=new Handler();

        importFiles();
    }

    private void importFiles (){
        if (btnIDint==0){
            switch (levelFromScore) {
                case (1):
                    importQuestionsAnswers("pitanjaL1.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 1"));
                    numberLevel = 1;
                    break;
                case (2):
                    importQuestionsAnswers("pitanjaL2.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 2"));
                    numberLevel = 2;
                    break;
                case (3):
                    importQuestionsAnswers("pitanjaL3.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 3"));
                    numberLevel = 3;
                    break;
                case (4):
                    importQuestionsAnswers("pitanjaL4.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 4"));
                    numberLevel = 4;
                    break;
                case (5):
                    importQuestionsAnswers("pitanjaL5.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 5"));
                    numberLevel = 5;
                    break;
                case (6):
                    importQuestionsAnswers("pitanjaL6.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 6"));
                    numberLevel = 6;
                    break;
                case (7):
                    importQuestionsAnswers("pitanjaL7.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 7"));
                    numberLevel = 7;
                    break;
                case (8):
                    importQuestionsAnswers("pitanjaL8.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 8"));
                    numberLevel = 8;
                    break;
                case (9):
                    importQuestionsAnswers("pitanjaL9.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 9"));
                    numberLevel = 9;
                    break;
                case (10):
                    importQuestionsAnswers("pitanjaL10.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 10"));
                    numberLevel = 10;
                    break;
                case (11):
                    importQuestionsAnswers("pitanjaL11.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 11"));
                    numberLevel = 11;
                    break;
                case (12):
                    importQuestionsAnswers("pitanjaL12.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 12"));
                    numberLevel = 12;
                    break;
                case (13):
                    importQuestionsAnswers("pitanjaL13.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 13"));
                    numberLevel = 13;
                    break;
                case (14):
                    importQuestionsAnswers("pitanjaL14.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 14"));
                    numberLevel = 14;
                    break;
                case (15):
                    importQuestionsAnswers("pitanjaL15.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 15"));
                    numberLevel = 15;
                    break;
                case (16):
                    importQuestionsAnswers("pitanjaL16.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 16"));
                    numberLevel = 16;
                    break;
                case (17):
                    importQuestionsAnswers("pitanjaL17.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 17"));
                    numberLevel = 17;
                    break;
                case (18):
                    importQuestionsAnswers("pitanjaL18.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 18"));
                    numberLevel = 18;
                    break;
                case (19):
                    importQuestionsAnswers("pitanjaL19.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 19"));
                    numberLevel = 19;
                    break;
                case (20):
                    importQuestionsAnswers("pitanjaL20.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 20"));
                    numberLevel = 20;
                    break;
                case (21):
                    importQuestionsAnswers("pitanjaL21.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 21"));
                    numberLevel = 21;
                    break;
                case (22):
                    importQuestionsAnswers("pitanjaL22.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 22"));
                    numberLevel = 22;
                    break;
                case (23):
                    importQuestionsAnswers("pitanjaL23.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 23"));
                    numberLevel = 23;
                    break;
                case (24):
                    importQuestionsAnswers("pitanjaL24.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 24"));
                    numberLevel = 24;
                    break;
                case (25):
                    importQuestionsAnswers("pitanjaL25.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 25"));
                    numberLevel = 25;
                    break;
                default:
                    Log.e("error","Level does not exist!");
                    break;
            }
        }
        else {
            switch (btnIDint) {
                case (R.id.questionsLayout1):
                    importQuestionsAnswers("pitanjaL1.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 1"));
                    numberLevel = 1;
                    break;
                case (R.id.questionsLayout2):
                    importQuestionsAnswers("pitanjaL2.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 2"));
                    numberLevel = 2;
                    break;
                case (R.id.questionsLayout3):
                    importQuestionsAnswers("pitanjaL3.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 3"));
                    numberLevel = 3;
                    break;
                case (R.id.questionsLayout4):
                    importQuestionsAnswers("pitanjaL4.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 4"));
                    numberLevel = 4;
                    break;
                case (R.id.questionsLayout5):
                    importQuestionsAnswers("pitanjaL5.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 5"));
                    numberLevel = 5;
                    break;
                case (R.id.questionsLayout6):
                    importQuestionsAnswers("pitanjaL6.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 6"));
                    numberLevel = 6;
                    break;
                case (R.id.questionsLayout7):
                    importQuestionsAnswers("pitanjaL7.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 7"));
                    numberLevel = 7;
                    break;
                case (R.id.questionsLayout8):
                    importQuestionsAnswers("pitanjaL8.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 8"));
                    numberLevel = 8;
                    break;
                case (R.id.questionsLayout9):
                    importQuestionsAnswers("pitanjaL9.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 9"));
                    numberLevel = 9;
                    break;
                case (R.id.questionsLayout10):
                    importQuestionsAnswers("pitanjaL10.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 10"));
                    numberLevel = 10;
                    break;
                case (R.id.questionsLayout11):
                    importQuestionsAnswers("pitanjaL11.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 11"));
                    numberLevel = 11;
                    break;
                case (R.id.questionsLayout12):
                    importQuestionsAnswers("pitanjaL12.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 12"));
                    numberLevel = 12;
                    break;
                case (R.id.questionsLayout13):
                    importQuestionsAnswers("pitanjaL13.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 13"));
                    numberLevel = 13;
                    break;
                case (R.id.questionsLayout14):
                    importQuestionsAnswers("pitanjaL14.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 14"));
                    numberLevel = 14;
                    break;
                case (R.id.questionsLayout15):
                    importQuestionsAnswers("pitanjaL15.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 15"));
                    numberLevel = 15;
                    break;
                case (R.id.questionsLayout16):
                    importQuestionsAnswers("pitanjaL16.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 16"));
                    numberLevel = 16;
                    break;
                case (R.id.questionsLayout17):
                    importQuestionsAnswers("pitanjaL17.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 17"));
                    numberLevel = 17;
                    break;
                case (R.id.questionsLayout18):
                    importQuestionsAnswers("pitanjaL18.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 18"));
                    numberLevel = 18;
                    break;
                case (R.id.questionsLayout19):
                    importQuestionsAnswers("pitanjaL19.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 19"));
                    numberLevel = 19;
                    break;
                case (R.id.questionsLayout20):
                    importQuestionsAnswers("pitanjaL20.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 20"));
                    numberLevel = 20;
                    break;
                case (R.id.questionsLayout21):
                    importQuestionsAnswers("pitanjaL21.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 21"));
                    numberLevel = 21;
                    break;
                case (R.id.questionsLayout22):
                    importQuestionsAnswers("pitanjaL22.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 22"));
                    numberLevel = 22;
                    break;
                case (R.id.questionsLayout23):
                    importQuestionsAnswers("pitanjaL23.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 23"));
                    numberLevel = 23;
                    break;
                case (R.id.questionsLayout24):
                    importQuestionsAnswers("pitanjaL24.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 24"));
                    numberLevel = 24;
                    break;
                case (R.id.questionsLayout25):
                    importQuestionsAnswers("pitanjaL25.txt");
                    Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Questions Level 25"));
                    numberLevel = 25;
                    break;
                default:
                    Log.e("error","Level does not exist!");
                    break;
            }
        }
    }

    private void importQuestionsAnswers(String nametxt){
        try {
            DataInputStream textFileStream = new DataInputStream(getAssets().open(String.format(nametxt)));
            file = new Scanner(textFileStream).useDelimiter("%\\s*");

            while (file.hasNext()){
                token=file.next();
                temps.add(token);
            }

            file.close();

            String[] a;

            for (String x:temps) {
                String del = Pattern.quote(";");
                a=x.split(del);
                questions.add(a[0]);
                answers.add(a[1]);
                wronganswer1.add(a[2]);
                wronganswer2.add(a[3]);
                wronganswer3.add(a[4]);
                hints.add(a[5]);
            }
            showquestions();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    }

    private void showquestions(){
        countRandom=randomnumbers.get(count);

        question.setText(questions.get(countRandom));

        Global.questions_for_results.add(questions.get(countRandom));

        ans1=answers.get(countRandom);
        ans2=wronganswer1.get(countRandom);
        ans3=wronganswer2.get(countRandom);
        ans4=wronganswer3.get(countRandom);

        randomAnswers.add(ans1);
        randomAnswers.add(ans2);
        randomAnswers.add(ans3);
        randomAnswers.add(ans4);

        Collections.shuffle(randomAnswers);

        if (randomAnswers.get(0).equals(ans1)) correctAnswertemp=R.id.dryanswer1;
        else {
            if (randomAnswers.get(1).equals(ans1)) correctAnswertemp=R.id.dryanswer2;
            else {
                if (randomAnswers.get(2).equals(ans1)) correctAnswertemp=R.id.dryanswer3;
                else {
                    if (randomAnswers.get(3).equals(ans1)) correctAnswertemp=R.id.dryanswer4;
                }
            }
        }

        for (int i=0;i<4;i++){
            answer1234.get(i).setText(randomAnswers.get(i));
        }
        remainingHints();
    }

    public void odgovorKlik(View view){

        hint1.setEnabled(true);
        hint2.setEnabled(true);
        hint3.setEnabled(true);
        hint1.setBackgroundResource(R.drawable.infoicon);
        hint2.setBackgroundResource(R.drawable.halficon);
        hint3.setBackgroundResource(R.drawable.correcticon);

        countQuestion++;

        if (view.getId()==correctAnswertemp) {
            countcorrectAnswer++;
            Global.true_false_for_results.add(true);
        }
        else Global.true_false_for_results.add(false);

        String clicked_answer= (String) ((Button)view).getText();
        Global.answer_for_results.add(clicked_answer);

        for (int i=0;i<4;i++){
            answer1234.get(i).setBackgroundResource(R.drawable.button_bg);
            answer1234.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            answer1234.get(i).setEnabled(true);
        }


        if(count<19) {
            count++;
            randomAnswers.clear();
            showquestions();
        }
        else {

            if (btnIDint==0){
                switch (levelFromScore) {
                    case (1):
                        oldScore = sharedPref.getInt("score_Q1", 0);
                        if (countcorrectAnswer>oldScore) mEditor.putInt("score_Q1", countcorrectAnswer).commit();
                        break;
                    case (2):
                        oldScore = sharedPref.getInt("score_Q2", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q2", countcorrectAnswer).commit();
                        break;
                    case (3):
                        oldScore = sharedPref.getInt("score_Q3", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q3", countcorrectAnswer).commit();
                        break;
                    case (4):
                        oldScore = sharedPref.getInt("score_Q4", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q4", countcorrectAnswer).commit();
                        break;
                    case (5):
                        oldScore = sharedPref.getInt("score_Q5", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q5", countcorrectAnswer).commit();
                        break;
                    case (6):
                        oldScore = sharedPref.getInt("score_Q6", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q6", countcorrectAnswer).commit();
                        break;
                    case (7):
                        oldScore = sharedPref.getInt("score_Q7", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q7", countcorrectAnswer).commit();
                        break;
                    case (8):
                        oldScore = sharedPref.getInt("score_Q8", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q8", countcorrectAnswer).commit();
                        break;
                    case (9):
                        oldScore = sharedPref.getInt("score_Q9", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q9", countcorrectAnswer).commit();
                        break;
                    case (10):
                        oldScore = sharedPref.getInt("score_Q10", 0);
                        if (countcorrectAnswer>oldScore) mEditor.putInt("score_Q10", countcorrectAnswer).commit();
                        break;
                    case (11):
                        oldScore = sharedPref.getInt("score_Q11", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q11", countcorrectAnswer).commit();
                        break;
                    case (12):
                        oldScore = sharedPref.getInt("score_Q12", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q12", countcorrectAnswer).commit();
                        break;
                    case (13):
                        oldScore = sharedPref.getInt("score_Q13", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q13", countcorrectAnswer).commit();
                        break;
                    case (14):
                        oldScore = sharedPref.getInt("score_Q14", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q14", countcorrectAnswer).commit();
                        break;
                    case (15):
                        oldScore = sharedPref.getInt("score_Q15", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q15", countcorrectAnswer).commit();
                        break;
                    case (16):
                        int oldScore16 = sharedPref.getInt("score_Q16", 0);
                        if (countcorrectAnswer>oldScore16)mEditor.putInt("score_Q16", countcorrectAnswer).commit();
                        break;
                    case (17):
                        oldScore = sharedPref.getInt("score_Q17", 0);
                        if (countcorrectAnswer>oldScore) mEditor.putInt("score_Q17", countcorrectAnswer).commit();
                        break;
                    case (18):
                        oldScore = sharedPref.getInt("score_Q18", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q18", countcorrectAnswer).commit();
                        break;
                    case (19):
                        oldScore = sharedPref.getInt("score_Q19", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q19", countcorrectAnswer).commit();
                        break;
                    case (20):
                        oldScore = sharedPref.getInt("score_Q20", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q20", countcorrectAnswer).commit();
                        break;
                    case (21):
                        oldScore = sharedPref.getInt("score_Q21", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q21", countcorrectAnswer).commit();
                        break;
                    case (22):
                        oldScore = sharedPref.getInt("score_Q22", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q22", countcorrectAnswer).commit();
                        break;
                    case (23):
                        oldScore = sharedPref.getInt("score_Q23", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q23", countcorrectAnswer).commit();
                        break;
                    case (24):
                        oldScore = sharedPref.getInt("score_Q24", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q24", countcorrectAnswer).commit();
                        break;
                    case (25):
                        oldScore = sharedPref.getInt("score_Q25", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q25", countcorrectAnswer).commit();
                        break;
                    default:
                        break;
                }
            } else {

                switch (btnIDint) {
                    case (R.id.questionsLayout1):
                        oldScore = sharedPref.getInt("score_Q1", 0);
                        if (countcorrectAnswer>oldScore) mEditor.putInt("score_Q1", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout2):
                        oldScore = sharedPref.getInt("score_Q2", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q2", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout3):
                        oldScore = sharedPref.getInt("score_Q3", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q3", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout4):
                        oldScore = sharedPref.getInt("score_Q4", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q4", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout5):
                        oldScore = sharedPref.getInt("score_Q5", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q5", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout6):
                        oldScore = sharedPref.getInt("score_Q6", 0);
                        if (countcorrectAnswer>oldScore) mEditor.putInt("score_Q6", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout7):
                        oldScore = sharedPref.getInt("score_Q7", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q7", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout8):
                        oldScore = sharedPref.getInt("score_Q8", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q8", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout9):
                        oldScore = sharedPref.getInt("score_Q9", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q9", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout10):
                        oldScore = sharedPref.getInt("score_Q10", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q10", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout11):
                        oldScore = sharedPref.getInt("score_Q11", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q11", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout12):
                        oldScore = sharedPref.getInt("score_Q12", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q12", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout13):
                        oldScore = sharedPref.getInt("score_Q13", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q13", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout14):
                        oldScore = sharedPref.getInt("score_Q14", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q14", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout15):
                        oldScore = sharedPref.getInt("score_Q15", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q15", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout16):
                        oldScore = sharedPref.getInt("score_Q16", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q16", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout17):
                        oldScore = sharedPref.getInt("score_Q17", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q17", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout18):
                        oldScore = sharedPref.getInt("score_Q18", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q18", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout19):
                        oldScore = sharedPref.getInt("score_Q19", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q19", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout20):
                        oldScore = sharedPref.getInt("score_Q20", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q20", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout21):
                        oldScore = sharedPref.getInt("score_Q21", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q21", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout22):
                        oldScore = sharedPref.getInt("score_Q22", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q22", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout23):
                        oldScore = sharedPref.getInt("score_Q23", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q23", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout24):
                        oldScore = sharedPref.getInt("score_Q24", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q24", countcorrectAnswer).commit();
                        break;
                    case (R.id.questionsLayout25):
                        oldScore = sharedPref.getInt("score_Q25", 0);
                        if (countcorrectAnswer>oldScore)mEditor.putInt("score_Q25", countcorrectAnswer).commit();
                        break;
                    default:
                        break;
                }
            }

            Intent intent = new Intent(this, ShowScore.class);
            intent.putExtra("showScore", countcorrectAnswer);
            intent.putExtra("showLevel", numberLevel);
            intent.putExtra("oldscore",oldScore);
            startActivity(intent);

            if (mAdinter.isLoaded()) {
                mAdinter.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

            finish();


        }

    }

    public void hintsQuestions(View view){
        switch (view.getId()){
            case (R.id.questionsHint1):
                if (hintsQuestions<Global.hint1pointsq){
                    setOnClickpopupAds();
                }
                else {
                    hintsQuestions=hintsQuestions-Global.hint1pointsq;
                    mEditor.putInt("hints_questions", hintsQuestions);
                    mEditor.commit();

                    AlertDialog.Builder builder=new AlertDialog.Builder(Suhapitanja.this);
                    builder.setTitle("Information");
                    builder.setMessage(hints.get(countRandom));
                    builder.setPositiveButton("Close", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                    hint1.setEnabled(false);
                    hint1.setBackgroundResource(R.drawable.infoicon_disabled);
                }

                break;

            case (R.id.questionsHint2):
                if (hintsQuestions<Global.hint2pointsq){
                    setOnClickpopupAds();
                }
                else {
                    hintsQuestions = hintsQuestions - Global.hint2pointsq;
                    mEditor.putInt("hints_questions", hintsQuestions);
                    mEditor.commit();

                    wrtemp=new ArrayList<>();

                    for (int i=0;i<4;i++) {
                        if (answer1234.get(i).getId() != correctAnswertemp) {
                            wrtemp.add(answer1234.get(i));
                        }
                    }

                    Collections.shuffle(wrtemp);

                    for (int i=0;i<2;i++){
                        wrtemp.get(i).setEnabled(false);
                        wrtemp.get(i).setBackgroundResource(R.drawable.disable_button);
                        wrtemp.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                    }
                    hint2.setEnabled(false);
                    hint2.setBackgroundResource(R.drawable.halficon_disabled);
                }

                break;

            case (R.id.questionsHint3):
                if (hintsQuestions<Global.hint3pointsq){
                    setOnClickpopupAds();
                }
                else {
                    hintsQuestions = hintsQuestions - Global.hint3pointsq;
                    mEditor.putInt("hints_questions", hintsQuestions);
                    mEditor.commit();

                    wrtemp=new ArrayList<>();

                    for (int i=0;i<4;i++) {
                        if (answer1234.get(i).getId() != correctAnswertemp) {
                            wrtemp.add(answer1234.get(i));

                        }
                        else answer1234.get(i).setBackgroundResource(R.drawable.correct_answer_button);

                    }
                    Collections.shuffle(wrtemp);

                    for (int i=0;i<3;i++){
                        wrtemp.get(i).setEnabled(false);
                        wrtemp.get(i).setBackgroundResource(R.drawable.disable_button);
                        wrtemp.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                    }
                    hint1.setEnabled(false);
                    hint1.setBackgroundResource(R.drawable.infoicon_disabled);
                    hint2.setEnabled(false);
                    hint2.setBackgroundResource(R.drawable.halficon_disabled);
                    hint3.setEnabled(false);
                    hint3.setBackgroundResource(R.drawable.correcticon_disabled);
                }

                break;

            default: Log.e("error", "pogresan buttonId");
                break;
        }
        remainingHints();
    }

    private void remainingHints(){
        qprogress.setText(countQuestion+"/20");
        hintsQuestions=sharedPref.getInt("hints_questions", Global.hintsStart);
        hintremain.setText(String.valueOf(hintsQuestions));
    }

    @Override
    public void onBackPressed() {

        stoptimers_savestop();
        Intent intent = new Intent(this, Questions.class);
        startActivity(intent);
        finish();
    }

    private Runnable timer1 = new Runnable() {

        @Override
        public void run() {

            if(Global.qtimerVideo1*1000>0) {

                advideo1.setEnabled(false);

                Global.qtimerTracker1 = true;
                mEditor.putBoolean("qtime_tracker_1", true);
                mEditor.commit();

                Global.qtimerVideo1--;

                mEditor.putLong("qtimer_video_1_left", Global.qtimerVideo1);
                mEditor.commit();

                long hourT1 = Global.qtimerVideo1 / 3600;
                long minT1 = Global.qtimerVideo1 / 60;
                long secT1 = Global.qtimerVideo1 % 60;

                if (minT1 < 10) {
                    if (secT1 < 10)
                        advideo1.setText(String.valueOf(hourT1) + " : 0" + String.valueOf(minT1) + " : 0" + String.valueOf(secT1));
                    else
                        advideo1.setText(String.valueOf(hourT1) + " : 0" + String.valueOf(minT1) + " : " + String.valueOf(secT1));
                } else
                    advideo1.setText(String.valueOf(hourT1) + " : " + String.valueOf(minT1) + " : " + String.valueOf(secT1));

                handler1.postDelayed(timer1,1000);
            }

            else {
                advideo1.setEnabled(true);
                advideo1.setText(String.valueOf("Watch ad"));

                Global.qtimerTracker1 = false;
                mEditor.putBoolean("qtime_tracker_1", false);
                mEditor.commit();

                Global.qtimerVideo1 = 0;
                mEditor.putLong("qtimer_video_1_left", 0);
                mEditor.commit();
                handler1.removeCallbacks(timer1);
            }

        }
    };


    private Runnable timer2 = new Runnable() {

        @Override
        public void run() {

            if (Global.qtimerVideo2 * 1000 > 0) {
                advideo2.setEnabled(false);

                Global.qtimerTracker2 = true;
                mEditor.putBoolean("qtime_tracker_2", Global.qtimerTracker2);
                mEditor.commit();

                Global.qtimerVideo2--;

                mEditor.putLong("qtimer_video_2_left", Global.qtimerVideo2);
                mEditor.commit();

                long hourT2 = Global.qtimerVideo2 / 3600;
                long minT2 = Global.qtimerVideo2 / 60;
                long secT2 = Global.qtimerVideo2 % 60;

                if (minT2 < 10) {
                    if (secT2 < 10)
                        advideo2.setText(String.valueOf(hourT2) + " : 0" + String.valueOf(minT2) + " : 0" + String.valueOf(secT2));
                    else
                        advideo2.setText(String.valueOf(hourT2) + " : 0" + String.valueOf(minT2) + " : " + String.valueOf(secT2));
                } else
                    advideo2.setText(String.valueOf(hourT2) + " : " + String.valueOf(minT2) + " : " + String.valueOf(secT2));

                handler2.postDelayed(timer2,1000);

            } else {

                advideo2.setEnabled(true);
                advideo2.setText(String.valueOf("Watch ad"));

                Global.qtimerTracker2 = false;
                mEditor.putBoolean("qtime_tracker_2", Global.qtimerTracker2);
                mEditor.commit();

                Global.qtimerVideo2 = 0;
                mEditor.putLong("qtimer_video_2_left", 0);
                mEditor.commit();

                handler2.removeCallbacks(timer2);
            }

        }
    };


    private Runnable timer3 = new Runnable() {

        @Override
        public void run() {

            if (Global.qtimerVideo3 * 1000 > 0) {
                advideo3.setEnabled(false);

                Global.qtimerTracker3 = true;
                mEditor.putBoolean("qtime_tracker_3", Global.qtimerTracker3);
                mEditor.commit();

                Global.qtimerVideo3--;

                mEditor.putLong("qtimer_video_3_left", Global.qtimerVideo3);
                mEditor.commit();

                long hourT3=Global.qtimerVideo3/3600;
                long minT3=Global.qtimerVideo3/60;
                long secT3=Global.qtimerVideo3%60;

                if (minT3<10) {
                    if (secT3 < 10)
                        advideo3.setText(String.valueOf(hourT3) + " : 0" + String.valueOf(minT3) + " : 0" + String.valueOf(secT3));
                    else
                        advideo3.setText(String.valueOf(hourT3) + " : 0" + String.valueOf(minT3) + " : " + String.valueOf(secT3));
                }
                else advideo3.setText(String.valueOf(hourT3) + " : " + String.valueOf(minT3) + " : " + String.valueOf(secT3));

                handler3.postDelayed(timer3,1000);
            }

            else{

                advideo3.setEnabled(true);
                advideo3.setText(String.valueOf("Watch ad"));

                Global.qtimerTracker3 = false;
                mEditor.putBoolean("qtime_tracker_3", Global.qtimerTracker3);
                mEditor.commit();

                Global.qtimerVideo3 = 0;
                mEditor.putLong("qtimer_video_3_left", 0);
                mEditor.commit();

                handler3.removeCallbacks(timer3);
            }
        }

    };


    public void setOnClickpopupAds() {
        popupWindow1 = new PopupWindow(container1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow1.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        long starttime=System.currentTimeMillis();
        mEditor.putLong("qtime_start_1", starttime);
        mEditor.commit();

        long stoptime=sharedPref.getLong("qtime_stop_1",0);

        long timeleft1=sharedPref.getLong("qtimer_video_1_left",Global.qtimerVideo1);
        long timeleft2=sharedPref.getLong("qtimer_video_2_left",Global.qtimerVideo2);
        long timeleft3=sharedPref.getLong("qtimer_video_3_left",Global.qtimerVideo3);

        long raz=(starttime-stoptime)/1000;

        Boolean ttracker1=sharedPref.getBoolean("qtime_tracker_1",false);
        Boolean ttracker2=sharedPref.getBoolean("qtime_tracker_2",false);
        Boolean ttracker3=sharedPref.getBoolean("qtime_tracker_3",false);

        if (timeleft1!=0){
            Global.qtimerVideo1 = timeleft1 - raz;
            if ( ttracker1 && !Global.qtimerTracker1) {
                handler1.postDelayed(timer1,1000);

            }
            else if (ttracker1 && Global.qtimerTracker1) {
                handler1.removeCallbacksAndMessages(null);
                handler1.postDelayed(timer1, 1000);
            }
        }


        if (timeleft2!=0){
            Global.qtimerVideo2 = timeleft2 - raz;
            if (ttracker2 && !Global.qtimerTracker2) {
                handler2.postDelayed(timer2,1000);
            }
            else if (ttracker2 && Global.qtimerTracker2){
                handler2.removeCallbacksAndMessages(null);
                handler2.postDelayed(timer2, 1000);
            }
        }

        if (timeleft3!=0){
            Global.qtimerVideo3 = timeleft3 - raz;
            if (ttracker3 && !Global.qtimerTracker3) {
                handler3.postDelayed(timer3,1000);
            }
            else if (ttracker3 && Global.qtimerTracker3){
                handler3.removeCallbacksAndMessages(null);
                handler3.postDelayed(timer3, 1000);
            }
        }


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stoptimers_savestop();
                popupWindow1.dismiss();
            }
        });

        apprate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + this.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
*/
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.dendell.denix.myapplication")));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.dendell.denix.myapplication")));
                }

                popupWindow1.dismiss();

            }
        });

        advideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAd.isLoaded()) {
                    numberOfVideo=1;
                    mAd.show();
                    Global.qtimerVideo1=Global.adTimer1;
                    handler1.postDelayed(timer1,1000);
                    popupWindow1.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Video is loading...\n" +"Please try again in couple of seconds.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        advideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAd.isLoaded()) {
                    numberOfVideo=2;
                    mAd.show();
                    Global.qtimerVideo2=Global.adTimer2;
                    handler2.postDelayed(timer2,1000);
                    popupWindow1.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Video is loading...\n" +"Please try again in couple of seconds.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        advideo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAd.isLoaded()) {
                    numberOfVideo=3;
                    mAd.show();
                    Global.qtimerVideo3=Global.adTimer3;
                    handler3.postDelayed(timer3,1000);
                    popupWindow1.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Video is loading...\n" +"Please try again in couple of seconds.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void stoptimers_savestop(){
        long stoptime=System.currentTimeMillis();
        mEditor.putLong("qtime_stop_1", stoptime);
        mEditor.commit();

        handler1.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
        handler3.removeCallbacksAndMessages(null);
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
        stoptimers_savestop();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        loadRewardedVideoAd();
        stoptimers_savestop();
        addhintsvideo();
    }

    private void addhintsvideo() {
        if (numberOfVideo==1) hintsQuestions = hintsQuestions + Global.hintsAfterVideo1;
        else if (numberOfVideo==2) hintsQuestions = hintsQuestions + Global.hintsAfterVideo2;
        else if (numberOfVideo==3) hintsQuestions = hintsQuestions + Global.hintsAfterVideo3;
        else Log.e("error","wrong number of video");

        mEditor.putInt("hints_questions", hintsQuestions);
        mEditor.commit();
        remainingHints();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {}

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