package com.rival.algoview.Sorting;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rival.algoview.R;

import java.util.Random;

public class SortingMainActivity extends AppCompatActivity {
    LinearLayout ll;
    Button btnTranslate;
    int width, height;
    SeekBar seekbar;
    int[] a;
    ImageButton btnPlayPause , btnRepeat, btnNextStep;
    Button btnBubbleSort, btnSelectionSort, btnInsertionSort, btnMergeSort, btnHeapSort, btnQuickSort;
    static int sleepValues[] = {800,750,450,300,200,150,130,110,90,70,50, 35, 25, 20,15,10,8,5,3,3,3,3,3,3,3};
    static int sleepIndex = 1;
    final static int BUBBLE_SORT =1;
    final static int SELECTION_SORT =2;
    final static int INSERTION_SORT =3;
    final static int MERGE_SORT =4;
    final static int HEAP_SORT =5;
    final static int QUICK_SORT =6;
    int CURRENT_SORTING = 1;
    Thread SortingAnimationThread;
    boolean algorithmSelectionLocked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_main);
        SortingPlayBackControl.resetValues();
        getSupportActionBar().hide();
        ll= findViewById(R.id.linearlayout);
        seekbar=  findViewById(R.id.seekBar);
        sleepIndex = 1;
        btnBubbleSort = findViewById(R.id.bubble_sort);
        btnBubbleSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
        btnSelectionSort= findViewById(R.id.selection_sort);
        btnInsertionSort = findViewById(R.id.insertion_sort);
        btnMergeSort = findViewById(R.id.mege_sort);
        btnHeapSort = findViewById(R.id.heap_sort);
        btnQuickSort = findViewById(R.id.quick_sort);
        btnPlayPause= findViewById(R.id.play_pause);
        btnRepeat = findViewById(R.id.repeat);
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(SortingAnimationThread!=null)
                     SortingAnimationThread.interrupt();
                SortingPlayBackControl.resetValues();
                disableAlgorithmsButton(false);
                algorithmSelectionLocked = false;
                seekBarVisibility(true);
                btnPlayPause.setBackgroundResource(R.drawable.round_green);
                btnPlayPause.setImageResource(R.drawable.play_image_button);
                ll.post(new Runnable() {
                    @Override
                    public void run() {
                        ll.getHandler().removeCallbacksAndMessages(null);
                        resetBars();
                    }
                });
                return;
            }

        });
        btnNextStep = findViewById(R.id.next_step);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SortingPlayBackControl.isRunning == false && SortingPlayBackControl.isPaused == false  && SortingPlayBackControl.isFinished == false)
                {
                    SortingPlayBackControl.isRunning = false;
                    SortingPlayBackControl.isPaused = true;
                    seekBarVisibility(false);
                    disableAlgorithmsButton(true);
                    algorithmSelectionLocked = true;
                    playSorting();
                }
                SortingPlayBackControl.nextStep = true;
            }
        });
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SortingMainActivity", SortingPlayBackControl.isRunning +" "+ SortingPlayBackControl.isPaused +" "+ SortingPlayBackControl.isFinished);
                if(SortingPlayBackControl.isRunning == false && SortingPlayBackControl.isPaused == false  && SortingPlayBackControl.isFinished == false)
                {
                    SortingPlayBackControl.isRunning = true;
                    btnPlayPause.setBackgroundResource(R.drawable.round_red);
                    btnPlayPause.setImageResource(R.drawable.pause_image_button);
                    seekBarVisibility(false);
                    disableAlgorithmsButton(true);
                    algorithmSelectionLocked = true;
                    playSorting();
                }

                else if(SortingPlayBackControl.isRunning ==true && SortingPlayBackControl.isPaused== false)
                {
                    SortingPlayBackControl.isRunning = false;
                    SortingPlayBackControl.isPaused = true;
                    btnPlayPause.setBackgroundResource(R.drawable.round_green);
                    btnPlayPause.setImageResource(R.drawable.play_image_button);
                }
                else if(SortingPlayBackControl.isRunning ==false && SortingPlayBackControl.isPaused== true)
                {
                    SortingPlayBackControl.isRunning = true;
                    SortingPlayBackControl.isPaused = false;
                    btnPlayPause.setBackgroundResource(R.drawable.round_red);
                    btnPlayPause.setImageResource(R.drawable.pause_image_button);
                }
                else if(SortingPlayBackControl.isFinished ==true)
                {
                    SortingPlayBackControl.isRunning = false;
                    SortingPlayBackControl.isPaused = false;
                    SortingPlayBackControl.isFinished = false;
                    resetBars();
                    btnPlayPause.callOnClick();
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                initBars();
                sleepIndex = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ll.post(new Runnable() {
            @Override
            public void run() {
                width = ll.getWidth();
                height = ll.getHeight();
                print(width+  " "+ height);
                initBars();

            }
        });

    }
    public void initBars()
    {
        ll.removeAllViews();
        int lowerbound = 10;
        int upperbound = height/10;
        int n= (seekbar.getProgress()+1) * 5;
        print("n : "+ n);
        double barWidthWithPadding = width / n;
        a = new int[n];
        double barWidthPercentage=0.7;
        for(int i=0;i<n;i++)
        {
            int x = new Random().nextInt(upperbound-lowerbound) + lowerbound;
            a[i]= x;
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams((int) (barWidthPercentage*barWidthWithPadding),  x*10);
            dim.gravity= Gravity.BOTTOM;
            dim.setMargins((int)(0.15*barWidthWithPadding), 0, (int)(0.15*barWidthWithPadding), 0);
            dim.weight = 1;
            tv.setLayoutParams(dim);
            tv.setBackgroundColor(getResources().getColor(R.color.bar_color));
            tv.setSingleLine();
            if(n<=10)
            tv.setText(a[i]+"");
            tv.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            ll.addView(tv);
        }
    }
    public void translate(final int pos1, final int pos2)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int p1= Math.min(pos1, pos2);
                int p2= Math.max(pos1, pos2);
                final TextView tv1 = (TextView) ll.getChildAt(p1);
                final TextView tv2 = (TextView) ll.getChildAt(p2);
                ll.removeView(tv1);
                ll.removeView(tv2);
                ll.addView(tv2, p1);
                ll.addView(tv1, p2);
            }
        });
    }
    public void playSorting()
    {
        Log.d("SortingMainActivity", "playsorting");
        final int b[] =a.clone();
        SortingAnimationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("SortingMainActivity", "Inside thread");
                switch (CURRENT_SORTING)
                {
                    case SortingMainActivity.BUBBLE_SORT :
                        BubbleSortHandler bs = new BubbleSortHandler(b , SortingMainActivity.this, ll);
                        try{
                            bs.runner();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case SortingMainActivity.SELECTION_SORT:
                        SelectionSortHandler ss = new SelectionSortHandler(b , SortingMainActivity.this, ll);
                        try {
                            ss.runner();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SortingMainActivity.INSERTION_SORT:
                        InsertionSortHandler is = new InsertionSortHandler(b , SortingMainActivity.this, ll);
                        try {
                            is.runner();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SortingMainActivity.MERGE_SORT:
                        MergeSortHandler ms = new MergeSortHandler(b , SortingMainActivity.this, ll);
                        try {
                            ms.runner();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SortingMainActivity.HEAP_SORT:
                        HeapSortHandler hs = new HeapSortHandler(b , SortingMainActivity.this, ll);
                        try {
                            hs.runner();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SortingMainActivity.QUICK_SORT:
                        QuickSortHandler qs = new QuickSortHandler(b , SortingMainActivity.this, ll);
                        try {
                            qs.runner();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });
        SortingAnimationThread.start();
    }

    public void print(String s)
    {
        System.out.println(s);
    }
    public void sortingMethodSelected(View v)
    {
        if((algorithmSelectionLocked ==true))
        {
            Toast.makeText(this, "Feature not available", Toast.LENGTH_SHORT).show();
            return;
        }
        int id =v.getId();
        switch (id)
        {
            case R.id.bubble_sort :
                CURRENT_SORTING = SortingMainActivity.BUBBLE_SORT;
                btnBubbleSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
                btnInsertionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnSelectionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnMergeSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnHeapSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnQuickSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                break;
            case R.id.selection_sort :
                CURRENT_SORTING = SortingMainActivity.SELECTION_SORT;
                btnSelectionSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
                btnInsertionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnBubbleSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnMergeSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnHeapSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnQuickSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                break;
            case R.id.insertion_sort :
                CURRENT_SORTING = SortingMainActivity.INSERTION_SORT;
                btnInsertionSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
                btnBubbleSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnSelectionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnMergeSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnHeapSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnQuickSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                break;
            case R.id.mege_sort:
                CURRENT_SORTING = SortingMainActivity.MERGE_SORT;
                btnMergeSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
                btnBubbleSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnSelectionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnInsertionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnHeapSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnQuickSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                break;
            case R.id.heap_sort :
                CURRENT_SORTING = SortingMainActivity.HEAP_SORT;
                btnHeapSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
                btnBubbleSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnSelectionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnMergeSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnInsertionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnQuickSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                break;
            case R.id.quick_sort :
                CURRENT_SORTING = SortingMainActivity.QUICK_SORT;
                btnQuickSort.setBackgroundResource(R.drawable.sorting_algo_selected_button_bg);
                btnBubbleSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnSelectionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnMergeSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnHeapSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                btnInsertionSort.setBackgroundResource(R.drawable.sorting_alog_button_bg);
                break;
        }
    }
    public void SortingFinished()
    {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               btnPlayPause.setBackgroundResource(R.drawable.round_green);
               btnPlayPause.setImageResource(R.drawable.play_image_button);
               SortingPlayBackControl.isRunning = false;
               SortingPlayBackControl.isPaused = false;
               seekBarVisibility(true);
               disableAlgorithmsButton(false);
               algorithmSelectionLocked = false;
           }
       });
    }

    private void disableAlgorithmsButton(boolean b) {
        if(b== true)
        {
            Log.d("DAB", "hmm");
            btnInsertionSort.setTextColor(getResources().getColor(R.color.disable_button));
            btnQuickSort.setTextColor(getResources().getColor(R.color.disable_button));
            btnHeapSort.setTextColor(getResources().getColor(R.color.disable_button));
            btnMergeSort.setTextColor(getResources().getColor(R.color.disable_button));
            btnBubbleSort.setTextColor(getResources().getColor(R.color.disable_button));
            btnSelectionSort.setTextColor(getResources().getColor(R.color.disable_button));
        }
        else
        {
            btnInsertionSort.setTextColor(Color.BLACK);
            btnQuickSort.setTextColor(Color.BLACK);
            btnHeapSort.setTextColor(Color.BLACK);
            btnMergeSort.setTextColor(Color.BLACK);
            btnBubbleSort.setTextColor(Color.BLACK);
            btnSelectionSort.setTextColor(Color.BLACK);
        }
    }

    public void resetBars()
    {
        ll.removeAllViews();
        int n= (seekbar.getProgress()+1) * 5;
        print("n : "+ n);
        double barWidthWithPadding = width / n;
        double barWidthPercentage=0.7;
        for(int i=0;i<n;i++)
        {
            int x = a[i];
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams((int) (barWidthPercentage*barWidthWithPadding),  x*10);
            dim.gravity= Gravity.BOTTOM;
            dim.setMargins((int)(0.15*barWidthWithPadding), 0, (int)(0.15*barWidthWithPadding), 0);
            dim.weight = 1;
            tv.setLayoutParams(dim);
            tv.setBackgroundColor(getResources().getColor(R.color.bar_color));
            tv.setSingleLine();
            if(n<=10)
                tv.setText(a[i]+"");
            tv.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            ll.addView(tv);
        }
        print("bar reset  done");
    }
    public void seekBarVisibility(boolean v)
    {
        if(v== true)
            seekbar.setVisibility(View.VISIBLE);
        else
            seekbar.setVisibility(View.INVISIBLE);
    }
    }