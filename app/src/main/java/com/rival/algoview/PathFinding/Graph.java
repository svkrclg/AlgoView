package com.rival.algoview.PathFinding;

import android.util.Log;

import java.util.Arrays;

public class Graph {
    public static int ROW;
    public static int COLUMN ;
    public static int graph[][];
    public static int sx, sy, dx, dy;
    public static void initialize(int row, int col)
    {
        ROW=row;
        COLUMN=col;
        Log.d("BFS", "init");
        graph=new int[ROW][COLUMN];
        for(int[] r: graph)
        {
            Arrays.fill(r, 0);
        }
    }
    public static void setPoint(int x, int y, int value)
    {
        if(value==1)
        {
            sx= x;
            sy=y;
        }
        if(value==2)
        {
            dx= x;
            dy=y;
        }

        graph[x][y]= value;

    }
    public static void resetGraphValues()
    {
        for(int[] r :graph)
            Arrays.fill(r, 0);
    }
}
