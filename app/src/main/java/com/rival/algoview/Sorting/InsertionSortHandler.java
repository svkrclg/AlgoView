package com.rival.algoview.Sorting;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;
import java.util.Arrays;

class InsertionSortElementEvent {
    int i;
    int j;
    int x;
    final static int NEW_KEY = 1;
    final static int NO_SWAP = 2;
    final static int SWAP_BAR =3;
    int eventType;
    InsertionSortElementEvent(int eventType )
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

public class InsertionSortHandler {
    int a[];
    SortingMainActivity obj;
    LinearLayout ll;
    String TAG ="InsertionSort";
    InsertionSortHandler(int a[], SortingMainActivity obj, LinearLayout ll)
    {
        this.a = a;
        this.obj = obj;
        this.ll = ll;
    }
    public void runner() throws InterruptedException {
        int sleep = SortingMainActivity.sleepValues[SortingMainActivity.sleepIndex];
            ArrayList<InsertionSortElementEvent> events = getInsertionSortResult();
            for(InsertionSortElementEvent event : events)
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
                    case InsertionSortElementEvent.NEW_KEY :
                    {
                        final int newKey =event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView  tv = (TextView) ll.getChildAt(newKey);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case InsertionSortElementEvent.SWAP_BAR:
                    {
                        final int toSortBarIndex = event.j;
                        final int toReplaceWith = event.i;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv = (TextView) ll.getChildAt(toReplaceWith);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                obj.translate(toSortBarIndex, toReplaceWith);
                            }
                        });
                        Thread.sleep(sleep);
                        //change red bar back to normal color
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                   TextView tv = (TextView) ll.getChildAt(toSortBarIndex);
                                   tv.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case InsertionSortElementEvent.NO_SWAP:
                    {
                        final int i = event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                               TextView tv = (TextView)ll.getChildAt(i);
                               tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_check));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv = (TextView)ll.getChildAt(i);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                }
            }
        SortingPlayBackControl.isFinished = true;
        obj.SortingFinished();

    }
    public ArrayList<InsertionSortElementEvent> getInsertionSortResult()
    {
        ArrayList<InsertionSortElementEvent> result = new ArrayList<>();
        int n= a.length;
        int i, key, j;
        Log.d("insertion", "Array: "+ Arrays.toString(a));
        for (i = 1; i < n; i++)
        {
            key = a[i];
            InsertionSortElementEvent newKey = new InsertionSortElementEvent(InsertionSortElementEvent.NEW_KEY);
            newKey.setX(i);
            result.add(newKey);
            Log.d("insertion", "1: "+ newKey.x+" ");
            j = i - 1;
            boolean entered = false;
            while (j >= 0 && a[j] > key)
            {
                entered = true;
                InsertionSortElementEvent toSwapWith = new InsertionSortElementEvent(InsertionSortElementEvent.SWAP_BAR);
                toSwapWith.setI(j);
                toSwapWith.setJ(j+1);
                result.add(toSwapWith);
                Log.d("insertion", "2: "+ toSwapWith.i+" "+ toSwapWith.j);
                a[j + 1] = a[j];
                a[j]= key;
                j = j - 1;
            }
            if(entered == false)
            {
                InsertionSortElementEvent justCheck = new InsertionSortElementEvent(InsertionSortElementEvent.NO_SWAP);
                justCheck.setX(j);
                result.add(justCheck);
                Log.d("insertion", "3: "+ justCheck.x+" ");
            }

        }
        return result;
    }
}