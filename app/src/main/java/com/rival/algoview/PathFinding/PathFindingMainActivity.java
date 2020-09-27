package com.rival.algoview.PathFinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rival.algoview.R;

import java.util.ArrayList;

public class PathFindingMainActivity extends AppCompatActivity implements ControlFragment.ControlPanelButtonClicks, ResultFragment.ResultFragmentButtonClick {

    GridView gridView;
    ArrayList cells = new ArrayList<>();
    boolean selectSource = false;
    boolean selectDestination = false;
    TextView guide;
    int sourcePostion = -1;
    int destinationPosition = -1;
    private String TAG = "MainActivity";
    private int sleepDurationArray[] = new int[]{512, 256, 128, 64, 32};
    private int sleepIndex= 2;
    private boolean toContinueAnimation = true;
    private int gridViewHeight;
    private int gridViewWidth;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private boolean isFinished = false;
    private boolean isWallCreationAllowed=false;
    private String GUIDE_FRAGMENT_TAG = "GUIDE FRAGMENT TAG";
    private String CONTROL_FRAGMENT_TAG = "CONTROL FRAGMENT TAG";
    private String RESULT_FRAGMENT_TAG = "RESULT_FRAGMENT_TAG";
    private final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private GuideFragment guideFragment;
    private ControlFragment controlFragment;
    private ResultFragment resultFragment;
    private int traversalIndex = 0;
    private boolean goToAnotherIndex = false;
    private int newIndexFromUser = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle ", "onCreate");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_pf);
        gridView = findViewById(R.id.grid_view);
        showGuideFragment();
        final CellAdapter myAdapter = new CellAdapter(this, R.layout.cell_layout_pf, cells);
        gridView.setAdapter(myAdapter);
        PathFindingAlgorithms.activityObj=this;
        gridView.post(new Runnable() {
            @Override
            public void run() {
                gridViewHeight = gridView.getHeight();
                gridViewWidth = gridView.getWidth();
                final int column = gridView.getNumColumns();
                Log.d(TAG, gridViewHeight + " --- " + gridViewWidth + " -- " + column);
                ArrayAdapter adapter = (ArrayAdapter) gridView.getAdapter();

                View childView = adapter.getView(0, null, gridView);
                childView.measure(UNBOUNDED, UNBOUNDED);
                int viewHeight = childView.getMeasuredHeight();
                int verticalSpacing = gridView.getVerticalSpacing();
                Log.d(TAG, viewHeight + " ---- " + verticalSpacing);
                int row = (int) Math.floor((double) gridViewHeight / (double) (viewHeight + verticalSpacing));
                Log.d(TAG, "No of rows: " + row);
                Log.d(TAG, "size: " + gridView.getCount());
                int totalChild = row * column;
                for (int i = 0; i < totalChild; i++)
                    cells.add("");
                myAdapter.notifyDataSetChanged();
                Graph.initialize(row, column);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, "Position: "+i, Toast.LENGTH_SHORT).show();
                System.out.println("onClick");
                TextView tv = view.findViewById(R.id.textView);
                int y = i % Graph.COLUMN;
                int x = i / Graph.COLUMN;
                if (selectSource == false) {
                    sourcePostion = i;
                    tv.setBackgroundResource(R.drawable.grey_to_green);
                    TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                    transitionDrawable.startTransition(200);
                    Graph.setPoint(x, y, 1);
                    selectSource = true;
                //    guideFragment = (GuideFragment) getSupportFragmentManager().findFragmentByTag(GUIDE_FRAGMENT_TAG);
                    guideFragment.updateTextView("Select Destination");
                } else if (selectSource == true && selectDestination == false) {
                    if (i == sourcePostion)
                        return;
                    destinationPosition = i;
                    tv.setBackgroundResource(R.drawable.grey_to_red);
                    TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                    transitionDrawable.startTransition(200);
                    Graph.setPoint(x, y, 2);
                    selectDestination = true;
                    showControlFragment();
                    getSupportFragmentManager().executePendingTransactions();
                  //  controlFragment = (ControlFragment) getSupportFragmentManager().findFragmentByTag(CONTROL_FRAGMENT_TAG);

                }
            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ((selectSource == true && selectDestination == true) == false)
                    return false;
                if (isRunning==true)
                    return false;
                if(isPaused==true)
                    return false;
                if(resultFragment!= null && resultFragment.isVisible()==true)
                    return false;
                int action = motionEvent.getActionMasked();
                int xcord = (int) motionEvent.getX();
                int ycord = (int) motionEvent.getY();
                GridView gv = (GridView) view;
                int position = gv.pointToPosition(xcord, ycord);
                if (position == sourcePostion || position == destinationPosition)
                    return false;
                if (position == gv.INVALID_POSITION)
                    return false;
                view.getParent().requestDisallowInterceptTouchEvent(true);
                RelativeLayout relativeLayoutView = (RelativeLayout) gv.getChildAt(position);
                if (relativeLayoutView == null)
                    return false;
                TextView cellView = relativeLayoutView.findViewById(R.id.textView);
                int x = position / Graph.COLUMN;
                int y = position % Graph.COLUMN;
                cellView.setBackgroundResource(R.drawable.grey_to_black);
                TransitionDrawable transitionDrawable = (TransitionDrawable) cellView.getBackground();
                transitionDrawable.startTransition(200);
                Graph.setPoint(x, y, -1);
                // Toast.makeText(MainActivity.this, "Coordinates: "+x + " "+y, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void showGuideFragment() {
        Fragment guideFragment = new GuideFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, guideFragment, GUIDE_FRAGMENT_TAG);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        this.guideFragment=(GuideFragment) guideFragment;
    }

    public void showControlFragment() {
        Fragment controlFragment = new ControlFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, controlFragment, CONTROL_FRAGMENT_TAG);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        this.controlFragment= (ControlFragment)controlFragment;
        sleepIndex = ((ControlFragment) controlFragment).getCurrentTraversalSpeed();
    }

    @Override
    public void onPlayPauseClick() {
        Log.d(TAG, "Clicked");
        if (isRunning == false && isPaused == false) {
            if (isFinished == true) {
                isFinished = false;
                resetTraversal();
                traversalIndex = 0;
            }
            PathFindingAlgorithms.executePathFindingAlgorithm();
            toContinueAnimation = true;
            controlFragment.activateTraversalSeekBar(PathFindingAlgorithms.getPointTravelledSequence().size() - 1);
            moveTraversalForward();
            controlFragment.disableNextStepButton(true);
            isRunning = true;
            controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PAUSE);
        } else if (isRunning == true && isPaused == false) {
            toContinueAnimation = false;
            isRunning = false;
            controlFragment.disableNextStepButton(false);
            isPaused = true;
            controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PLAY);
        } else if (isRunning == false && isPaused == true) {
            toContinueAnimation = true;
            isRunning = true;
            controlFragment.disableNextStepButton(true);
            moveTraversalForward();
            isPaused = false;
            controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PAUSE);
        }
    }

    public void moveTraversalForward() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("1:  TI: "+traversalIndex+ ", size: "+PathFindingAlgorithms.getPointTravelledSequence().size());
                for (; traversalIndex < PathFindingAlgorithms.getPointTravelledSequence().size() && toContinueAnimation == true; traversalIndex++) {
                    if (goToAnotherIndex == true) {
                        int val = newIndexFromUser;
                        if (val < traversalIndex) {
                            traversalIndex--;
                            for (; traversalIndex >= val; traversalIndex--) {
                                Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
                                final int x = p.first;
                                final int y = p.second;
                                gridView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                                        tv.setBackgroundResource(R.drawable.grey_to_blue);
                                        TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                                        transitionDrawable.reverseTransition(100);
                                    }
                                });
                            }
                        } else {
                            for (; traversalIndex <= val; traversalIndex++) {
                                Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
                                final int x = p.first;
                                final int y = p.second;
                                gridView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                                        tv.setBackgroundResource(R.drawable.grey_to_blue);
                                        TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                                        transitionDrawable.startTransition(100);
                                    }
                                });
                            }
                        }
                        newIndexFromUser = -1;
                        goToAnotherIndex = false;
                    }
                    if (traversalIndex == -1) {
                        continue;
                    }
                    if (traversalIndex == PathFindingAlgorithms.getPointTravelledSequence().size()) {
                        showResultSheet();
                        showPathAnimation();
                        return;
                    }
                    controlFragment.updateSeekbarValue(traversalIndex);
                    Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
                    final int x = p.first;
                    final int y = p.second;
                    gridView.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                            tv.setBackgroundResource(R.drawable.grey_to_blue);
                            TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                            transitionDrawable.startTransition(100);
                        }
                    });
                    try {
                        Thread.sleep(sleepDurationArray[sleepIndex]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(toContinueAnimation==false)
                        return;

                }
                System.out.println("2:  TI: "+traversalIndex+ ", size: "+PathFindingAlgorithms.getPointTravelledSequence().size());
                if (traversalIndex == PathFindingAlgorithms.getPointTravelledSequence().size()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResultSheet();
                        }
                    });
                    showPathAnimation();
                }
            }
        }).start();

    }

    public void showPathAnimation() {
        ArrayList<Pair<Integer, Integer>> routePoints = PathFindingAlgorithms.getRoutePoints();
        for (int i = 0; i < routePoints.size(); i++) {
            final int x = routePoints.get(i).first;
            final int y = routePoints.get(i).second;
            gridView.post(new Runnable() {
                @Override
                public void run() {

                    final RelativeLayout rl = (RelativeLayout) gridView.getChildAt(x * Graph.COLUMN + y);
                    if (rl == null) {
                        System.out.println("Exception2");
                        return;
                    }
                    final TextView tv = rl.findViewById(R.id.textView);
                    tv.setBackgroundResource(R.drawable.grey_to_yellow);
                    TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                    transitionDrawable.startTransition(100);

                }
            });
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(toContinueAnimation==false)
                return;
        }
        animationCompleted();
    }

    public void animationCompleted() {
        isFinished = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PLAY_AGAIN);
            }
        });
        isRunning = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controlFragment.disableNextStepButton(true);
            }
        });
        isPaused = false;
    }

    public void resetTraversal() {
        ArrayList<Pair<Integer, Integer>> pointTravelledSequence = PathFindingAlgorithms.getPointTravelledSequence();
        System.out.println(pointTravelledSequence.size());
        int i = pointTravelledSequence.size() - 1;
        for (; i >= 0; i--) {
            // System.out.println(i+"");
            final int x = pointTravelledSequence.get(i).first;
            final int y = pointTravelledSequence.get(i).second;
            gridView.post(new Runnable() {
                @Override
                public void run() {
                    final RelativeLayout rl = (RelativeLayout) gridView.getChildAt(x * Graph.COLUMN + y);
                    if (rl == null) {
                        System.out.println("Exception3");
                        //  Log.d(TAG, "getting null: "+x +" "+y);
                        return;
                    }
                    rl.findViewById(R.id.textView).setBackgroundResource(R.drawable.rounded_corner_grey);
                }
            });
        }
    }

    public void showResultSheet()
    {
        Log.d("Support", "showResultSheet");
        resultFragment = new ResultFragment(PathFindingAlgorithms.getCost(), PathFindingAlgorithms.getNumber());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, resultFragment, RESULT_FRAGMENT_TAG);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }
    public void resetToInitialGraph()
    {
        controlFragment.deactivateTraversalSeekBar();
        traversalIndex=0;
        isRunning=false;
        isPaused=false;
        isFinished=false;
        toContinueAnimation=false;
        resetTraversal();
        controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PLAY);

    }
    public void notifyAlgorithmChange()
    {
        if(resultFragment!=null && resultFragment.isVisible()==true)
            super.onBackPressed();
        controlFragment.deactivateTraversalSeekBar();
        traversalIndex=0;
        isRunning=false;
        isPaused=false;
        isFinished=false;
        toContinueAnimation=false;
        resetTraversal();
        controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PLAY);
    }
    @Override
    public void traversalSeekBarValueUpdate(final int i, boolean b) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFinished == true) {
                    resetTraversal();
                    for (traversalIndex = 0; traversalIndex <= i; traversalIndex++) {
                        Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
                        final int x = p.first;
                        final int y = p.second;
                        gridView.post(new Runnable() {
                            @Override
                            public void run() {
                                final TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                                tv.setBackgroundResource(R.drawable.grey_to_blue);
                                TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                                transitionDrawable.startTransition(100);
                            }
                        });
                    }
                    isFinished = false;
                }
                if (isRunning == false) {
                    final int val = i;
                    //pause state
                    if (val < traversalIndex) {
                        if (traversalIndex == PathFindingAlgorithms.getPointTravelledSequence().size())
                            traversalIndex--;
                        for (; traversalIndex >= val; traversalIndex--) {
                            Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
                            final int x = p.first;
                            final int y = p.second;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                                    tv.setBackgroundResource(R.drawable.rounded_corner_grey);
                                      /* tv.setBackgroundResource(R.drawable.grey_to_blue);
                                       TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                                       transitionDrawable.reverseTransition(100);*/
                                }
                            });
                        }
                        if (traversalIndex == -1)
                            traversalIndex = 0;
                    } else {
                        for (; traversalIndex <= val; traversalIndex++) {

                            Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
                            final int x = p.first;
                            final int y = p.second;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                                            tv.setBackgroundResource(R.drawable.grey_to_blue);
                                            TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                                            transitionDrawable.startTransition(100);
                                }
                            });
                        }
                        if (traversalIndex == PathFindingAlgorithms.getPointTravelledSequence().size()) {
                            showResultSheet();
                            showPathAnimation();
                        }
                    }
                } else {
                    goToAnotherIndex = true;
                    newIndexFromUser = i;
                }
            }
        }).start();
    }

    @Override
    public void onNextStepButtonClick() {
        if(isRunning==false && isPaused==false && isFinished==false)
        {
            PathFindingAlgorithms.executePathFindingAlgorithm();
            controlFragment.activateTraversalSeekBar(PathFindingAlgorithms.getPointTravelledSequence().size()-1);
        }
        if (traversalIndex == PathFindingAlgorithms.getPointTravelledSequence().size()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showResultSheet();
                }
            });
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     toContinueAnimation=true;
                     showPathAnimation();
                 }
             }).start();
            return;
        }
        controlFragment.updateSeekbarValue(traversalIndex);
        Pair<Integer, Integer> p = PathFindingAlgorithms.getPointTravelledSequence().get(traversalIndex);
        final int x = p.first;
        final int y = p.second;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView tv = gridView.getChildAt(x * Graph.COLUMN + y).findViewById(R.id.textView);
                tv.setBackgroundResource(R.drawable.grey_to_blue);
                TransitionDrawable transitionDrawable = (TransitionDrawable) tv.getBackground();
                transitionDrawable.startTransition(100);
            }
        });
        traversalIndex++;
    }

    @Override
    public void onResetGraphButtonClick() {
        //resetFlag
        isRunning=false;
        toContinueAnimation=false;
        isPaused=false;
        isFinished=false;
        //reset GridView
        int count = gridView.getCount();
        for(int i=0;i<count;i++)
        {
            final int x= i;
            gridView.post(new Runnable() {
                @Override
                public void run() {
                    gridView.getChildAt(x).findViewById(R.id.textView).setBackgroundResource(R.drawable.rounded_corner_grey);
                }
            });
        }
        //remove source and destinations
        selectSource=false;
        selectDestination=false;
        //set guide fragment;
        GuideFragment guideFragment= new GuideFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, guideFragment, GUIDE_FRAGMENT_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        this.guideFragment=guideFragment;
        // reset traversal index;
        traversalIndex=0;
        //reset Graph data strcuture values
        Graph.resetGraphValues();
        //clear Path sequence and route points ArrayList
        PathFindingAlgorithms.clearAll();
    }

    @Override
    public void onResetWallButtonClick() {
        //resetFlag
        isRunning = false;
        toContinueAnimation = false;
        isPaused = false;
        isFinished = false;
        //reset GridView and Graph Data Structure
        resetTraversal();
        for (int i = 0; i < Graph.ROW; i++)
        {
            for(int j=0;j < Graph.COLUMN; j++)
            {
                if(Graph.graph[i][j]==-1)
                {
                    Graph.graph[i][j]=0;
                    final int childIndex= i*Graph.COLUMN+j;
                    gridView.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = gridView.getChildAt(childIndex).findViewById(R.id.textView);
                            tv.setBackgroundResource(R.drawable.rounded_corner_grey);
                        }
                    });
                }
            }
        }
        // reset traversal index;
        traversalIndex=0;
        controlFragment.updatePauseAndPlayButton(AnimationStatus.SET_PLAY);
        controlFragment.deactivateTraversalSeekBar();
        //clear Path sequence and route points ArrayList
        PathFindingAlgorithms.clearAll();
    }

    @Override
    public void onCloseButtonClick() {
        Log.d(TAG, "Clicked close fragment");
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Log.d("Lifecycle ", "onbackpressed");
        if(resultFragment!=null &&  resultFragment.isVisible())
        {
            Log.d(TAG, "onBackPressed");
           resetToInitialGraph();

        }
        super.onBackPressed();
    }

    @Override
    public void onSpeedButtonClick(int i) {
        sleepIndex=i;
    }

    /*@Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        savedInstanceState.putString("sgsf", "efef");
        super.onPostCreate(savedInstanceState);
    }*/
}