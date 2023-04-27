import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

// Shortest ancestral path.
// An ancestral path between two vertices v and w in a digraph
// is a directed path from v to a common ancestor x,
// together with a directed path from w to the same ancestor x.
// A shortest ancestral path is an ancestral path of minimum total length.
// We refer to the common ancestor in a shortest ancestral path as a shortest common ancestor.

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkNotNull(G);
        this.G = new Digraph(G);
    }

    private Map<String, Integer> findShortestAncestralPath(Iterable<Integer> v, Iterable<Integer> w) {

        Map<String, Integer> shortestAncestralPath = new HashMap<>();
        shortestAncestralPath.put("shortestCommonAncestor", -1);
        shortestAncestralPath.put("shortestDistance", -1);

        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return shortestAncestralPath;
        }

        // Performs breadth-first search on graph G from all vertices in v and w respectively.
        // This gives us shortest paths from any vertex in v (and separately, w) to every other vertex in the graph,
        // if such paths exist.
        BreadthFirstDirectedPaths bfsPathsFromV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsPathsFromW = new BreadthFirstDirectedPaths(G, w);
        int shortestDistBetweenVandW = Integer.MAX_VALUE;
        int shortestCommonAncestor = -1;

        for (int vertex = 0; vertex < G.V(); vertex++) {

            // If there exists a path from any vertex in v to the current vertex and
            // a path from any vertex in w to the current vertex
            if (bfsPathsFromV.hasPathTo(vertex) && bfsPathsFromW.hasPathTo(vertex)) {
                int sumOfDists = bfsPathsFromV.distTo(vertex) + bfsPathsFromW.distTo(vertex);
                if (sumOfDists < shortestDistBetweenVandW) {
                    shortestDistBetweenVandW = sumOfDists;
                    shortestCommonAncestor = vertex;
                }
            }
        }

        if (shortestCommonAncestor != -1) {
            shortestAncestralPath.put("shortestCommonAncestor", shortestCommonAncestor);
            shortestAncestralPath.put("shortestDistance", shortestDistBetweenVandW);
        }

        return shortestAncestralPath;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkNotNull(v, w);
        validateVertices(v, w);
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkNotNull(v, w);
        validateVertices(v, w);
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkNotNull(v, w);
        checkNotContainNull(v);
        checkNotContainNull(w);
        validateVertexCollection(v);
        validateVertexCollection(w);

        Map<String, Integer> shortestAncestralPath = findShortestAncestralPath(v, w);
        return shortestAncestralPath.get("shortestDistance");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkNotNull(v, w);
        checkNotContainNull(v);
        checkNotContainNull(w);
        validateVertexCollection(v);
        validateVertexCollection(w);

        Map<String, Integer> shortestAncestralPath = findShortestAncestralPath(v, w);
        return shortestAncestralPath.get("shortestCommonAncestor");
    }

    private void checkNotNull(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Arguments cannot be null");
            }
        }
    }

    private void checkNotContainNull(Iterable<Integer> arg) {
        for (Integer value : arg) {
            if (value == null) {
                throw new IllegalArgumentException("Iterable arguments must not contain a null item");
            }
        }
    }

    private void validateVertices(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("Arguments must be valid vertices in the graph.");
        }
    }

    private void validateVertexCollection(Iterable<Integer> v) {
        for (Integer vertex : v) {
            if (vertex < 0 || vertex >= G.V()) {
                throw new IllegalArgumentException("Iterable arguments must contain valid vertices in the graph");
            }
        }
    }

    // test client
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            System.out.println("length = " + length + " ancestor = " + ancestor);
        }
    }
}
