package com.dendell.studio.footballquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
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
import android.widget.ImageView;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PlayersPitanja extends AppCompatActivity implements RewardedVideoAdListener{

    private FirebaseAnalytics mFirebaseAnalytics;
    private String token = "";
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

    private LinearLayout layoutAnswer0,layoutAnswer1,layoutAnswer2;
    private ImageView questionImg;
    private TextView scorelive,hintremain;
    private Button[] btn = new Button[20];
    private Button[] dybtns;
    private Button btnPrevious, btnNext;
    private ImageButton  hint1,hint2, hint3,hint4,adsButton;
    private Button bntid;

    private Integer[] scores = new Integer[20];
    private List <Integer> spaces;
    private int spacesInt;

    private int hintsplayer;

    private String[] allletters;

    private PopupWindow popupWindow,popupWindow1;
    private LayoutInflater layoutInflater;

    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;

    private RewardedVideoAd mAd;
    private InterstitialAd mAdinter;

    private  ViewGroup container1;
    private Button no, advideo1,advideo2,advideo3,apprate;
    private int numberOfVideo;

    private Handler handler1,handler2,handler3;

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

        setContentView(R.layout.playerspitanja);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Players Pitanja");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Players Pitanja");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "start");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        constraintLayout = (ConstraintLayout) findViewById(R.id.relativeP);

        Intent btnId = getIntent();
        btnIDint = btnId.getIntExtra("playersLevel", 0);

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-6229589484617184~3409426273");

        mAdView = (AdView) findViewById(R.id.bannerP);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdinter = new InterstitialAd(this);
        mAdinter.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //testni Ad
        mAdinter.loadAd(new AdRequest.Builder().build());

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        layoutAnswer0 = (LinearLayout) findViewById(R.id.dynamicAnsPlayer0);
        layoutAnswer1 = (LinearLayout) findViewById(R.id.dynamicAnsPlayer1);
        layoutAnswer2 = (LinearLayout) findViewById(R.id.dynamicAnsPlayer2);
        hint1 = (ImageButton) findViewById(R.id.playersHint1);
        hint2 = (ImageButton) findViewById(R.id.playersHint2);
        hint3 = (ImageButton) findViewById(R.id.playersHint3);
        hint4 = (ImageButton) findViewById(R.id.playersHint4);

        questionImg = (ImageView) findViewById(R.id.imageplayer);

        btnPrevious = (Button) findViewById(R.id.previousP);
        btnNext = (Button) findViewById(R.id.nextP);
        scorelive = (TextView) findViewById(R.id.scoreLivePlayers);
        hintremain=(TextView) findViewById(R.id.hintsPlayerAll);

        adsButton = (ImageButton) findViewById(R.id.adsButtonP);

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

    private void importScoresAndHints (){

        remainingHints();

        switch (btnIDint) {
            case (R.id.playersLayout1):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + i, 0);
                importQuestionsAnswers("playersL1.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 1"));
                break;
            case (R.id.playersLayout2):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20), 0);
                importQuestionsAnswers("playersL2.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 2"));
                break;
            case (R.id.playersLayout3):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 2), 0);
                importQuestionsAnswers("playersL3.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 3"));
                break;
            case (R.id.playersLayout4):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 3), 0);
                importQuestionsAnswers("playersL4.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 4"));
                break;
            case (R.id.playersLayout5):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 4), 0);
                importQuestionsAnswers("playersL5.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 5"));
                break;
            case (R.id.playersLayout6):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 5), 0);
                importQuestionsAnswers("playersL6.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 6"));
                break;
            case (R.id.playersLayout7):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 6), 0);
                importQuestionsAnswers("playersL7.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 7"));
                break;
            case (R.id.playersLayout8):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 7 * 2), 0);
                importQuestionsAnswers("playersL8.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 8"));
                break;
            case (R.id.playersLayout9):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 8), 0);
                importQuestionsAnswers("playersL9.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 9"));
                break;
            case (R.id.playersLayout10):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 9), 0);
                importQuestionsAnswers("playersL10.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 10"));
                break;
            case (R.id.playersLayout11):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 10), 0);
                importQuestionsAnswers("playersL11.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 11"));
                break;
            case (R.id.playersLayout12):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 11), 0);
                importQuestionsAnswers("playersL12.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 12"));
                break;
            case (R.id.playersLayout13):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 12), 0);
                importQuestionsAnswers("playersL13.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 13"));
                break;
            case (R.id.playersLayout14):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 13), 0);
                importQuestionsAnswers("playersL14.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 14"));
                break;
            case (R.id.playersLayout15):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 14), 0);
                importQuestionsAnswers("playersL15.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 15"));
                break;
            case (R.id.playersLayout16):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 15), 0);
                importQuestionsAnswers("playersL16.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 16"));
                break;
            case (R.id.playersLayout17):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 16), 0);
                importQuestionsAnswers("playersL17.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 17"));
                break;
            case (R.id.playersLayout18):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 17), 0);
                importQuestionsAnswers("playersL18.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 18"));
                break;
            case (R.id.playersLayout19):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 18), 0);
                importQuestionsAnswers("playersL19.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 19"));
                break;
            case (R.id.playersLayout20):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 19), 0);
                importQuestionsAnswers("playersL20.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 20"));
                break;
            case (R.id.playersLayout21):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 20), 0);
                importQuestionsAnswers("playersL21.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 21"));
                break;
            case (R.id.playersLayout22):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 21), 0);
                importQuestionsAnswers("playersL22.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 22"));
                break;
            case (R.id.playersLayout23):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 22), 0);
                importQuestionsAnswers("playersL23.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 23"));
                break;
            case (R.id.playersLayout24):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 23), 0);
                importQuestionsAnswers("playersL24.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 24"));
                break;
            case (R.id.playersLayout25):
                for (int i = 0; i < 20; i++)
                    scores[i] = sharedPref.getInt("score_players_" + (i + 20 * 24), 0);
                importQuestionsAnswers("playersL25.txt");
                Answers.getInstance().logLevelStart(new LevelStartEvent().putLevelName("Players Level 25"));
                break;
            default:
                Log.e("error", "Level does not exist!");
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
            String btnstr2 = "ans2_p_" + i;
            String btnstr1 = "ans1_p_" + i;
            String btnstr0 = "ans0_p_" + i;
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

        spaces= new ArrayList<Integer>();

        for (int i = 1; i < array.length; i++) {
            if (array[i].equals(" ")) spaces.add(i-1);
            else justAnswer.add(array[i]) ;
        }

        int countChar = currentAnswer.length()-spaces.size();
        spacesInt=spaces.size();

        count_word_1=0;
        count_word_2=0;
        count_word_3=0;

        dybtns=new Button[justAnswer.size()];

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
                if (i<count_word_1) {
                    String btnstr = "ans2_p_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i]=tempBtn;
                }
            }
        }

        if (spacesInt==1) {
            for (int i = 0; i < numberofbuttons; i++) {
                if (i<count_word_1) {
                    String btnstr = "ans2_p_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i]=tempBtn;

                }
                if (i<count_word_2) {
                    String btnstr = "ans1_p_" + i;
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
                    String btnstr = "ans2_p_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i]=tempBtn;

                }

                if (i<count_word_2) {
                    String btnstr = "ans1_p_" + i;
                    int btnint = getResources().getIdentifier(btnstr, "id", getPackageName());
                    Button tempBtn = (Button) findViewById(btnint);
                    tempBtn.setText("_");
                    tempBtn.setVisibility(View.VISIBLE);
                    tempBtn.setOnClickListener(removeletters(getBtn));
                    dybtns[i+count_word_1]=tempBtn;

                }

                if (i<count_word_3) {
                    String btnstr = "ans0_p_" + i;
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
            allletters[i] = justAnswer.get(i);      }

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

        if (checkfull <=dybtns.length && checkfull!=0) {
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
                        answersCorrect.add(i, 1);
                    } else {
                        answersCorrect.add(i, 0);
                    }
                }

                checkAnswer();

            }
        }
    }

    private void checkAnswer(){

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

        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_doublescore,linearLayout,false);
        popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true );
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER,0,0);

        Button btnAdd_yes = container.findViewById(R.id.ad_buttonPL_Yes);
        Button btnAdd_no = container.findViewById(R.id.ad_buttonPL_No);
        TextView hintext = container.findViewById(R.id.hintNumber);

        hintext.setText("+"+Global.hintCorrect+" Hint");

        btnAdd_yes.setOnClickListener(new View.OnClickListener() {
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

                hintsplayer = hintsplayer + Global.hintCorrect;
                mEditor.putInt("hints_player", hintsplayer);
                mEditor.commit();
                remainingHints();
                popupWindow.dismiss();
            }
        });
    }


    private void saveVariables (){
        int saveint = countQuestion - 1;
        scores[saveint] = 1;

        switch (btnIDint) {
            case (R.id.playersLayout1):
                mEditor.putInt("score_players_" + saveint, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout2):
                int saveint2 = saveint + 20;
                mEditor.putInt("score_players_" + saveint2, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout3):
                int saveint3 = saveint + 20 * 2;
                mEditor.putInt("score_players_" + saveint3, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout4):
                int saveint4 = saveint + 20 * 3;
                mEditor.putInt("score_players_" + saveint4, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout5):
                int saveint5 = saveint + 20 * 4;
                mEditor.putInt("score_players_" + saveint5, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout6):
                int saveint6 = saveint + 20 * 5;
                mEditor.putInt("score_players_" + saveint6, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout7):
                int saveint7 = saveint + 20 * 6;
                mEditor.putInt("score_players_" + saveint7, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout8):
                int saveint8 = saveint + 20 * 7;
                mEditor.putInt("score_players_" + saveint8, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout9):
                int saveint9 = saveint + 20 * 8;
                mEditor.putInt("score_players_" + saveint9, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout10):
                int saveint10 = saveint + 20 * 9;
                mEditor.putInt("score_players_" + saveint10, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout11):
                int saveint11 = saveint + 20 * 10;
                mEditor.putInt("score_players_" + saveint11, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout12):
                int saveint12 = saveint + 20 * 11;
                mEditor.putInt("score_players_" + saveint12, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout13):
                int saveint13 = saveint + 20 * 12;
                mEditor.putInt("score_players_" + saveint13, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout14):
                int saveint14 = saveint + 20 * 13;
                mEditor.putInt("score_players_" + saveint14, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout15):
                int saveint15 = saveint + 20 * 14;
                mEditor.putInt("score_players_" + saveint15, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout16):
                int saveint16 = saveint + 20 * 15;
                mEditor.putInt("score_players_" + saveint16, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout17):
                int saveint17 = saveint + 20 * 16;
                mEditor.putInt("score_players_" + saveint17, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout18):
                int saveint18 = saveint + 20 * 17;
                mEditor.putInt("score_players_" + saveint18, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout19):
                int saveint19 = saveint + 20 * 18;
                mEditor.putInt("score_players_" + saveint19, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout20):
                int saveint20 = saveint + 20 * 19;
                mEditor.putInt("score_players_" + saveint20, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout21):
                int saveint21 = saveint + 20 * 20;
                mEditor.putInt("score_players_" + saveint21, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout22):
                int saveint22 = saveint + 20 * 21;
                mEditor.putInt("score_players_" + saveint22, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout23):
                int saveint23 = saveint + 20 * 22;
                mEditor.putInt("score_players_" + saveint23, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout24):
                int saveint24 = saveint + 20 * 23;
                mEditor.putInt("score_players_" + saveint24, 1);
                mEditor.commit();
                break;
            case (R.id.playersLayout25):
                int saveint25 = saveint + 20 * 24;
                mEditor.putInt("score_players_" + saveint25, 1);
                mEditor.commit();
                break;
            default:
                Log.e("error", "Level does not exist!");
                break;
        }

        scoreLive();
    }

    View.OnClickListener removeletters(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                answersCorrect=new ArrayList<Integer>();
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

    public void goToPN (View view){
        if (view.getId()==R.id.previousP)countQuestion--;
        else if (view.getId()==R.id.nextP) countQuestion++;


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

    public void backToLevelsplayers (View view){

        stoptimers_savestop();

        Intent intent = new Intent(this, Players.class);
        startActivity(intent);

        if (mAdinter.isLoaded()) {
            mAdinter.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();

    }

    @Override
    public void onBackPressed() {

        stoptimers_savestop();
        Intent intent = new Intent(this, Players.class);
        startActivity(intent);

        if (mAdinter.isLoaded()) {
            mAdinter.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();
    }

    public void hintsplayers(View view){
        switch (view.getId()){
            case (R.id.playersHint1):
                if (hintsplayer<Global.hint1points){
                    setOnClickpopupAds();
                }
                else {
                    hintsplayer=hintsplayer-Global.hint1points;
                    mEditor.putInt("hints_player", hintsplayer);
                    mEditor.commit();

                    AlertDialog.Builder builder=new AlertDialog.Builder(PlayersPitanja.this);
                    builder.setTitle("Information");
                    builder.setMessage(hints.get(countQuestion-1));
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

            case (R.id.playersHint2):
                if (hintsplayer<Global.hint2points){
                    setOnClickpopupAds();
                }
                else {
                    hintsplayer = hintsplayer - Global.hint2points;
                    mEditor.putInt("hints_player", hintsplayer);
                    mEditor.commit();

                    int answersize=justAnswer.size();
                    int pola;
                    if (answersize%2==0){
                        pola=answersize/2;
                    }
                    else pola=answersize/2+1;

                    for (int i = 0; i < pola; i++) {
                        String answer = justAnswer.get(i);
                        dybtns[i].setText(answer);
                    }
                    hint2.setEnabled(false);
                    hint2.setBackgroundResource(R.drawable.halficon_disabled);
                }


                break;

            case (R.id.playersHint3):
                if (hintsplayer<Global.hint3points){
                    setOnClickpopupAds();
                }
                else {
                    hintsplayer = hintsplayer - Global.hint3points;
                    mEditor.putInt("hints_player", hintsplayer);
                    mEditor.commit();

                    int answersize=justAnswer.size();

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

            case (R.id.playersHint4):
                if (hintsplayer<Global.hint4points){
                    setOnClickpopupAds();
                }
                else {
                    hintsplayer = hintsplayer - Global.hint4points;
                    mEditor.putInt("hints_player", hintsplayer);
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

    private void scoreLive(){
        List <Integer> tempscore=new ArrayList<Integer>();
        for (int i=0;i<scores.length;i++){
            tempscore.add(scores[i]);
        }
        int temp = Collections.frequency(tempscore, 1);
        scorelive.setText(temp+"/20");

    }

    private void remainingHints(){
        hintsplayer=sharedPref.getInt("hints_player",Global.hintsStart);
        hintremain.setText(String.valueOf(hintsplayer));
    }


    private Runnable timer1 = new Runnable() {

        @Override
        public void run() {

            if(Global.ptimerVideo1*1000>0) {

                advideo1.setEnabled(false);

                Global.ptimerTracker1 = true;
                mEditor.putBoolean("ptime_tracker_1", true);
                mEditor.commit();

                Global.ptimerVideo1--;

                mEditor.putLong("ptimer_video_1_left", Global.ptimerVideo1);
                mEditor.commit();

                long hourT1 = Global.ptimerVideo1 / 3600;
                long minT1 = Global.ptimerVideo1 / 60;
                long secT1 = Global.ptimerVideo1 % 60;

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

                Global.ptimerTracker1 = false;
                mEditor.putBoolean("ptime_tracker_1", false);
                mEditor.commit();

                Global.ptimerVideo1 = 0;
                mEditor.putLong("ptimer_video_1_left", 0);
                mEditor.commit();
                handler1.removeCallbacks(timer1);
            }

        }
    };


    private Runnable timer2 = new Runnable() {

        @Override
        public void run() {

            if (Global.ptimerVideo2 * 1000 > 0) {
                advideo2.setEnabled(false);

                Global.ptimerTracker2 = true;
                mEditor.putBoolean("ptime_tracker_2", Global.ptimerTracker2);
                mEditor.commit();

                Global.ptimerVideo2--;

                mEditor.putLong("ptimer_video_2_left", Global.ptimerVideo2);
                mEditor.commit();

                long hourT2 = Global.ptimerVideo2 / 3600;
                long minT2 = Global.ptimerVideo2 / 60;
                long secT2 = Global.ptimerVideo2 % 60;

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

                Global.ptimerTracker2 = false;
                mEditor.putBoolean("ptime_tracker_2", Global.ptimerTracker2);
                mEditor.commit();

                Global.ptimerVideo2 = 0;
                mEditor.putLong("ptimer_video_2_left", 0);
                mEditor.commit();

                handler2.removeCallbacks(timer2);
            }

        }
    };


    private Runnable timer3 = new Runnable() {

        @Override
        public void run() {

            if (Global.ptimerVideo3 * 1000 > 0) {
                advideo3.setEnabled(false);

                Global.ptimerTracker3 = true;
                mEditor.putBoolean("ptime_tracker_3", Global.ptimerTracker3);
                mEditor.commit();

                Global.ptimerVideo3--;

                mEditor.putLong("ptimer_video_3_left", Global.ptimerVideo3);
                mEditor.commit();

                long hourT3=Global.ptimerVideo3/3600;
                long minT3=Global.ptimerVideo3/60;
                long secT3=Global.ptimerVideo3%60;

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

                Global.ptimerTracker3 = false;
                mEditor.putBoolean("ptime_tracker_3", Global.ptimerTracker3);
                mEditor.commit();

                Global.ptimerVideo3 = 0;
                mEditor.putLong("ptimer_video_3_left", 0);
                mEditor.commit();

                handler3.removeCallbacks(timer3);
            }
        }

    };


    public void setOnClickpopupAds() {
        popupWindow1 = new PopupWindow(container1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow1.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        long starttime=System.currentTimeMillis();
        mEditor.putLong("ptime_start_1", starttime);
        mEditor.commit();

        long stoptime=sharedPref.getLong("ptime_stop_1",0);

        long timeleft1=sharedPref.getLong("ptimer_video_1_left",Global.ptimerVideo1);
        long timeleft2=sharedPref.getLong("ptimer_video_2_left",Global.ptimerVideo2);
        long timeleft3=sharedPref.getLong("ptimer_video_3_left",Global.ptimerVideo3);

        long raz=(starttime-stoptime)/1000;

        Boolean ttracker1=sharedPref.getBoolean("ptime_tracker_1",false);
        Boolean ttracker2=sharedPref.getBoolean("ptime_tracker_2",false);
        Boolean ttracker3=sharedPref.getBoolean("ptime_tracker_3",false);

        if (timeleft1!=0){
            Global.ptimerVideo1 = timeleft1 - raz;
            if ( ttracker1 && !Global.ptimerTracker1) {
                handler1.postDelayed(timer1,1000);

            }
            else if (ttracker1 && Global.ptimerTracker1) {
                handler1.removeCallbacksAndMessages(null);
                handler1.postDelayed(timer1, 1000);
            }
        }


        if (timeleft2!=0){
            Global.ptimerVideo2 = timeleft2 - raz;
            if (ttracker2 && !Global.ptimerTracker2) {
                handler2.postDelayed(timer2,1000);
            }
            else if (ttracker2 && Global.ptimerTracker2){
                handler2.removeCallbacksAndMessages(null);
                handler2.postDelayed(timer2, 1000);
            }
        }

        if (timeleft3!=0){
            Global.ptimerVideo3 = timeleft3 - raz;
            if (ttracker3 && !Global.ptimerTracker3) {
                handler3.postDelayed(timer3,1000);
            }
            else if (ttracker3 && Global.ptimerTracker3){
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
                    Global.ptimerVideo1=Global.adTimer1;
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
                    Global.ptimerVideo2=Global.adTimer2;
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
                    Global.ptimerVideo3=Global.adTimer3;
                    handler3.postDelayed(timer3,1000);
                    popupWindow1.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Video is loading...\n" +"Please try again in couple of seconds.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void stoptimers_savestop() {
        long stoptime=System.currentTimeMillis();
        mEditor.putLong("ptime_stop_1", stoptime);
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
        if (numberOfVideo==1) hintsplayer = hintsplayer + Global.hintsAfterVideo1;
        else if (numberOfVideo==2) hintsplayer = hintsplayer + Global.hintsAfterVideo2;
        else if (numberOfVideo==3) hintsplayer = hintsplayer + Global.hintsAfterVideo3;
        else if (numberOfVideo==4) hintsplayer = hintsplayer + Global.hintCorrect*2;

        mEditor.putInt("hints_player", hintsplayer);
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