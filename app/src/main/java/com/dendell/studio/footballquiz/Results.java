package com.dendell.studio.footballquiz;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {

    private List<TextView> questions=new ArrayList<>();
    private List<TextView> answers=new ArrayList<>();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.results);

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-6229589484617184~3409426273");

        mAdView = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        for (int i=1;i<21;i++){
            String textquestion="question"+(i);
            String textanswer="answer"+(i);

            int textquestionid = getResources().getIdentifier(textquestion, "id", getPackageName());
            int textanswerid = getResources().getIdentifier(textanswer, "id", getPackageName());

            questions.add((TextView) findViewById(textquestionid));
            answers.add((TextView) findViewById(textanswerid));
        }

        for (int i=0;i<20;i++){
            questions.get(i).setText(Global.questions_for_results.get(i));
            answers.get(i).setText(Global.answer_for_results.get(i));
            if (Global.true_false_for_results.get(i)){
                answers.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            }
            else
            {
                answers.get(i).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
            }
        }



    }
}