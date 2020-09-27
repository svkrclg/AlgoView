package com.rival.algoview.PathFinding;

import android.util.Pair;


import java.util.ArrayList;

public class PathFindingAlgorithms {
    public static final int BFS=0;
    public static final int DFS=1;
    public static final int A_STAR=2;
    public static final int DIJKSTRA=3;
    public static int CURRENT_ALGORITHM=0;
    public static PathFindingMainActivity activityObj;
    public static void setCurrentAlogrithm(int i)
    {
        if(i!=CURRENT_ALGORITHM)
        {
            activityObj.notifyAlgorithmChange();
        }
        CURRENT_ALGORITHM=i;
    }
    public static void executePathFindingAlgorithm()
    {
        // System.out.println(Arrays.deepToString(Graph.graph));
        switch (CURRENT_ALGORITHM)
        {
            case 0: com.rival.algoview.PathFinding.Algorithms.BFS.BFS();
            break;
            case 1: com.rival.algoview.PathFinding.Algorithms.DFS.DFS();
            break;
            case 2: com.rival.algoview.PathFinding.Algorithms.ASTAR.ASTAR();
            break;
            case 3: com.rival.algoview.PathFinding.Algorithms.DIJKSTRA.DIJKSTRA();
            break;
        }
    }

    public static ArrayList<Pair<Integer, Integer>> getPointTravelledSequence()
    {

        ArrayList<Pair<Integer, Integer>> pointTravelledSequence = new ArrayList<>();
        switch (CURRENT_ALGORITHM)
           {
               case 0:
                   pointTravelledSequence = com.rival.algoview.PathFinding.Algorithms.BFS.getPointTravelledSequence();
                   break;
               case 1:
                    pointTravelledSequence = com.rival.algoview.PathFinding.Algorithms.DFS.getPointTravelledSequence();
                    break;
              case 2:
                   pointTravelledSequence = com.rival.algoview.PathFinding.Algorithms.ASTAR.getPointTravelledSequence();
                   break;
               case 3:
                  pointTravelledSequence=  com.rival.algoview.PathFinding.Algorithms.DIJKSTRA.getPointTravelledSequence();
                   break;

           }
           return pointTravelledSequence;
    }
    public static ArrayList<Pair<Integer, Integer>> getRoutePoints()
    {
        ArrayList<Pair<Integer, Integer>> routePoints = new ArrayList<>();
        switch (CURRENT_ALGORITHM)
        {
            case 0:
                routePoints = com.rival.algoview.PathFinding.Algorithms.BFS.getRoutePoints();
                break;
                case 1:
                 routePoints= com.rival.algoview.PathFinding.Algorithms.DFS.getRoutePoints();
                  break;
              case 2:
                 routePoints =  com.rival.algoview.PathFinding.Algorithms.ASTAR.getRoutePoints();
                   break;
               case 3:
                   routePoints = com.rival.algoview.PathFinding.Algorithms.DIJKSTRA.getRoutePoints();
                   break;

        }
        return routePoints;
    }
    public static int getCost()
    {
          switch (CURRENT_ALGORITHM)
          {
              case BFS: return com.rival.algoview.PathFinding.Algorithms.BFS.cost;
              case DFS: return com.rival.algoview.PathFinding.Algorithms.DFS.cost;
              case A_STAR: return com.rival.algoview.PathFinding.Algorithms.ASTAR.cost;
              case DIJKSTRA: return com.rival.algoview.PathFinding.Algorithms.DIJKSTRA.cost;
          }
          return -1;
    }
    public static int getNumber()
    {
        switch (CURRENT_ALGORITHM)
        {
            case BFS: return com.rival.algoview.PathFinding.Algorithms.BFS.number;
            case DFS: return com.rival.algoview.PathFinding.Algorithms.DFS.number;
            case A_STAR: return com.rival.algoview.PathFinding.Algorithms.ASTAR.number;
            case DIJKSTRA: return com.rival.algoview.PathFinding.Algorithms.DIJKSTRA.number;
        }
        return -1;
    }
    public static void clearAll()
    {
        com.rival.algoview.PathFinding.Algorithms.BFS.clearArrayLists();
        com.rival.algoview.PathFinding.Algorithms.DFS.clearArrayLists();
        com.rival.algoview.PathFinding.Algorithms.ASTAR.clearArrayLists();
        com.rival.algoview.PathFinding.Algorithms.DIJKSTRA.clearArrayLists();
    }


}
