package com.rival.algoview.PathFinding.Algorithms;

import android.util.Log;
import android.util.Pair;

import com.rival.algoview.PathFinding.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
class Node
{
    int i, j, dist;
    Node parent;
    Node(int i , int j, int dist, Node parent)
    {
        this.i=i;
        this.j=j;
        this.parent=parent;
        this.dist=dist;
    }
    @Override
    public String toString() {
        return "{" + i + ", " + j + '}';
    }
}
public class BFS {

    private static ArrayList<Pair<Integer, Integer>> routePoints = new ArrayList<>();
    private static ArrayList<Pair<Integer, Integer>> pointTravelledSequence = new ArrayList<>();
    public static int cost=-1;
    public static int number=0;
    private static final int row[] = { -1, 0, 0, 1 };
    private static final int col[] = { 0, -1, 1, 0 };
    public static int shortestPathLength;

    static int isvalid(boolean visited[][], int x, int y){
        if(x>=0 && x< Graph.ROW && y>=0 && y< Graph.COLUMN && Graph.graph[x][y]!=-1 && visited[x][y]== false)
            return 1;
        return 0;
    }

    public static void BFS()
    {
        pointTravelledSequence.clear();
        Log.d("DFS", Graph.sx+" "+Graph.sy+ " ---- "+ Graph.dx+" "+Graph.dy);
        boolean[][] visited = new boolean[Graph.ROW][Graph.COLUMN];

        Queue<Node> q = new ArrayDeque<>();

        visited[Graph.sx][Graph.sy] = true;
        q.add(new Node(Graph.sx, Graph.sy, 0, null));

        int min_dist = Integer.MAX_VALUE;
        Node node = null;

        while (!q.isEmpty())
        {
            node = q.poll();
            int i= node.i;
            int j = node.j;
            int dist = node.dist;
            if (i == Graph.dx && j == Graph.dy)
            {
                min_dist = dist;
                break;
            }
            if(!(i==Graph.sx && j==Graph.sy))
            pointTravelledSequence.add(new Pair<>(i, j));
            for (int k = 0; k < 4; k++)
            {
                if (isvalid(visited, i + row[k], j + col[k])==1)
                {
                    visited[i + row[k]][j + col[k]] = true;
                    q.add(new Node(i + row[k], j + col[k], dist + 1, node));
                }
            }
        }
        number=pointTravelledSequence.size();
        if (min_dist != Integer.MAX_VALUE) {
            System.out.println("The shortest path from source to destination "
                    + "has length " + min_dist);

            routePoints.clear();
            cost=min_dist;
            number=pointTravelledSequence.size();
            findPath(node);
        }
        else {
            System.out.println("Destination can't be reached from source");
        }
    }
    public static void findPath(Node node)
    {
        if (node == null) {
            return;
        }
        findPath(node.parent);
     //   Log.d("DFS", node.toString());
        if( !((node.i ==Graph.sx && node.j==Graph.sy) || (node.i==Graph.dx && node.j==Graph.dy)))
        routePoints.add(new Pair<>(node.i, node.j));
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
