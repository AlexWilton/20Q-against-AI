package ajw28.cs3105.p02.part2.EulerOnTour;

/*************************************************************************
 *  Compilation:  javac DirectedEulerianCycle.java
 *  Dependencies: Digraph.java Stack.java Queue.java StdOut.java
 *
 *  Find an Eulerian cycle in a digraph, if one exists.
 *
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.Stack;

public class DirectedEulerianCycle {
    private Stack<Integer> cycle = new Stack<Integer>();
    private boolean isEulerian = true;

    // find Eulerian cycle
    public DirectedEulerianCycle(Digraph G) {

        // create local view of adjacency lists
        Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.V()];
        for (int v = 0; v < G.V(); v++)
            adj[v] = G.adj(v).iterator();

        // find vertex with nonzero degree as start of potential Eulerian cycle
        int s = 0;
        for (int v = 0; v < G.V(); v++) {
            if (adj[v].hasNext()) {
                s = v;
                break;
            }
        }

        // greedily add to cycle, depth-first search style
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            cycle.push(v);
            int w = v;
            while (adj[w].hasNext()) {
                stack.push(w);
                w = adj[w].next();
            }
//            if (w != v) isEulerian = false;
        }

        // check if all edges have been used
        for (int v = 0; v < G.V(); v++)
            if (adj[v].hasNext()) isEulerian = false;
    }

    // return Eulerian cycle, or null if no such tour
    public Iterable<Integer> cycle() {
        if (!isEulerian) return null;
        return cycle;
    }

    // does the digraph have an Eulerian tour?
    public boolean isEulerian() {
        return isEulerian;
    }



    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);

        // random graph of V vertices and approximately E edges
        // with indegree[v] = outdegree[v] for all vertices
        Digraph G = new Digraph(V);
        int[] indegree  = new int[V];
        int[] outdegree = new int[V];
        int deficit = 0;
        for (int i = 0; i < E - deficit/2; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            // if (v == w) { i--; continue; }
            G.addEdge(v, w);
            if (outdegree[v] >= indegree[v]) deficit++;
            else                             deficit--;
            outdegree[v]++;
            if (indegree[w] >= outdegree[w]) deficit++;
            else                             deficit--;
            indegree[w]++;
        }

        while (deficit > 0) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            if (v == w) continue;
            if (outdegree[v] >= indegree[v])  continue;
            if (indegree[w]  >= outdegree[w]) continue;
            G.addEdge(v, w);
            if (outdegree[v] >= indegree[v]) deficit++;
            else                             deficit--;
            outdegree[v]++;
            if (indegree[w] >= outdegree[w]) deficit++;
            else                             deficit--;
            indegree[w]++;
        }

        StdOut.println(G);
        DirectedEulerianCycle euler = new DirectedEulerianCycle(G);
        if (euler.isEulerian()) {
            for (int v : euler.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.println("Not eulerian");
        }
    }

}