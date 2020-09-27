package com.rival.algoview.Sorting;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;
import java.util.Arrays;

class HeapSortElementEvent {
    int i;
    int j;
    int x;
    final static int SELECT_ZERO_AND_PUT_AT_LAST = 1;
    final static int NEW_HEAP_BAR = 2;
    final static int COLOR_SWAP_AND_RESTORE=3;
    final static int RESTORE_COLOR = 4;
    final static int FINAL_ELEMENT  = 5;
    int eventType;
    HeapSortElementEvent(int eventType )
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

public class HeapSortHandler {
    int a[];
    SortingMainActivity obj;
    LinearLayout ll;
    String TAG ="HeapSort";
    HeapSortHandler(int a[], SortingMainActivity obj, LinearLayout ll)
    {
        this.a = a;
        this.obj = obj;
        this.ll = ll;
    }
    public void runner() throws InterruptedException {
        final int sleep = SortingMainActivity.sleepValues[SortingMainActivity.sleepIndex];
            ArrayList<HeapSortElementEvent> events = getHeapSortResult();
            System.out.println("Sorted: "+Arrays.toString(a));
            for(HeapSortElementEvent event : events)
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
                    case HeapSortElementEvent.NEW_HEAP_BAR:
                     {
                         final int x= event.x;
                         ll.post(new Runnable() {
                             @Override
                             public void run() {
                                 TextView tv= (TextView) ll.getChildAt(x);
                                 tv.setBackgroundColor(obj.getResources().getColor(R.color.bar_examining));
                             }
                         });
                     }
                     Thread.sleep(sleep);
                    break;
                    case HeapSortElementEvent.COLOR_SWAP_AND_RESTORE:
                    {
                        final int largest = event.i;
                        final int heapy = event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView TVlarge  = (TextView) ll.getChildAt(largest);
                                TVlarge.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));

                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                obj.translate(largest, heapy);
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView TVlarge  = (TextView) ll.getChildAt(largest);
                                TextView TVHeapy = (TextView) ll.getChildAt(heapy);
                                TVlarge.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                                TVHeapy.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                        break;
                    }
                    case HeapSortElementEvent.RESTORE_COLOR:
                    {
                        final int h = event.x;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView TVlarge  = (TextView) ll.getChildAt(h);
                                TVlarge.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));

                            }
                        });
                        Thread.sleep(sleep);
                        break;
                    }
                    case HeapSortElementEvent.SELECT_ZERO_AND_PUT_AT_LAST:
                    {
                        final int zeroth = event.i;
                        final int last = event.j;
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView TVZero  = (TextView) ll.getChildAt(zeroth);
                                TVZero.setBackgroundColor(obj.getResources().getColor(R.color.sorted_bar));
                                TextView TVLast  = (TextView) ll.getChildAt(last);
                                TVLast.setBackgroundColor(obj.getResources().getColor(R.color.swap_bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                obj.translate(zeroth, last);
                            }
                        });
                        Thread.sleep(sleep);
                        ll.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView TVZero  = (TextView) ll.getChildAt(zeroth);
                                TVZero.setBackgroundColor(obj.getResources().getColor(R.color.bar_color));
                            }
                        });
                        Thread.sleep(sleep);
                    }
                    break;
                    case HeapSortElementEvent.FINAL_ELEMENT:
                    {
                        final int x= event.x;
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
                }
            }
        SortingPlayBackControl.isFinished = true;
        obj.SortingFinished();
    }
    public ArrayList<HeapSortElementEvent> getHeapSortResult() {
        ArrayList<HeapSortElementEvent> result = new ArrayList<>();
        int n = a.length;
        HeapSort(result,a, n);
        return  result;
    }
    public void HeapSort(ArrayList<HeapSortElementEvent> results, int a[], int n)
    {
           for(int i = n/2-1 ; i>=0; i--)
           {
               heapify(results, a, n, i);
           }
          for (int i=n-1; i>0; i--)
          {
              HeapSortElementEvent selectZero = new HeapSortElementEvent(HeapSortElementEvent.SELECT_ZERO_AND_PUT_AT_LAST);
              selectZero.setI(0); //change color to purple
              selectZero.setJ(i);// change to red and swap and restore this color
              results.add(selectZero);
            int temp = a[0];
            a[0] = a[i];
            a[i] = temp;

            heapify(results, a, i, 0);
          }
          HeapSortElementEvent finalElementToPurple = new HeapSortElementEvent(HeapSortElementEvent.FINAL_ELEMENT);
          finalElementToPurple.setX(0);
          results.add(finalElementToPurple);
    }
    public void heapify(ArrayList<HeapSortElementEvent> results, int a[], int n, int i)
    {
        HeapSortElementEvent newHeapBar = new HeapSortElementEvent(HeapSortElementEvent.NEW_HEAP_BAR);
        newHeapBar.setX(i); //change color to bar_examining
        results.add(newHeapBar);
        int largest = i;
        int l = 2*i + 1; // left child
        int r = 2*i + 2; // right child

        // If left child is larger than root
        boolean isLeft =true;
        if (l < n && a[l] > a[largest])
            largest = l;

        // If right child is larger than largest so far
        if (r < n && a[r] > a[largest])
            largest = r;

        // If largest is not root
        if (largest != i)
        {
            int swap = a[i];
            a[i] = a[largest];
            a[largest] = swap;
            HeapSortElementEvent restoreColors = new HeapSortElementEvent(HeapSortElementEvent.COLOR_SWAP_AND_RESTORE);
            restoreColors.setI(largest); //change to red
            restoreColors.setJ(i);
            results.add(restoreColors);
            // Recursively heapify the affected sub-tree
            heapify(results , a, n, largest);
        }
        else
        {
            HeapSortElementEvent restoreColor = new HeapSortElementEvent(HeapSortElementEvent.RESTORE_COLOR);
            restoreColor.setX(i); //change to bar color
            results.add(restoreColor);
        }

    }

}