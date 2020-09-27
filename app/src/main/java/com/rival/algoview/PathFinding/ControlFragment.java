package com.rival.algoview.PathFinding;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rival.algoview.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ControlFragment extends Fragment {
    View view;
    ImageButton btnPauseAndPlay, btnAlgorithm, btnNextStep, btnResetGraph;
    SeekBar traversealProgressSeekBar;
    String[] speedValues ={".25", ".5", "1", "2", "4"};
    int speedCurrentIndex =2;
    TextView tvSpeed;
    ImageButton btnResetWall;
    private ControlPanelButtonClicks controlPanelButtonClicks;
    private String[] pathFindingAlgorithms={"Breath First Search", "Depth First Search", "A-star", "Dijkstra's Alogrithm"};
    public interface ControlPanelButtonClicks{
        public void onPlayPauseClick();
        public void traversalSeekBarValueUpdate(int i, boolean b);
        public void onNextStepButtonClick();
        public void onResetGraphButtonClick();
        public void onSpeedButtonClick(int i);
        public void onResetWallButtonClick();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_controller_pf, container, false);
        btnPauseAndPlay = view.findViewById(R.id.pause_and_play);
        btnAlgorithm = view.findViewById(R.id.algo);
        tvSpeed= view.findViewById(R.id.speed);
        tvSpeed.setText(speedValues[speedCurrentIndex]+"x");
        tvSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedCurrentIndex++;
                speedCurrentIndex%=5;
                tvSpeed.setText(speedValues[speedCurrentIndex]+"x");
                controlPanelButtonClicks.onSpeedButtonClick(speedCurrentIndex);
            }
        });
        btnResetWall= view.findViewById(R.id.reset_wall);
        btnResetWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlPanelButtonClicks.onResetWallButtonClick();
            }
        });
        switch (PathFindingAlgorithms.CURRENT_ALGORITHM)
        {
            case PathFindingAlgorithms.BFS : btnAlgorithm.setImageResource(R.drawable.icon_bfs);
                break;
            case PathFindingAlgorithms.DFS : btnAlgorithm.setImageResource(R.drawable.icon_dfs);
                break;
            case PathFindingAlgorithms.A_STAR : btnAlgorithm.setImageResource(R.drawable.icon_a_star);
                break;
            case PathFindingAlgorithms.DIJKSTRA : btnAlgorithm.setImageResource(R.drawable.icon_dijkstra);
                break;
        }
        traversealProgressSeekBar= view.findViewById(R.id.traversal_progress_seekbar);
        traversealProgressSeekBar.setVisibility(View.INVISIBLE);
        btnNextStep = view.findViewById(R.id.next_step);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlPanelButtonClicks.onNextStepButtonClick();
            }
        });
        btnResetGraph = view.findViewById(R.id.reset_graph);
        btnResetGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlPanelButtonClicks.onResetGraphButtonClick();
            }
        });
        traversealProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b==true)
                controlPanelButtonClicks.traversalSeekBarValueUpdate(i, b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Your Choice");

                builder.setSingleChoiceItems(pathFindingAlgorithms, PathFindingAlgorithms.CURRENT_ALGORITHM, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        PathFindingAlgorithms.setCurrentAlogrithm(item);
                        switch (item)
                        {
                            case PathFindingAlgorithms.BFS : btnAlgorithm.setImageResource(R.drawable.icon_bfs);
                                                             break;
                            case PathFindingAlgorithms.DFS : btnAlgorithm.setImageResource(R.drawable.icon_dfs);
                                break;
                            case PathFindingAlgorithms.A_STAR : btnAlgorithm.setImageResource(R.drawable.icon_a_star);
                                break;
                            case PathFindingAlgorithms.DIJKSTRA : btnAlgorithm.setImageResource(R.drawable.icon_dijkstra);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                 alertDialog= builder.create();
                alertDialog.show();

            }
        });
        btnPauseAndPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlPanelButtonClicks.onPlayPauseClick();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("ControlFragment", "onttach");
        controlPanelButtonClicks = (ControlPanelButtonClicks) context;
    }
    public void updatePauseAndPlayButton(int i)
    {
         switch (i)
         {
             case AnimationStatus.SET_PAUSE: btnPauseAndPlay.setBackgroundResource(R.drawable.round_red);
                                             btnPauseAndPlay.setImageResource(R.drawable.pause_image_button  );
             break;
             case AnimationStatus.SET_PLAY: btnPauseAndPlay.setImageResource(R.drawable.play_image_button);
                                            btnPauseAndPlay.setBackgroundResource(R.drawable.round_green);
             break;
             case AnimationStatus.SET_PLAY_AGAIN: btnPauseAndPlay.setImageResource(R.drawable.play_image_button);
                                                  btnPauseAndPlay.setBackgroundResource(R.drawable.round_green);
             break;
         }
    }
    public void activateTraversalSeekBar(int max)
    {
        traversealProgressSeekBar.setVisibility(View.VISIBLE);
        traversealProgressSeekBar.setMax(max);
    }
    public void deactivateTraversalSeekBar()
    {
        traversealProgressSeekBar.setVisibility(View.INVISIBLE);
    }
    public void updateSeekbarValue(final int i)
    {
                traversealProgressSeekBar.setProgress(i);
    }
    public int getSeekBarValue()
    {
        return  traversealProgressSeekBar.getProgress();
    }
    public void disableNextStepButton(boolean b)
    {
        if(b)
        {
            btnNextStep.setImageAlpha(100);
            btnNextStep.setEnabled(false);
        }
        else
        {
            btnNextStep.setImageAlpha(255);
            btnNextStep.setEnabled(true);
        }
    }
    public int getCurrentTraversalSpeed()
    {
        return  speedCurrentIndex;
    }
}
