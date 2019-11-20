package com.dendell.studio.footballquiz;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelStartEvent;
import com.google.android.gms.ads.AdListener;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import android.os.Handler;
import android.widget.Toast;
import java.util.regex.Pattern;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class LogosPitanja extends AppCompatActivity implements RewardedVideoAdListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private String token = "",btnIDString;
    private Scanner file = null;

    private AdView mAdView;

    private int btnIDint, countQuestion = 1;

    private List<String> temps = new ArrayList<>();
    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<String>();
    private List<String> hints = new ArrayList<String>();

    private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "x", "w", "y", "z"};

    private List<Integer> answersCorrect = new ArrayList<Integer>();
    private List<String> justAnswer = new ArrayList<String>();

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor mEditor;

    private ImageView questionImg;
    private TextView scorelive, hintremain;
    private Button[] btn = new Button[20];
    private Button[] dybtns;
    private Button btnPrevious, btnNext;
    private ImageButton hint1, hint2, hint3,hint4, adsButton;
    private Button bntid;

    private Integer[] scores = new Integer[20];
    private List<Integer> spaces;
    private int spacesInt;

    private int hintsLogo;

    private String[] allletters;

    private PopupWindow popupWindow, popupWindow1;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;

    private RewardedVideoAd mAd;
    private InterstitialAd mAdinter;

    private  ViewGroup container1;
    private Button no, advideo1,advideo2,advideo3,apprate;

    private int numberOfVideo;

    private Handler handler1,handler2,handler3;

    int countChar;
    private Button getBtn;

    int count_word_1;
    int count_word_2;
    int count_word_3;

    Button tempBtn;

    int numberofbuttons =18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.logospitanja);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Logos Pitanja");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Logos Pitanja");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "start");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        constraintLayout = (ConstraintLayout) findViewById(R.id.relativeL);

        Intent btnId = getIntent();
        btnIDint = btnId.getIntExtra("logosLevel", 0);
        btnIDString=btnId.getStringExtra("logosLevel");
        //Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Logos "+btnIDString));
        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-6229589484617184~3409426273");

        mAdView = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdinter = new InterstitialAd(this);
        mAdinter.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //testni Ad
        mAdinter.loadAd(new AdRequest.Builder().build());

        mAdinter.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("TAG","onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("TAG","onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d("TAG","onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d("TAG","onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.d("TAG","onAdClosed");
                mAdinter.loadAd(new AdRequest.Builder().build());
            }
        });

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        hint1 = (ImageButton) findViewById(R.id.logosHint1);
        hint2 = (ImageButton) findViewById(R.id.logosHint2);
        hint3 = (ImageButton) findViewById(R.id.logosHint3);
        hint4 = (ImageButton) findViewById(R.id.logosHint4);

        questionImg = (ImageView) findViewById(R.id.imageLogo);

        btnPrevious = (Button) findViewById(R.id.previous);
        btnNext = (Button) findViewById(R.id.next);
        scorelive = (TextView) findViewById(R.id.scoreLiveLogo);
        hintremain = (TextView) findViewById(R.id.hintsLogoAll);

        adsButton = (ImageButton) findViewById(R.id.adsButtonL);

        for (int i = 1; i < 21; i++) {
            String btnstr = "btn" + i;
            int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
            int manje = i - 1;
            btn[manje] = (Button) findViewById(btnint);
        }

        sharedPref = getSharedPreferences("score", MODE_PRIVATE);
        mEditor = sharedPref.edit();


        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container1 = (ViewGroup) layoutInflater.inflate(R.layout.popup_layout, linearLayout, false);

        no = container1.findViewById(R.id.noad);
        advideo1 = container1.findViewById(R.id.advideo1);
        advideo2 = container1.findViewById(R.id.advideo2);
        advideo3 = container1.findViewById(R.id.advideo3);
        apprate = container1.findViewById(R.id.apprate);

        adsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickpopupAds();
            }
        });

        handler1=new Handler();
        handler2=new Handler();
        handler3=new Handler();

        importScoresAndHints();

    }


    private void importScoresAndHints() {
        remainingHints();
        switch (btnIDint) {
            case (R.id.logosLayout1):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + i, 0);
                importQuestionsAnswers("logosL1.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 1"));
                break;
            case (R.id.logosLayout2):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20), 0);
                importQuestionsAnswers("logosL2.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 2"));
                break;
            case (R.id.logosLayout3):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 2), 0);
                importQuestionsAnswers("logosL3.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 3"));
                break;
            case (R.id.logosLayout4):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 3), 0);
                importQuestionsAnswers("logosL4.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 4"));
                break;
            case (R.id.logosLayout5):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 4), 0);
                importQuestionsAnswers("logosL5.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 5"));
                break;
            case (R.id.logosLayout6):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 5), 0);
                importQuestionsAnswers("logosL6.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 6"));
                break;
            case (R.id.logosLayout7):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 6), 0);
                importQuestionsAnswers("logosL7.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 7"));
                break;
            case (R.id.logosLayout8):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 7), 0);
                importQuestionsAnswers("logosL8.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 8"));
                break;
            case (R.id.logosLayout9):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 8), 0);
                importQuestionsAnswers("logosL9.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 9"));
                break;
            case (R.id.logosLayout10):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 9), 0);
                importQuestionsAnswers("logosL10.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 10"));
                break;
            case (R.id.logosLayout11):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 10), 0);
                importQuestionsAnswers("logosL11.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 11"));
                break;
            case (R.id.logosLayout12):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 11), 0);
                importQuestionsAnswers("logosL12.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 12"));
                break;
            case (R.id.logosLayout13):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 12), 0);
                importQuestionsAnswers("logosL13.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 13"));
                break;
            case (R.id.logosLayout14):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 13), 0);
                importQuestionsAnswers("logosL14.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 14"));
                break;
            case (R.id.logosLayout15):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 14), 0);
                importQuestionsAnswers("logosL15.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 15"));
                break;
            case (R.id.logosLayout16):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 15), 0);
                importQuestionsAnswers("logosL16.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 16"));
                break;
            case (R.id.logosLayout17):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 16), 0);
                importQuestionsAnswers("logosL17.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 17"));
                break;
            case (R.id.logosLayout18):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 17), 0);
                importQuestionsAnswers("logosL18.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 18"));
                break;
            case (R.id.logosLayout19):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 18), 0);
                importQuestionsAnswers("logosL19.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 19"));
                break;
            case (R.id.logosLayout20):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 19), 0);
                importQuestionsAnswers("logosL20.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 20"));
                break;
            case (R.id.logosLayout21):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 20), 0);
                importQuestionsAnswers("logosL21.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 21"));
                break;
            case (R.id.logosLayout22):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 21), 0);
                importQuestionsAnswers("logosL22.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 22"));
                break;
            case (R.id.logosLayout23):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 22), 0);
                importQuestionsAnswers("logosL23.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 23"));
                break;
            case (R.id.logosLayout24):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 23), 0);
                importQuestionsAnswers("logosL24.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 24"));
                break;
            case (R.id.logosLayout25):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_logos_" + (i + 20 * 24), 0);
                importQuestionsAnswers("logosL25.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("LogosLevel 25"));
                break;
            default:
                break;
        }

        scoreLive();

    }


    private void importQuestionsAnswers(String nametxt) {
        try {
            DataInputStream textFileStream = new DataInputStream(getAssets().open(String.format(nametxt)));
            file = new Scanner(textFileStream).useDelimiter("%\\s*");

            while (file.hasNext()) {
                token = file.next();
                temps.add(token);
            }

            file.close();

            String[] a;
            for (String x : temps) {
                String del = Pattern.quote(";");
                a = x.split(del);
                questions.add(a[0]);
                answers.add(a[1]);
                hints.add(a[2]);
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


    private void showquestions() {

        for (int i = 0; i < numberofbuttons; i++) {
            String btnstr2 = "ans2_" + i;
            String btnstr1 = "ans1_" + i;
            String btnstr0 = "ans0_" + i;
            int btnint2 = getResources().getIdentifier(btnstr2, "id", getPackageName());
            int btnint1 = getResources().getIdentifier(btnstr1, "id", getPackageName());
            int btnint0 = getResources().getIdentifier(btnstr0, "id", getPackageName());
            Button tempBtn2 = (Button) findViewById(btnint2);
            Button tempBtn1 = (Button) findViewById(btnint1);
            Button tempBtn0 = (Button) findViewById(btnint0);
            tempBtn2.setVisibility(View.GONE);
            tempBtn1.setVisibility(View.GONE);
            tempBtn0.setVisibility(View.GONE);
        }

        if (countQuestion == 1) btnPrevious.setEnabled(false);
        else if (countQuestion == 20) btnNext.setEnabled(false);
        else {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
        }

        String currentQuestion = questions.get(countQuestion - 1);
        String currentAnswer = answers.get(countQuestion - 1);
        int countCharall = currentAnswer.length();

        int img = getResources().getIdentifier(currentQuestion, "drawable", getPackageName());
        questionImg.setImageResource(img);

        String currentAnswerUpper = currentAnswer.toUpperCase();
        String[] array = currentAnswerUpper.split("");

        spaces = new ArrayList<Integer>();

        for (int i = 1; i < array.length; i++) {
            if (array[i].equals(" ")) spaces.add(i - 1);
            else justAnswer.add(array[i]);
        }




        countChar = currentAnswer.length() - spaces.size(); // countChar broj slova !!!
        spacesInt = spaces.size();

        dybtns=new Button[justAnswer.size()];
        count_word_1=0;
        count_word_2=0;
        count_word_3=0;


        int tempSpacesInt=spacesInt;

        for (int i = 1; i < array.length; i++) {
            switch (spacesInt) {
                case 0:
                    count_word_1=countCharall;
                    break;
                case 1:
                    if (tempSpacesInt == 1) {
                        if (!array[i].equals(" ")) count_word_1++;
                        else tempSpacesInt--;
                    }
                    if (tempSpacesInt == 0) {
                        if (!array[i].equals(" ")) count_word_2++;
                    }
                    break;
                case 2:
                    if (tempSpacesInt == 2) {
                        if (!array[i].equals(" ")) count_word_1++;
                        else tempSpacesInt--;
                    }
                    if (tempSpacesInt == 1) {
                        if (!array[i].equals(" ")) count_word_2++;
                        else
                        if (count_word_2!=0) tempSpacesInt--;
                        else continue;
                    }
                    if (tempSpacesInt == 0) {
                        if (!array[i].equals(" ")) count_word_3++;
                    }
                    break;
                default:break;

            }

        }

        Log.v("spaces", "ukupno karaktera = "+String.valueOf(countCharall));
        Log.v("spaces", "broj raymaka = "+String.valueOf(spaces.size()));
        Log.v("spaces", "mjesta na kojima su raymaci = "+String.valueOf(spaces));

        Log.v("spaces", "rijec 1 = "+String.valueOf(count_word_1));
        Log.v("spaces", "rijec 2 = "+String.valueOf(count_word_2));
        Log.v("spaces", "rijec 3 = "+String.valueOf(count_word_3));


        if (spacesInt==0) {
            for (int i = 0; i < numberofbuttons; i++) {
                if (i < count_word_1) {
                    String btnstr = "ans2_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i] = tempBtn;
                }
            }
        }

        if (spacesInt==1) {
            for (int i = 0; i < numberofbuttons; i++) {
                if (i<count_word_1) {
                    String btnstr = "ans2_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i]=tempBtn;

                }
                if (i<count_word_2) {
                    String btnstr = "ans1_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i+count_word_1]=tempBtn;

                }
            }
        }

        if (spacesInt==2) {
            for (int i = 0; i < numberofbuttons; i++) {
                if (i<count_word_1) {
                    String btnstr = "ans2_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i]=tempBtn;

                }

                if (i<count_word_2) {
                    String btnstr = "ans1_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i+count_word_1]=tempBtn;

                }

                if (i<count_word_3) {
                    String btnstr = "ans0_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i+count_word_1+count_word_2]=tempBtn;
                }
            }
        }


        allletters = new String[20];


        for (int i = 0; i < justAnswer.size(); i++) {
            allletters[i] = justAnswer.get(i);
        }

        for (int i = justAnswer.size(); i < allletters.length; i++) {
            Random rand = new Random();
            int n = rand.nextInt(26);
            allletters[i] = letters[n].toUpperCase();
        }

        Collections.shuffle(Arrays.asList(allletters));

        if (scores[countQuestion - 1].equals(1)) {
            for (int i = 0; i < allletters.length; i++) {
                btn[i].setVisibility(View.GONE);
            }
            for (int i = 0; i < justAnswer.size(); i++) {
                String answer = justAnswer.get(i);
                dybtns[i].setText(answer);

            }

            hint1.setEnabled(false);
            hint1.setBackgroundResource(R.drawable.infoicon_disabled);
            hint2.setEnabled(false);
            hint2.setBackgroundResource(R.drawable.halficon_disabled);
            hint3.setEnabled(false);
            hint3.setBackgroundResource(R.drawable.correcticon_disabled);
            hint4.setEnabled(false);
            hint4.setBackgroundResource(R.drawable.prvoslovo_disabled);

        } else {

            for (int i = 0; i < allletters.length; i++) {
                btn[i].setVisibility(View.VISIBLE);
                btn[i].setEnabled(true);
                btn[i].setText(allletters[i]);
            }
        }
    }




    public void checkAndScore(View view) {

        int checkfull = 0;


        for (int i = 0; i < dybtns.length; i++) {
            if (dybtns[i].getText() == "_") checkfull++;
        }

        if (checkfull <= dybtns.length && checkfull != 0) {
            int id = view.getId();
            bntid = (Button) findViewById(id);
            String letter = (String) bntid.getText();
            bntid.setText("");
            bntid.setEnabled(false);
            bntid.getBackground().setAlpha(0);

            int check = 0;

            for (int i = 0; i < dybtns.length; i++) {
                if (dybtns[i].getText() == "_") {
                    dybtns[i].setText(letter);
                    break;
                }
            }

            for (int i = 0; i < dybtns.length; i++) {
                if (dybtns[i].getText() == "_") {
                    check++;
                }
            }

            if (check == 0) {

                for (int i = 0; i < justAnswer.size(); i++) {
                    if (dybtns[i].getText().equals(justAnswer.get(i))) {
                        answersCorrect.add(i, 1);  ///////////////
                    } else {
                        answersCorrect.add(i, 0);  //////////////
                    }
                }

                checkAnswer();

            }
        }

    }

    private void checkAnswer() {

        int wrongLett = Collections.frequency(answersCorrect, 0);
        if (wrongLett == 0) {
            for (int i = 0; i < btn.length; i++) {
                btn[i].setEnabled(false);
                btn[i].setVisibility(View.GONE);
            }

            hint1.setEnabled(false);
            hint1.setBackgroundResource(R.drawable.infoicon_disabled);
            hint2.setEnabled(false);
            hint2.setBackgroundResource(R.drawable.halficon_disabled);
            hint3.setEnabled(false);
            hint3.setBackgroundResource(R.drawable.correcticon_disabled);
            hint4.setEnabled(false);
            hint4.setBackgroundResource(R.drawable.prvoslovo_disabled);

            popupdouble();
            saveVariables();
        }

    }

    private void popupdouble() {


        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_doublescore, linearLayout, false);
        popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        Button btnAdd_yes = container.findViewById(R.id.ad_buttonPL_Yes);
        Button btnAdd_no = container.findViewById(R.id.ad_buttonPL_No);
        TextView hintext = container.findViewById(R.id.hintNumber);

        hintext.setText("+"+Global.hintCorrect+" Hint");

        btnAdd_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAd.isLoaded()) {
                    numberOfVideo = 4;
                    mAd.show();
                    popupWindow.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Video is loading...\n" +"Please try again in couple of seconds.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdd_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hintsLogo = hintsLogo + Global.hintCorrect;
                mEditor.putInt("hints_logo", hintsLogo);
                mEditor.commit();
                remainingHints();

                popupWindow.dismiss();
            }
        });
    }

    private void saveVariables() {
        int saveint = countQuestion - 1;
        scores[saveint] = 1;

        switch (btnIDint) {
            case (R.id.logosLayout1):
                mEditor.putInt("score_logos_" + saveint, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout2):
                int saveint2 = saveint + 20;
                mEditor.putInt("score_logos_" + saveint2, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout3):
                int saveint3 = saveint + 20 * 2;
                mEditor.putInt("score_logos_" + saveint3, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout4):
                int saveint4 = saveint + 20 * 3;
                mEditor.putInt("score_logos_" + saveint4, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout5):
                int saveint5 = saveint + 20 * 4;
                mEditor.putInt("score_logos_" + saveint5, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout6):
                int saveint6 = saveint + 20 * 5;
                mEditor.putInt("score_logos_" + saveint6, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout7):
                int saveint7 = saveint + 20 * 6;
                mEditor.putInt("score_logos_" + saveint7, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout8):
                int saveint8 = saveint + 20 * 7;
                mEditor.putInt("score_logos_" + saveint8, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout9):
                int saveint9 = saveint + 20 * 8;
                mEditor.putInt("score_logos_" + saveint9, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout10):
                int saveint10 = saveint + 20 * 9;
                mEditor.putInt("score_logos_" + saveint10, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout11):
                int saveint11 = saveint + 20 * 10;
                mEditor.putInt("score_logos_" + saveint11, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout12):
                int saveint12 = saveint + 20 * 11;
                mEditor.putInt("score_logos_" + saveint12, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout13):
                int saveint13 = saveint + 20 * 12;
                mEditor.putInt("score_logos_" + saveint13, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout14):
                int saveint14 = saveint + 20 * 13;
                mEditor.putInt("score_logos_" + saveint14, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout15):
                int saveint15 = saveint + 20 * 14;
                mEditor.putInt("score_logos_" + saveint15, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout16):
                int saveint16 = saveint + 20 * 15;
                mEditor.putInt("score_logos_" + saveint16, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout17):
                int saveint17 = saveint + 20 * 16;
                mEditor.putInt("score_logos_" + saveint17, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout18):
                int saveint18 = saveint + 20 * 17;
                mEditor.putInt("score_logos_" + saveint18, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout19):
                int saveint19 = saveint + 20 * 18;
                mEditor.putInt("score_logos_" + saveint19, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout20):
                int saveint20 = saveint + 20 * 19;
                mEditor.putInt("score_logos_" + saveint20, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout21):
                int saveint21 = saveint + 20 * 20;
                mEditor.putInt("score_logos_" + saveint21, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout22):
                int saveint22 = saveint + 20 * 21;
                mEditor.putInt("score_logos_" + saveint22, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout23):
                int saveint23 = saveint + 20 * 22;
                mEditor.putInt("score_logos_" + saveint23, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout24):
                int saveint24 = saveint + 20 * 23;
                mEditor.putInt("score_logos_" + saveint24, 1);
                mEditor.commit();
                break;
            case (R.id.logosLayout25):
                int saveint25 = saveint + 20 * 24;
                mEditor.putInt("score_logos_" + saveint25, 1);
                mEditor.commit();
                break;
            default:
                break;
        }

        scoreLive();
    }

    View.OnClickListener removeletters(final Button button) {

        return new View.OnClickListener() {
            public void onClick(View v) {

                answersCorrect = new ArrayList<Integer>();
                Button dybutton = (Button) findViewById(v.getId());
                String lastletter = (String) dybutton.getText();

                if (lastletter != "_") {
                    dybutton.setText("_");

                    for (int i = 0; i < btn.length; i++) {
                        if (btn[i].getText() == "") {
                            btn[i].setText(lastletter);
                            btn[i].setEnabled(true);
                            btn[i].getBackground().setAlpha(255);
                            break;
                        }
                    }

                }
            }
        };
    }

    public void goToPN(View view) {
        if (view.getId() == R.id.previous) countQuestion--;
        else if (view.getId() == R.id.next) countQuestion++;

        answersCorrect.clear();
        justAnswer.clear();

        for (int i = 0; i < btn.length; i++) btn[i].getBackground().setAlpha(255);

        hint1.setEnabled(true);
        hint2.setEnabled(true);
        hint3.setEnabled(true);
        hint4.setEnabled(true);

        hint1.setBackgroundResource(R.drawable.infoicon);
        hint2.setBackgroundResource(R.drawable.halficon);
        hint3.setBackgroundResource(R.drawable.correcticon);
        hint4.setBackgroundResource(R.drawable.prvoslovo);

        scoreLive();
        showquestions();

    }

    public void backToLevelsLogos(View view) {

        stoptimers_savestop();

        Intent intent = new Intent(this, Logos.class);
        startActivity(intent);

        if (mAdinter.isLoaded()) {
            mAdinter.show();
            Log.d("TAG", "loaded");
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();

    }

    @Override
    public void onBackPressed() {

        stoptimers_savestop();
        Intent intent = new Intent(this, Logos.class);
        startActivity(intent);

        if (mAdinter.isLoaded()) {
            mAdinter.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();
    }

    public void hintsLogos(View view) {
        switch (view.getId()) {
            case (R.id.logosHint1):
                if (hintsLogo < Global.hint1points) {
                    setOnClickpopupAds();
                } else {
                    hintsLogo = hintsLogo - Global.hint1points;
                    mEditor.putInt("hints_logo", hintsLogo);
                    mEditor.commit();

                    AlertDialog.Builder builder = new AlertDialog.Builder(LogosPitanja.this);
                    builder.setTitle("Information");
                    builder.setMessage(hints.get(countQuestion - 1));
                    builder.setPositiveButton("Close", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.show();
                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    builder.create();

                    hint1.setEnabled(false);
                    hint1.setBackgroundResource(R.drawable.infoicon_disabled);
                }

                break;

            case (R.id.logosHint2):
                if (hintsLogo < Global.hint2points) {
                    setOnClickpopupAds();
                } else {
                    hintsLogo = hintsLogo - Global.hint2points;
                    mEditor.putInt("hints_logo", hintsLogo);
                    mEditor.commit();

                    int answersize = justAnswer.size();
                    int pola;
                    if (answersize % 2 == 0) {
                        pola = answersize / 2;
                    } else pola = answersize / 2 + 1;

                    for (int i = 0; i < pola; i++) {
                        String answer = justAnswer.get(i);
                        dybtns[i].setText(answer);

                    }
                    hint2.setEnabled(false);
                    hint2.setBackgroundResource(R.drawable.halficon_disabled);
                }

                break;

            case (R.id.logosHint3):
                if (hintsLogo < Global.hint3points) {
                    setOnClickpopupAds();
                } else {
                    hintsLogo = hintsLogo - Global.hint3points;
                    mEditor.putInt("hints_logo", hintsLogo);
                    mEditor.commit();

                    int answersize = justAnswer.size();

                    for (int i = 0; i < answersize; i++) {
                        String answer = justAnswer.get(i);
                        dybtns[i].setText(answer);
                    }

                    checkAnswer();
                    hint1.setEnabled(false);
                    hint1.setBackgroundResource(R.drawable.infoicon_disabled);
                    hint2.setEnabled(false);
                    hint2.setBackgroundResource(R.drawable.halficon_disabled);
                    hint3.setEnabled(false);
                    hint3.setBackgroundResource(R.drawable.correcticon_disabled);
                    hint4.setEnabled(false);
                    hint4.setBackgroundResource(R.drawable.prvoslovo_disabled);
                }

                break;

            case (R.id.logosHint4):
                if (hintsLogo < Global.hint4points) {
                    setOnClickpopupAds();
                } else {
                    hintsLogo = hintsLogo - Global.hint4points;
                    mEditor.putInt("hints_logo", hintsLogo);
                    mEditor.commit();

                    String answer = justAnswer.get(0);
                    dybtns[0].setText(answer);

                    hint4.setEnabled(false);
                    hint4.setBackgroundResource(R.drawable.prvoslovo_disabled);
                }

                break;

            default:
                break;
        }
        remainingHints();
    }

    private void scoreLive() {
        List<Integer> tempscore = new ArrayList<Integer>();
        for (int i = 0; i < scores.length; i++) {
            tempscore.add(scores[i]);
        }
        int temp = Collections.frequency(tempscore, 1);
        scorelive.setText(temp + "/20");

    }

    private void remainingHints() {
        hintsLogo = sharedPref.getInt("hints_logo", Global.hintsStart);
        hintremain.setText(String.valueOf(hintsLogo));
    }


    private Runnable timer1 = new Runnable() {

        @Override
        public void run() {

            if(Global.timerVideo1*1000>0) {

                advideo1.setEnabled(false);

                Global.timerTracker1 = true;
                mEditor.putBoolean("time_tracker_1", true);
                mEditor.commit();

                Global.timerVideo1--;

                mEditor.putLong("timer_video_1_left", Global.timerVideo1);
                mEditor.commit();

                long hourT1 = Global.timerVideo1 / 3600;
                long minT1 = Global.timerVideo1 / 60;
                long secT1 = Global.timerVideo1 % 60;

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

                Global.timerTracker1 = false;
                mEditor.putBoolean("time_tracker_1", false);
                mEditor.commit();

                Global.timerVideo1 = 0;
                mEditor.putLong("timer_video_1_left", 0);
                mEditor.commit();
                handler1.removeCallbacks(timer1);
            }

        }
    };


    private Runnable timer2 = new Runnable() {

        @Override
        public void run() {

            if (Global.timerVideo2 * 1000 > 0) {
                advideo2.setEnabled(false);

                Global.timerTracker2 = true;
                mEditor.putBoolean("time_tracker_2", Global.timerTracker2);
                mEditor.commit();

                Global.timerVideo2--;

                mEditor.putLong("timer_video_2_left", Global.timerVideo2);
                mEditor.commit();

                long hourT2 = Global.timerVideo2 / 3600;
                long minT2 = Global.timerVideo2 / 60;
                long secT2 = Global.timerVideo2 % 60;

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

                Global.timerTracker2 = false;
                mEditor.putBoolean("time_tracker_2", Global.timerTracker2);
                mEditor.commit();

                Global.timerVideo2 = 0;
                mEditor.putLong("timer_video_2_left", 0);
                mEditor.commit();

                handler2.removeCallbacks(timer2);
            }

        }
    };


    private Runnable timer3 = new Runnable() {

        @Override
        public void run() {

            if (Global.timerVideo3 * 1000 > 0) {
                advideo3.setEnabled(false);

                Global.timerTracker3 = true;
                mEditor.putBoolean("time_tracker_3", Global.timerTracker3);
                mEditor.commit();

                Global.timerVideo3--;

                mEditor.putLong("timer_video_3_left", Global.timerVideo3);
                mEditor.commit();

                long hourT3=Global.timerVideo3/3600;
                long minT3=Global.timerVideo3/60;
                long secT3=Global.timerVideo3%60;

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

                Global.timerTracker3 = false;
                mEditor.putBoolean("time_tracker_3", Global.timerTracker3);
                mEditor.commit();

                Global.timerVideo3 = 0;
                mEditor.putLong("timer_video_3_left", 0);
                mEditor.commit();

                handler3.removeCallbacks(timer3);
            }
        }

    };


    public void setOnClickpopupAds() {
        popupWindow1 = new PopupWindow(container1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow1.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        long starttime=System.currentTimeMillis();
        mEditor.putLong("time_start_1", starttime);
        mEditor.commit();

        long stoptime=sharedPref.getLong("time_stop_1",0);

        long timeleft1=sharedPref.getLong("timer_video_1_left",Global.timerVideo1);
        long timeleft2=sharedPref.getLong("timer_video_2_left",Global.timerVideo2);
        long timeleft3=sharedPref.getLong("timer_video_3_left",Global.timerVideo3);

        long raz=(starttime-stoptime)/1000;

        Boolean ttracker1=sharedPref.getBoolean("time_tracker_1",false);
        Boolean ttracker2=sharedPref.getBoolean("time_tracker_2",false);
        Boolean ttracker3=sharedPref.getBoolean("time_tracker_3",false);


        if (timeleft1!=0){
            Global.timerVideo1 = timeleft1 - raz;
            if ( ttracker1 && !Global.timerTracker1) {
                handler1.postDelayed(timer1,1000);

            }
            else if (ttracker1 && Global.timerTracker1) {
                handler1.removeCallbacksAndMessages(null);
                handler1.postDelayed(timer1, 1000);
            }
        }


        if (timeleft2!=0){
            Global.timerVideo2 = timeleft2 - raz;
            if (ttracker2 && !Global.timerTracker2) {
                handler2.postDelayed(timer2,1000);
            }
            else if (ttracker2 && Global.timerTracker2){
                handler2.removeCallbacksAndMessages(null);
                handler2.postDelayed(timer2, 1000);
            }
        }

        if (timeleft3!=0){
            Global.timerVideo3 = timeleft3 - raz;
            if (ttracker3 && !Global.timerTracker3) {
                handler3.postDelayed(timer3,1000);
            }
            else if (ttracker3 && Global.timerTracker3){
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
                    Global.timerVideo1=Global.adTimer1;
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
                    Global.timerVideo2=Global.adTimer2;
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
                    Global.timerVideo3=Global.adTimer3;
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
        mEditor.putLong("time_stop_1", stoptime);
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
        if (numberOfVideo==1) hintsLogo = hintsLogo + Global.hintsAfterVideo1;
        else if (numberOfVideo==2) hintsLogo = hintsLogo + Global.hintsAfterVideo2;
        else if (numberOfVideo==3) hintsLogo = hintsLogo + Global.hintsAfterVideo3;
        else if (numberOfVideo==4) hintsLogo = hintsLogo + Global.hintCorrect*2;
        else Log.v("VideoAd","Not rewarded video");

        mEditor.putInt("hints_logo", hintsLogo);
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

