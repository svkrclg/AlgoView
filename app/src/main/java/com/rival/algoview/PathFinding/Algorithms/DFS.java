package com.rival.algoview.PathFinding.Algorithms;

import android.util.Pair;

import com.rival.algoview.PathFinding.Graph;

import java.util.ArrayList;
import java.util.Arrays;

public class DFS {
    private static ArrayList<Pair<Integer, Integer>> routePoints = new ArrayList<>();
    private static ArrayList<Pair<Integer, Integer>> pointTravelledSequence = new ArrayList<>();
    public static int cost=-1;
    public static int number=0;
    private static final int row[] = { -1, 0, 0, 1 };
    private static final int col[] = { 0, -1, 1, 0 };
    static int visited[][];
    static Node finalNode;
    public static boolean found= false;
    static int isvalid( int x, int y){
        if(x>=0 && x< Graph.ROW && y>=0 && y< Graph.COLUMN && Graph.graph[x][y]!=-1 && visited[x][y]== 0)
            return 1;
        return 0;
    }
    public static void DFS()
    {
        found=false;
        routePoints.clear();
        pointTravelledSequence.clear();
        visited= new int[Graph.ROW][Graph.COLUMN];
        for(int [] r : visited)
            Arrays.fill(r, 0);
        dfsUtil(Graph.sx, Graph.sy, null, 0);
        if(finalNode==null)
            cost=-1;
        else
            cost=finalNode.dist;
        number=pointTravelledSequence.size();

        printPath(finalNode);
    }
    public static void dfsUtil(int x, int y, Node parent, int distance)
    {
        if(found==true)
        {
            return;
        }
        visited[x][y]=1;
        Node node= new Node (x, y, distance, parent);
        if(Graph.graph[x][y]==2)
        {
            finalNode= node;
            found=true;
            return;
        }
        if(!(x==Graph.sx && y==Graph.sy))
        {
         //   System.out.println("visted: "+ x+" "+y);
            pointTravelledSequence.add(new Pair<>(x, y));
        }
        for(int i=0; i<4;i++)
        {
            int xn = x+row[i];
            int yn =y+ col[i];
            if(isvalid(xn, yn)==1)
            {
                dfsUtil(xn, yn, node, distance+1);
            }
        }
    }
    public static void printPath(Node r)
    {
        if(r ==null)
            return;
        printPath(r.parent);
        if( !((r.i ==Graph.sx && r.j==Graph.sy) || (r.i==Graph.dx && r.j==Graph.dy)))
            routePoints.add(new Pair<Integer, Integer>(r.i, r.j));
    }
    public static ArrayList<Pair<Integer, Integer>> getRoutePoints() {
        return routePoints;
    }

    public static ArrayList<Pair<Integer, Integer>> getPointTravelledSequence() {
        return pointTravelledSequence;
    }
    public static void clearArrayLists()
    {
        pointTravelledSequence.clear();
        routePoints.clear();
    }
}
