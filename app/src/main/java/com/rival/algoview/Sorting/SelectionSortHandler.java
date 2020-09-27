package com.rival.algoview.Sorting;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;

class SelectionSortElementEvent {
    int i;
    int j;
    int x;
    final static int MINIMUM_BAR_CHANGED = 1;
    final static int MINIMUM_BAR_UNCHANGED = 2;
    final static int SWAP_BAR =3;
    final static int SORTED_BAR = 4;
    final static int SET_MINIMUM_BAR = 5;
    int eventType;
    SelectionSortElementEvent(int eventType )
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

public class SelectionSortHandler {
    int a[];
    SortingMainActivity obj;
    LinearLayout ll;
    String TAG ="SelectionSort";
    SelectionSortHandler(int a[], SortingMainActivity obj, LinearLayout ll)
    {
        this.a = a;
        this.obj = obj;
        this.ll = ll;
    }
    public void runner() throws InterruptedException {
        int sleep = SortingMainActivity.sleepValues[SortingMainActivity.sleepIndex];
            ArrayList<SelectionSortElementEvent> events = getSelectionSortResult();
            for(SelectionSortElementEvent event : events)
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
                    case SelectionSortElementEvent.SET_MINIMUM_BAR :
                    {
                        final int x= event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv= (TextView)  ll.getChildAt(x);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                            }
                        });
                        Thread.sleep(sleep);

                    }
                    break;
                    case SelectionSortElementEvent.MINIMUM_BAR_UNCHANGED :
                    {
                        final int x= event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv= (TextView)  ll.getChildAt(x);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_check));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv= (TextView)  ll.getChildAt(x);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case SelectionSortElementEvent.MINIMUM_BAR_CHANGED :
                    {
                        final int newMinIdx = event.i;
                        final int oldMinIdx = event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvNew= (TextView)  ll.getChildAt(newMinIdx);
                                tvNew.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvNew= (TextView)  ll.getChildAt(newMinIdx);
                                TextView tvOld= (TextView)  ll.getChildAt(oldMinIdx);
                                tvNew.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                                tvOld.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case SelectionSortElementEvent.SWAP_BAR :
                    {
                        final int sortBarIndex = event.i;
                        final int toReplaceWithIndex = event.j;
                        if(sortBarIndex==toReplaceWithIndex)
                            break;
                        else
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                obj.translate(sortBarIndex, toReplaceWithIndex);
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
    public ArrayList<SelectionSortElementEvent> getSelectionSortResult()
    {
        ArrayList<SelectionSortElementEvent> result = new ArrayList<>();
        int n= a.length;
        int i, j, min_idx ;
        for (i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            min_idx = i;
            SelectionSortElementEvent e= new SelectionSortElementEvent(SelectionSortElementEvent.SET_MINIMUM_BAR);
            e.setX(i);
            result.add(e);
            Log.d("SelectionS", "Set minimum bar "+i);
            for (j = i+1; j < n; j++)
            {
                boolean gotNewBar= false;
                int oldMinIdx= -1;
                if (a[j] < a[min_idx])
                {
                    gotNewBar = true;
                    oldMinIdx= min_idx;
                    min_idx = j;
                }
                if(gotNewBar==false)
                {
                    SelectionSortElementEvent event = new SelectionSortElementEvent(SelectionSortElementEvent.MINIMUM_BAR_UNCHANGED);
                    event.setX(j);
                    result.add(event);
                    Log.d("SelectionS", "minimum bar Unchanged "+j);
                }
               else
                {
                    SelectionSortElementEvent ev= new SelectionSortElementEvent(SelectionSortElementEvent.MINIMUM_BAR_CHANGED);
                    ev.setI(min_idx);
                    ev.setJ(oldMinIdx);
                    result.add(ev);
                    Log.d("SelectionS", " minimum bar changed "+ min_idx +" "+oldMinIdx);
                }
            }

            // Swap the found minimum element with the first element

            int temp = a[min_idx];
            a[min_idx] =a[i];
            a[i]= temp;
            SelectionSortElementEvent swapEvent= new SelectionSortElementEvent(SelectionSortElementEvent.SWAP_BAR);
            swapEvent.setI(min_idx);
            swapEvent.setJ(i);
            result.add(swapEvent);
            Log.d("SelectionS", "swap bar "+min_idx+" "+i);

        }
        SelectionSortElementEvent last= new SelectionSortElementEvent(SelectionSortElementEvent.SET_MINIMUM_BAR);
        last.setX(n-1);
        result.add(last);
        return result;
    }
}
