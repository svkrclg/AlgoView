package com.rival.algoview.PathFinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rival.algoview.R;

import java.util.ArrayList;

public class CellAdapter extends ArrayAdapter {
    ArrayList birdList;

    public CellAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        birdList = objects;
    }
    public void removeItem(int position)
    {
        birdList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.cell_layout_pf, null);
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setBackgroundResource(R.drawable.rounded_corner_grey);
        return convertView;


    }

}