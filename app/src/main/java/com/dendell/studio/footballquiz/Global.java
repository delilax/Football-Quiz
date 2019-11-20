package com.dendell.studio.footballquiz;

import java.util.ArrayList;
import java.util.List;


public class Global {

    public static final int hintsStart=50;
    public static final int hint1points=1;
    public static final int hint2points=3;
    public static final int hint3points=5;
    public static final int hint4points=2;

    public static final int hint1pointsq=1;
    public static final int hint2pointsq=3;
    public static final int hint3pointsq=5;

    public static final int hintCorrect=1;
    public static final int hintCorrectQuestion=1;
    public static final int hintsAfterVideo1=5;
    public static final int hintsAfterVideo2=10;
    public static final int hintsAfterVideo3=15;


    public static final long adTimer1=120;
    public static final int adTimer2=300;
    public static final int adTimer3=600;

    public static long timerVideo1=0;
    public static long timerVideo2=0;
    public static long timerVideo3=0;

    public static boolean timerTracker1=false;
    public static boolean timerTracker2=false;
    public static boolean timerTracker3=false;

    public static long ptimerVideo1=0;
    public static long ptimerVideo2=0;
    public static long ptimerVideo3=0;

    public static boolean ptimerTracker1=false;
    public static boolean ptimerTracker2=false;
    public static boolean ptimerTracker3=false;

    public static long qtimerVideo1=0;
    public static long qtimerVideo2=0;
    public static long qtimerVideo3=0;

    public static boolean qtimerTracker1=false;
    public static boolean qtimerTracker2=false;
    public static boolean qtimerTracker3=false;

    public static List<String> questions_for_results=new ArrayList<>();
    public static List<String> answer_for_results=new ArrayList<>();
    public static List<Boolean> true_false_for_results=new ArrayList<>();


}
