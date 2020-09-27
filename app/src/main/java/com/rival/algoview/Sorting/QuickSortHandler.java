package com.rival.algoview.Sorting;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;
import java.util.Arrays;

class QuickSortElementEvent {
    int i;
    int j;
    int x;
    final static int NEW_PIVOT = 1;
    final static int SWAP_AROUND_PARTITION = 2;
    final static int CHECK_AROUND_PARTITION=3;
    final static int SWAP_PARTITION = 4;
    int eventType;
    QuickSortElementEvent(int eventType )
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

public class QuickSortHandler {
    int a[];
    SortingMainActivity obj;
    LinearLayout ll;
    String TAG ="QuickSort";
    QuickSortHandler(int a[], SortingMainActivity obj, LinearLayout ll)
    {
        this.a = a;
        this.obj = obj;
        this.ll = ll;
    }
    public void runner() throws InterruptedException {
        final int sleep = SortingMainActivity.sleepValues[SortingMainActivity.sleepIndex];
            ArrayList<QuickSortElementEvent> events = getQuickSortResult();
            System.out.println("Sorted: "+Arrays.toString(a));
            for(QuickSortElementEvent event : events)
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
                switch (event.eventType) {
                    case QuickSortElementEvent.NEW_PIVOT:
                    {
                        final  int x= event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                 TextView tv= (TextView) ll.getChildAt(x);
                                 tv.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case QuickSortElementEvent.SWAP_AROUND_PARTITION:
                    {
                        final int i = event.i;
                        final int j = event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv1= (TextView) ll.getChildAt(i);
                                TextView tv2 = (TextView)ll.getChildAt(j);
                                tv1.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                                tv2.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));

                            }
                        });
                        Thread.sleep(sleep);
                        if(i!=j)
                        {
                            ll.post(new Runnable() {
                                @Override
                                public void run() {
                                    obj.translate(i,j);
                                }
                            });
                        }
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv1= (TextView) ll.getChildAt(i);
                                TextView tv2 = (TextView)ll.getChildAt(j);
                                tv1.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                                tv2.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));

                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case QuickSortElementEvent.CHECK_AROUND_PARTITION:
                    {
                        final int i = event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv= (TextView) ll.getChildAt(i);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_check));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv= (TextView) ll.getChildAt(i);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case QuickSortElementEvent.SWAP_PARTITION:
                    {
                        final int i = event.i;
                        final int j = event.j;
                        if(i==j)
                        {
                            Thread.sleep(sleep);
                            break;
                        }
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                              TextView tv= (TextView) ll.getChildAt(i);
                              tv.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                obj.translate(i ,j);
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv= (TextView) ll.getChildAt(j);
                                tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
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
    public ArrayList<QuickSortElementEvent> getQuickSortResult() {
        ArrayList<QuickSortElementEvent> result = new ArrayList<>();
        int n = a.length;
        QuickSort(result,a, 0, n-1 );
        return  result;
    }
    public void QuickSort(ArrayList<QuickSortElementEvent> results, int a[],int low,  int high)
    {

                if( low  < high)
                {  QuickSortElementEvent newPivot = new QuickSortElementEvent(QuickSortElementEvent.NEW_PIVOT);
         newPivot.setX(high); //change to purple
         results.add(newPivot);
                    int pi = partition(results, a, low, high);
                    QuickSort(results, a, low, pi-1);
                    QuickSort(results, a, pi+1, high);
                }
                else if(low==high)
                {
                    QuickSortElementEvent newPivot = new QuickSortElementEvent(QuickSortElementEvent.NEW_PIVOT);
                    newPivot.setX(high); //change to purple
                    results.add(newPivot);
                }
    }
    public int partition(ArrayList<QuickSortElementEvent> results, int a[], int low , int high)
    {
         int pivot = a[high];
         QuickSortElementEvent newPivot = new QuickSortElementEvent(QuickSortElementEvent.NEW_PIVOT);
         newPivot.setX(high); //change to purple
         results.add(newPivot);
         int i  = low-1;
         for(int  j = low; j < high ; j++)
         {
             boolean isLess = false;
             if(a[j] < pivot)
             {
                 isLess = true;
                 i++;
                 int temp = a[j];
                 a[j] = a[i];
                 a[i]  =temp;
             }
             if(isLess == true)
             {
                 QuickSortElementEvent swapAroundPartition = new QuickSortElementEvent(QuickSortElementEvent.SWAP_AROUND_PARTITION);
                 swapAroundPartition.setI(j); // turn to red
                 swapAroundPartition.setJ(i); //turn to red
                 //and swap and restore color
                 results.add(swapAroundPartition);
             }
             else
             {
                 QuickSortElementEvent checkAroundPartition = new QuickSortElementEvent(QuickSortElementEvent.CHECK_AROUND_PARTITION);
                 checkAroundPartition.setX(j); // turn to green
                 //and swap and restore color
                 results.add(checkAroundPartition);
             }

         }
         i++;
         int temp = a[i];
         a[i] = a[high];
         a[high] = temp;
         // swap partition postion
        QuickSortElementEvent swapPartition = new QuickSortElementEvent(QuickSortElementEvent.SWAP_PARTITION);
        swapPartition.setI(i); // turn to red
        swapPartition.setJ(high);
        //and swap and restore color of i
        results.add(swapPartition);
         return i;
    }

}