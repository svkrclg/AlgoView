package com.rival.algoview.Sorting;

import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;
import java.util.Arrays;

class MergeSortElementEvent {
    int i;
    int j;
    int x;
    final static int NEW_COMPARISON = 1;
    final static int NEW_MERGE_GROUP = 2;
    final static int CHANGE_HEIGHT =3;
    int eventType;
    MergeSortElementEvent(int eventType )
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

public class MergeSortHandler {
    int a[];
    SortingMainActivity obj;
    LinearLayout ll;
    String TAG ="MergeSort";
    MergeSortHandler(int a[], SortingMainActivity obj, LinearLayout ll)
    {
        this.a = a;
        this.obj = obj;
        this.ll = ll;
    }
    public void runner() throws InterruptedException {
        int sleep = SortingMainActivity.sleepValues[SortingMainActivity.sleepIndex];
            ArrayList<MergeSortElementEvent> events = getMergeSortResult();
            System.out.println("Sorted: "+Arrays.toString(a));
            for(MergeSortElementEvent event : events)
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
                    case MergeSortElementEvent.NEW_MERGE_GROUP :
                    {
                        final int i= event.i;
                        final int j= event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                for(int k=i;k<=j;k++)
                                {
                                    TextView tv = (TextView) ll.getChildAt(k);
                                    tv.setBackgroundColor(Color.YELLOW);
                                }
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case MergeSortElementEvent.CHANGE_HEIGHT:
                    {
                        final int i= event.i;
                        final int newH= event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv = (TextView) ll.getChildAt(i);
                                LinearLayout.LayoutParams lp =(LinearLayout.LayoutParams) tv.getLayoutParams();
                                lp.height = newH*10;
                                tv.setLayoutParams(lp);
                                if(a.length<=10)
                                    tv.setText(newH+"");
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
    public ArrayList<MergeSortElementEvent> getMergeSortResult() {
        ArrayList<MergeSortElementEvent> result = new ArrayList<>();
        int n = a.length;
        mergeSort(result, a, 0, n-1);
        return  result;
    }
    public void mergeSort(ArrayList<MergeSortElementEvent> results, int a[], int l , int r)
    {
        if(l<r)
        {
            int m = l+(r-l)/2;
            mergeSort(results, a, l , m);
            mergeSort(results, a, m+1 , r);
            merge(results, a, l, m ,r);

        }
    }
    public void merge(ArrayList<MergeSortElementEvent> results, int a[], int l, int m , int r)
    {
        ArrayList<Integer> sortedArray = new ArrayList<>();
        int i = l;
        int j = m+1;
        MergeSortElementEvent newMerge = new MergeSortElementEvent(MergeSortElementEvent.NEW_MERGE_GROUP);
        //change to green
        newMerge.setI(l);
        newMerge.setJ(r);
        Log.d("Merge", "newMerge: "+ newMerge.i+" "+newMerge.j);
        results.add(newMerge);
        while(i<=m && j<=r)
        {
            //change color to red
         /*   MergeSortElementEvent newComparison  = new MergeSortElementEvent(MergeSortElementEvent.NEW_COMPARISON);
            newComparison.setI(i);
            newComparison.setJ(j);
            Log.d("Merge", "newComparison: "+ newComparison.i+" "+newComparison.j);
            results.add(newComparison);*/
            if(a[i]<=a[j])
            {
                //set i to purple
                MergeSortElementEvent changeHeight = new MergeSortElementEvent(MergeSortElementEvent.CHANGE_HEIGHT);
                changeHeight.setI(l+sortedArray.size());
                changeHeight.setJ(a[i]);
                results.add(changeHeight);
                Log.d("Merge", "chnageHe1: "+ changeHeight.i+" "+changeHeight.j);

                sortedArray.add(a[i]);
                i++;
            }
            else
            {
                MergeSortElementEvent changeHeight = new MergeSortElementEvent(MergeSortElementEvent.CHANGE_HEIGHT);
                changeHeight.setI(l+sortedArray.size());
                changeHeight.setJ(a[j]);
                results.add(changeHeight);
                Log.d("Merge", "chnageHe2: "+ changeHeight.i+" "+changeHeight.j);
                sortedArray.add(a[j]);
                j++;
            }
        }
        while(i <= m)
        {
            MergeSortElementEvent changeHeight = new MergeSortElementEvent(MergeSortElementEvent.CHANGE_HEIGHT);
            changeHeight.setI(l+sortedArray.size());
            changeHeight.setJ(a[i]);
            results.add(changeHeight);
            Log.d("Merge", "chnageHe3: "+ changeHeight.i+" "+changeHeight.j);
            sortedArray.add(a[i]);
            i++;
        }
        while(j <= r)
        {
            MergeSortElementEvent changeHeight = new MergeSortElementEvent(MergeSortElementEvent.CHANGE_HEIGHT);
            changeHeight.setI(l+sortedArray.size());
            changeHeight.setJ(a[j]);
            results.add(changeHeight);
            Log.d("Merge", "chnageHe4: "+ changeHeight.i+" "+changeHeight.j);
            sortedArray.add(a[j]);
            j++;
        }
        for(int k=l;k<=r;k++)
        {
            a[k] = sortedArray.get(k-l);
        }
    }
}