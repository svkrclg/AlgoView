package com.rival.algoview.Sorting;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;

class BubbleSortElementEvent {
    int i;
    int j;
    int x;
    final static int CHECK_BAR = 1;
    final static int SWAP_BAR = 2;
    final static int SORTED_BAR =3;
    int eventType;
    BubbleSortElementEvent(int eventType )
    {
        this.eventType = eventType;
    }
    public void setI(int i) {
        this.i = i;
    }
    public void setJ(int j) {
        this.j = j;
    }
    public void setX(int x) {
        this.x = x;
    }

}

public class BubbleSortHandler {
    int a[];
    SortingMainActivity obj;
    LinearLayout ll;
    String TAG ="BubbleSort";
    BubbleSortHandler(int a[], SortingMainActivity obj, LinearLayout ll)
    {
        this.a = a;
        this.obj = obj;
        this.ll = ll;
    }
    public void runner() throws InterruptedException {
        int sleep = SortingMainActivity.sleepValues[SortingMainActivity.sleepIndex];
            ArrayList<BubbleSortElementEvent> events = getBubbleSortResult();
            for(BubbleSortElementEvent event : events)
            {
                while(SortingPlayBackControl.isPaused ==true)
                {
                    if(SortingPlayBackControl.nextStep == true)
                    {
                        SortingPlayBackControl.nextStep = false;
                        break;
                    }
                    if(Thread.currentThread().isInterrupted() == true)
                         return;
                }
                switch (event.eventType)
                {
                    case BubbleSortElementEvent.CHECK_BAR :
                    {
                        final int i = event.i;
                        final int j = event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                        final TextView tv1 = (TextView) ll.getChildAt(i);
                        final TextView tv2 = (TextView) ll.getChildAt(j);
                                Log.d(TAG, "1");
                                tv1.setBackgroundColor(obj.getResources().getColor(R.color.bar_check));
                                tv2.setBackgroundColor(obj.getResources().getColor(R.color.bar_check));
                            }
                        });
                        Thread.sleep(sleep);

                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv1 = (TextView) ll.getChildAt(i);
                                TextView tv2 = (TextView) ll.getChildAt(j);
                                tv1.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                                tv2.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });
                    }
                    break;
                    case BubbleSortElementEvent.SWAP_BAR :
                    {
                        final  int i = event.i;
                        final int j = event.j;

                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                        final TextView tv1 = (TextView) ll.getChildAt(i);
                        final TextView tv2 = (TextView) ll.getChildAt(j);
                                Log.d(TAG, "3");
                                tv1.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                                tv2.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                            }
                        });
                        Thread.sleep(sleep);

                        obj.translate(i, j);
                       Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv1 = (TextView) ll.getChildAt(i);
                                TextView tv2 = (TextView) ll.getChildAt(j);
                                Log.d(TAG, "4");
                                tv1.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                                tv2.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });

                    }
                    break;
                    case BubbleSortElementEvent.SORTED_BAR:
                    {
                        final int x= event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                               TextView tv = (TextView) ll.getChildAt(x);
                                Log.d(TAG, "5");
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                            }
                        });
                    }
                    break;

                }
            }
        SortingPlayBackControl.isFinished = true;
        obj.SortingFinished();

    }
    public ArrayList<BubbleSortElementEvent> getBubbleSortResult()
    {
        ArrayList<BubbleSortElementEvent> result = new ArrayList<>();
        int i, j;
        int n= a.length;
        for (i = 0; i < n-1; i++) {
            for (j = 0; j < n - i - 1; j++) {
                boolean isSwap = false;
                if (a[j] > a[j + 1])
                    {
                       isSwap = true;
                       int temp = a[j];
                       a[j] = a[j+1];
                       a[j+1] =temp;
                    }
                if(isSwap == true)
                {
                    BubbleSortElementEvent event = new BubbleSortElementEvent(BubbleSortElementEvent.SWAP_BAR);
                    event.setI(j);
                    event.setJ(j+1);
                    result.add(event);
                }
                else
                {
                    BubbleSortElementEvent event = new BubbleSortElementEvent(BubbleSortElementEvent.CHECK_BAR);
                    event.setI(j);
                    event.setJ(j+1);
                    result.add(event);
                }
             //   Log.d(TAG, "Added fuck: "+j+ " "+(j+1));
            }
            BubbleSortElementEvent event = new BubbleSortElementEvent(BubbleSortElementEvent.SORTED_BAR);
            event.setX(j);
            result.add(event);
           // Log.d(TAG, "added sorted: "+j);
        }
        BubbleSortElementEvent event = new BubbleSortElementEvent(BubbleSortElementEvent.SORTED_BAR);
        event.setX(0);
        result.add(event);
        return result;
    }
}
