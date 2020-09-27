package com.rival.algoview.PathFinding.Algorithms;
import java.util.*;
import android.util.Pair;
import com.rival.algoview.PathFinding.Graph;

class point
{

    public int x, y;
    public double cost;
    public double g;
    point(int a , int b, double c ,double g) {
        x = a;
        y = b;
        cost = c;
        this.g= g;
    }

}
public class ASTAR
{
    private static ArrayList<Pair<Integer, Integer>> routePoints = new ArrayList<>();
    private static ArrayList<Pair<Integer, Integer>> pointTravelledSequence = new ArrayList<>();
    public static int cost;
    public static int number;
    public  static HashMap<String, Pair<Integer, Integer>> mp = new HashMap<>();
    public static void ASTAR()
    {
        routePoints.clear();
        pointTravelledSequence.clear();
        number=0;
        cost=-1;
        Pair<Integer, Integer> src= new Pair<>(Graph.sx,Graph.sy);
        Pair<Integer, Integer> dest= new Pair<>(Graph.dx,Graph.dy);
        int vis[][]= new int[Graph.ROW][Graph.COLUMN];
        for(int r[] : vis)
            Arrays.fill(r, 1);
        aStarSearch(Graph.graph,  vis, src, dest);
        number=pointTravelledSequence.size();
    }
    static boolean isValid(int row, int col)
    {
        // Returns true if row number and column number
        // is in range
        return (row >= 0) && (row < Graph.ROW) &&
                (col >= 0) && (col < Graph.COLUMN);
    }
    static double cost(int x1,  int y1, int x2, int y2)
    {
        return  Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
      //  return (Math.abs(x2-x1) + Math.abs(y2-y1));
    }
    static void aStarSearch(int grid[][], int vis[][], Pair<Integer, Integer> src, Pair<Integer, Integer> dest)
    {
       PriorityQueue<point> minHeap = new PriorityQueue<>(100, new Comparator<point>() {
           @Override
           public int compare(point point, point t1) {
               if(point.cost>t1.cost)
                    return 1;
               else if (point.cost < t1.cost)
                   return -1;
               else
                   return -1;
           }
       });
       minHeap.add(new point(src.first, src.second, 0.0+cost(src.first, src.second, dest.first, dest.second), 0.0));
       int r[] ={1,0,0,-1};
       int c[] ={0,1,-1,0};
       int x=1;
       vis[src.first][src.second]=0;
       while(minHeap.isEmpty()==false)
       {
           point top = minHeap.poll();
           System.out.println("Popped: "+ top.cost);
           if(top.x ==Graph.dx && top.y ==Graph.dy) {

               System.out.println("Reached: "+x);
               printPath(grid, src, dest);
               return;
           }
           for(int i=0;i<4;i++)
           {
               int newx= top.x+r[i];
               int newy= top.y+c[i];
               if(isValid(newx, newy) && (vis[newx][newy]==1) && grid[newx][newy] !=-1)
                       {
                           mp.put(newx+"^"+newy, new Pair<>(top.x, top.y)) ;
                           point obj= new point(newx, newy, top.g+cost(newx, newy, dest.first, dest.second)+1.0, top.g+1.0);
                           minHeap.add(obj);
                           System.out.println("added: "+ obj.cost);
                           if(!(newx ==dest.first && newy==dest.second))
                           pointTravelledSequence.add(new Pair<>(newx, newy));
                           x++;
                           vis[newx][newy]= 0;
                       }
           }
           System.out.println("Got it");
       }
    }
    public static void printPath(int grid[][], Pair<Integer, Integer> src , Pair<Integer, Integer> dest)
    {
        Stack<Pair<Integer, Integer>> st= new Stack<>();
        Pair<Integer, Integer> cur= dest;
        while(!(cur.first == src.first && cur.second == src.second))
        {
             st.push(cur);
             cur = mp.get(cur.first+"^"+cur.second);
        }
        int x=0;
        while(st.empty()==false)
        {
            Pair<Integer, Integer> p= st.pop();
            if( !((p.first ==Graph.sx && p.second==Graph.sy) || (p.first==Graph.dx && p.second==Graph.dy)))
            routePoints.add(new Pair<>(p.first, p.second));
            System.out.println(p.first +" "+p.second);
            x++;

        }
        cost=x;
        System.out.println("Cost: "+x);
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
