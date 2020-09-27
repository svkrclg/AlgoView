package com.rival.algoview.PathFinding.Algorithms;

import android.util.Pair;

import com.rival.algoview.PathFinding.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

import static com.rival.algoview.PathFinding.Graph.dy;

public class DIJKSTRA {
    private static ArrayList<Pair<Integer, Integer>> routePoints = new ArrayList<>();
    private static ArrayList<Pair<Integer, Integer>> pointTravelledSequence = new ArrayList<>();
    public static int cost=-1;
    public static int number=0;
    public static boolean found=false;
    public static void DIJKSTRA()
    {
        found=false;
        cost = -1;
        number = 0;
        clearArrayLists();
        int[][] maze = Graph.graph;
        int[] start= {Graph.sx, Graph.sy};
        int[] dest={Graph.dx, dy};
        int[][] distance = new int[maze.length][maze[0].length];
        int[][][] parent  = new int[maze.length][maze[0].length][2];
        int[][] visited = new int[maze.length][maze[0].length];

        parent[Graph.sx][Graph.sy]= new int[]{-1, -1};
        for (int[] row: distance)
            Arrays.fill(row, Integer.MAX_VALUE);
        for (int[] row: visited)
            Arrays.fill(row, 0);
        distance[start[0]][start[1]] = 0;
        System.out.println(Arrays.deepToString(maze));
        runDijkstra(maze, start, distance, parent, visited);
        //Get path
        if(found==true)
        {
            cost = distance[dest[0]][dest[1]];
            getPath(parent);
        }
        number= pointTravelledSequence.size();
    }

    public static void runDijkstra(int[][] maze , int [] start , int[][]distance, int[][][] parent, int[][] visited)
    {
        int[][] dirs={{0,1},{0,-1},{-1,0},{1,0}};
        PriorityQueue < int[] > queue = new PriorityQueue<>(100, new Comparator<int[]>() {
            @Override
            public int compare(int[] ints, int[] t1) {
                if(ints[2]>t1[2])
                    return 1;
                else if (ints[2] < t1[2])
                    return -1;
                else
                    return 0;
            }
        });
        queue.offer(new int[]{start[0], start[1], 0});
        System.out.println("Push : "+ Arrays.toString(queue.peek()));
        while(!queue.isEmpty()) {
            int[] s = queue.poll();
            System.out.println("pop : "+ Arrays.toString(s));
           if (distance[s[0]][s[1]] < s[2] ) {
                continue;
          }
            for (int[] dir : dirs)
            {
                int x = dir[0]+s[0];
                int y = dir[1]+s[1];
                if(x >= 0 && x < Graph.ROW && y>=0 && y<Graph.COLUMN && maze[x][y]!=-1) {

                    if (distance[s[0]][s[1]] != Integer.MAX_VALUE && distance[s[0]][s[1]] + 1 < distance[x][y]) {
                        parent[x][y] = new int[]{s[0], s[1]};
                        distance[x][y] = distance[s[0]][s[1]] + 1;
                        queue.offer(new int[]{x, y, distance[x][y]});
                        System.out.println("Push: " + (x) + " " + (y) + " " + distance[x][y]);
                        if( !(x == Graph.dx && y== dy))
                            pointTravelledSequence.add(new Pair<Integer, Integer>(x, y));
                        else
                        {
                            found = true;
                            return;
                        }

                    }
                }
            }
        }
    }
    public static void getPath(int[][][] parent)
    {
        Stack<Pair<Integer, Integer>> s = new Stack<>();
        int[] cur= parent[Graph.dx][dy];
        while(!(cur[0]== -1 && cur[1] ==-1))
        {
            s.push(new Pair<>(cur[0], cur[1]));
            cur= parent[cur[0]][cur[1]];
        }
        while(s.isEmpty()==false)
        {
            Pair<Integer, Integer> p = s.pop();
            if(!((p.first==Graph.dx && p.second==Graph.dy) || (p.first==Graph.sx && p.second==Graph.sy) ))
                routePoints.add(new Pair<Integer, Integer>(p.first, p.second));
        }

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
