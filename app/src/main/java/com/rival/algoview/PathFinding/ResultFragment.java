package com.rival.algoview.PathFinding;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rival.algoview.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    View view;
    int number, cost;
    private ResultFragmentButtonClick resultFragmentButtonClick;
    public interface ResultFragmentButtonClick{
        public void onCloseButtonClick();
    }

    public ResultFragment(int cost, int number) {
        this.number=number;
        this.cost=cost;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.result_fragment_pf, container, false);
        ImageButton btnClose= view.findViewById(R.id.result_close_btn);
        TextView cost = view.findViewById(R.id.cost);
        TextView number = view.findViewById(R.id.number);
        if(this.cost==-1)
        {
            cost.setText("Unable to reach destination node");
        }
        else
        cost.setText("Destination reached with cost: "+this.cost);
        number.setText("Number of nodes travelled: " +this.number);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultFragmentButtonClick.onCloseButtonClick();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("ControlFragment", "onttach");
        resultFragmentButtonClick = (ResultFragmentButtonClick) context;
    }

}
