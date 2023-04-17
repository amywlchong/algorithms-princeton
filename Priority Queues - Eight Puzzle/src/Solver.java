import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solver {

    private List<Board> boardsInSolutionPath;

    // We define a search node of the game to be a board,
    // the number of moves made to reach the board, and the previous search node.

    // Caching the Hamming and Manhattan priorities.
    // To avoid recomputing the Manhattan priority of a search node
    // from scratch each time during various priority queue operations,
    // pre-compute its value when we construct the search node;
    // save it in an instance variable; and return the saved value as needed.

    private static final class SearchNode {
        private final Board board;
        private final int numOfMovesMade;
        private final SearchNode prevSearchNode;
        private final int manhattanPriority;

        public SearchNode(Board board, int numOfMovesMade, SearchNode prevSearchNode) {
            this.board = board;
            this.numOfMovesMade = numOfMovesMade;
            this.prevSearchNode = prevSearchNode;
            this.manhattanPriority = board.manhattan() + numOfMovesMade;
        }

        public Board getBoard() {
            return this.board;
        }

        public int getNumMoves() {
            return this.numOfMovesMade;
        }

        public SearchNode getPrevNode() {
            return this.prevSearchNode;
        }

        public int getManhattanPriority() {
            return this.manhattanPriority;
        }
    }

    private static class ByManhattanPriority implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode a, SearchNode b) {
            return a.getManhattanPriority() - b.getManhattanPriority();
        }
    }

    // Find a solution to the initial board (using the A* search algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        this.boardsInSolutionPath = new ArrayList<>();

        MinPQ<SearchNode> pq = new MinPQ<>(new ByManhattanPriority());
        pq.insert(new SearchNode(initial, 0, null));
        MinPQ<SearchNode> pqTwin = new MinPQ<>(new ByManhattanPriority());
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode latestDequeuedNode = null, latestDequeuedNodeTwin = null;

        do {
            latestDequeuedNode = deleteAndInsertOntoPQ(pq);
            latestDequeuedNodeTwin = deleteAndInsertOntoPQ(pqTwin);
        } while (!latestDequeuedNode.getBoard().isGoal() && !latestDequeuedNodeTwin.getBoard().isGoal());

        if (latestDequeuedNode.getBoard().isGoal()) {
            this.boardsInSolutionPath.add(latestDequeuedNode.getBoard());
            while (latestDequeuedNode.getPrevNode() != null) {
                this.boardsInSolutionPath.add(latestDequeuedNode.getPrevNode().getBoard());
                latestDequeuedNode = latestDequeuedNode.getPrevNode();
            }
            Collections.reverse(this.boardsInSolutionPath);
        }
    }

    // Implementation of deleting from the priority queue
    // the search node with the minimum priority,
    // and inserting onto the priority queue all neighboring search nodes
    private SearchNode deleteAndInsertOntoPQ(MinPQ<SearchNode> pq) {

        SearchNode latestDequeuedNode = pq.delMin();

        for (Board neighboringBoard : latestDequeuedNode.getBoard().neighbors()) {
            boolean isDuplicatedBoard = false;
            if (latestDequeuedNode.getPrevNode() != null) {
                isDuplicatedBoard = neighboringBoard.equals(latestDequeuedNode.getPrevNode().getBoard());
            }
            if (!isDuplicatedBoard) {
                pq.insert(new SearchNode(neighboringBoard, latestDequeuedNode.getNumMoves() + 1, latestDequeuedNode));
            }
        }
        return latestDequeuedNode;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return !this.boardsInSolutionPath.isEmpty();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.boardsInSolutionPath.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.isSolvable()) {
            return null;
        }
        return this.boardsInSolutionPath;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
