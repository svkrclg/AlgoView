package com.rival.algoview.PathFinding;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rival.algoview.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GuideFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_pf, container, false);
        return view;
    }
    public void updateTextView(String text)
    {
        TextView tv = getActivity().findViewById(R.id.guide_textview);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundResource(R.drawable.rounded_corner_red);
    }
}
