import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;

public final class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    private final Digraph graph;

    public SAP(Digraph G) {
        // copy the digraph coming in
        if (G == null) {
            throw new IllegalArgumentException("");
        }
        graph = new Digraph(G);
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("");
        }
        return ancestorLength(v, w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // Run Two BFS for each pair, if the length in one pair is greater than another terminate early
        if (v == null || w == null) {
            throw new IllegalArgumentException("");
        }
        return ancestorLength(v, w)[0];
    }


    private int[] ancestorLength(Iterable<Integer> v, Iterable<Integer> w) {
        // Run Two BFS for each pair, if the length in one pair is greater than another terminate early
        int minLength = Integer.MAX_VALUE;
        int ansestor = -1;


        int[] ancestorAndLength = { -1, minLength };
        for (Integer iv : v) {
            for (Integer iw : w) {
                if (iv == null || iw == null) {
                    throw new IllegalArgumentException("");
                }
                minLength = ancestorAndLength[1];
                ansestor = ancestorAndLength[0];
                int[] result = lengthWithStop(iv, iw, ancestorAndLength);
                if (result[1] == -1) {
                    ancestorAndLength[1] = minLength;
                    ancestorAndLength[0] = ansestor;
                }
            }
        }
        if (ancestorAndLength[1] == Integer.MAX_VALUE) {
            ancestorAndLength[1] = -1;
            ancestorAndLength[0] = -1;
        }
        return ancestorAndLength;
    }

    // do unit testing of this class
    public static void main(String[] args) {


        In in = new In("digraph-wordnet.txt");
        Digraph graph = new Digraph(in);
        SAP g = new SAP(graph);
        ArrayList<Integer> a = new ArrayList<Integer>(
                Arrays.asList(1663, 11677, 13750, 31099, 35216, 45236, 60309, 65699, 66123, 71139,
                              76643));
        ArrayList<Integer> b = new ArrayList<Integer>(
                Arrays.asList(40021, 68450, 69042));




    }




    private int[] lengthWithStop(int v, int w, int[] ancestorWithLength) {
        if (v >= graph.V() || v < 0 || w >= graph.V() || w < 0) {
            throw new IllegalArgumentException();
        }

        // array of values to be marked the size of the digraph
        boolean[] markedV = new boolean[graph.V()];
        boolean[] markedW = new boolean[graph.V()];
        int[] distanceV = new int[graph.V()];
        int[] distanceW = new int[graph.V()];

        markedV[v] = true;
        markedW[w] = true;
        distanceV[v] = 0;
        distanceV[w] = 0;

        if (v == w) {
            // might need to change one to be the node number not zero
            ancestorWithLength[0] = v;
            ancestorWithLength[1] = 0;
            return ancestorWithLength;
        }

        Queue<Integer> vBFS = new Queue<Integer>();
        Queue<Integer> wBFS = new Queue<Integer>();

        vBFS.enqueue(v);
        wBFS.enqueue(w);

        while (!vBFS.isEmpty() || !wBFS.isEmpty()) {
            if (!vBFS.isEmpty()) {
                int vS = vBFS.dequeue();
                if (markedW[vS]) {
                    if (distanceV[vS] + distanceW[vS] < ancestorWithLength[1]
                            || ancestorWithLength[1] == -1) {
                        ancestorWithLength[1] = distanceV[vS] + distanceW[vS];
                        ancestorWithLength[0] = vS;
                    }
                   
                }
                for (int vAdj : graph.adj(vS)) {
                    if (!markedV[vAdj]) {
                        vBFS.enqueue(vAdj);
                        markedV[vAdj] = true;
                        distanceV[vAdj] = distanceV[vS] + 1;
                    }
                }


            }
            if (!wBFS.isEmpty()) {
                int wS = wBFS.dequeue();
                if (markedV[wS]) {
                    if (distanceV[wS] + distanceW[wS] < ancestorWithLength[1]
                            || ancestorWithLength[1] == -1) {
                        ancestorWithLength[1] = distanceV[wS] + distanceW[wS];
                        ancestorWithLength[0] = wS;
                    }
                   
                }
                for (int wAdj : graph.adj(wS)) {
                    if (!markedW[wAdj]) {
                        wBFS.enqueue(wAdj);
                        markedW[wAdj] = true;
                        distanceW[wAdj] = distanceW[wS] + 1;
                    }
                }
            }
        }
      
        return ancestorWithLength;
    }
}

