import edu.princeton.cs.algs4.StdRandom;

import java.util.List;
import java.util.ArrayList;

public final class Board {
    private final int[][] tiles;
    private final int n;
    private int blankRow;
    private int blankCol;
    private int randomRow1;
    private int randomCol1;
    private int randomRow2;
    private int randomCol2;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        this.n = tiles.length;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    this.blankRow = i;
                    this.blankCol = j;
                }
            }
        }
        do {
            randomRow1 = StdRandom.uniformInt(0, this.n);
            randomCol1 = StdRandom.uniformInt(0, this.n);
            randomRow2 = StdRandom.uniformInt(0, this.n);
            randomCol2 = StdRandom.uniformInt(0, this.n);
        } while ((randomRow1 == randomRow2 && randomCol1 == randomCol2) ||
                (randomRow1 == this.blankRow && randomCol1 == this.blankCol) ||
                (randomRow2 == this.blankRow && randomCol2 == this.blankCol));
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.n);
        sb.append("\n");

        String space = " ";
        int maxLength = String.valueOf(this.n * this.n - 1).length();
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int thisTileLength = String.valueOf(this.tiles[i][j]).length();
                String paddingSpaces = new String(new char[maxLength - thisTileLength + 1]).replace("\0", space);
                sb.append(paddingSpaces);
                sb.append(this.tiles[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int numOfTilesOutOfPlace = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] == 0) {
                    continue;
                }
                if (this.tiles[i][j] != i * this.n + j + 1) {
                    numOfTilesOutOfPlace++;
                }
            }
        }
        return numOfTilesOutOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sumOfManhattanDist = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] == 0) {
                    continue;
                }
                if (this.tiles[i][j] != i * this.n + j + 1) {
                    int correctRow = (this.tiles[i][j] - 1) / this.n;
                    int correctColumn = (this.tiles[i][j] - 1) % this.n;
                    sumOfManhattanDist += Math.abs(i - correctRow) + Math.abs(j - correctColumn);
                }
            }
        }
        return sumOfManhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (!(y instanceof Board)) {
            return false;
        }
        Board yBoard = (Board) y;
        if (this.n != yBoard.n) {
            return false;
        }
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != yBoard.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // function to exchange tiles
    private Board exchangeTiles(int row1, int col1, int row2, int col2) {
        int[][] tilesOfNewBoard = new int[this.n][];
        for (int i = 0; i < this.n; i++) {
            tilesOfNewBoard[i] = this.tiles[i].clone();
        }
        int temp = tilesOfNewBoard[row1][col1];
        tilesOfNewBoard[row1][col1] = tilesOfNewBoard[row2][col2];
        tilesOfNewBoard[row2][col2] = temp;
        Board newBoard = new Board(tilesOfNewBoard);
        return newBoard;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        if (this.blankRow != 0) {
            neighbors.add(exchangeTiles(this.blankRow, this.blankCol, this.blankRow - 1, this.blankCol));
        }
        if (this.blankRow != this.n - 1) {
            neighbors.add(exchangeTiles(this.blankRow, this.blankCol, this.blankRow + 1, this.blankCol));
        }
        if (this.blankCol != 0) {
            neighbors.add(exchangeTiles(this.blankRow, this.blankCol, this.blankRow, this.blankCol - 1));
        }
        if (this.blankCol != this.n - 1) {
            neighbors.add(exchangeTiles(this.blankRow, this.blankCol, this.blankRow, this.blankCol + 1));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = exchangeTiles(this.randomRow1, this.randomCol1, this.randomRow2, this.randomCol2);
        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        // Test 1: Construct Board
        System.out.println("Test 1: Construct Board");
        int[][] tiles1 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board1 = new Board(tiles1);
        System.out.println("Board: \n" + board1.toString());

        // Test 2: dimension of the board
        System.out.println("\nTest 2: dimension");
        int[][] tiles2 = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
        Board board2 = new Board(tiles2);
        System.out.println("Dimension: " + board2.dimension());  // Expected: 3

        // Test 3: Hamming distance
        System.out.println("\nTest 3: Hamming distance");
        int[][] tiles3 = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
        Board board3 = new Board(tiles3);
        System.out.println("Hamming: " + board3.hamming());  // Expected: 2

        // Test 4: Manhattan distance
        System.out.println("\nTest 4: Manhattan distance");
        int[][] tiles4 = {{1, 2, 3}, {4, 5, 6}, {0, 7, 8}};
        Board board4 = new Board(tiles4);
        System.out.println("Manhattan: " + board4.manhattan());  // Expected: 2

        // Test 5: Check if board is goal board
        System.out.println("\nTest 5: Check if board is goal board");
        int[][] tiles5 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board5 = new Board(tiles5);
        System.out.println("Is Goal? " + board5.isGoal());  // Expected: true

        int[][] tiles6 = {{1, 2, 3}, {4, 5, 6}, {7, 0, 8}};
        Board board6 = new Board(tiles6);
        System.out.println("Is Goal? " + board6.isGoal());  // Expected: false

        // Test 6: Compare equality of two boards
        System.out.println("\nTest 6: Compare equality of two boards");
        int[][] tiles7 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board7 = new Board(tiles7);
        int[][] tiles8 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board8 = new Board(tiles8);
        System.out.println("Boards are equal? " + board7.equals(board8));  // Expected: true

        int[][] tiles9 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board9 = new Board(tiles9);
        int[][] tiles10 = {{1, 2, 3}, {4, 5, 6}, {7, 0, 8}};
        Board board10 = new Board(tiles10);
        System.out.println("Boards are equal? " + board9.equals(board10));  // Expected: false

        // Test 7: Neighbors of the board
        System.out.println("\nTest 7: Neighbors of the board");
        int[][] tiles11 = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
        Board board11 = new Board(tiles11);
        Iterable<Board> neighbors = board11.neighbors();
        for (Board neighbor : neighbors) {
            System.out.println("Neighbor: \n" + neighbor.toString());
        }

        // Test 8: Twin board
        System.out.println("\nTest 8: Twin board");
        int[][] tiles12 = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
        Board board12 = new Board(tiles12);
        Board twinBoard = board12.twin();
        System.out.println("Twin Board: \n" + twinBoard.toString());

        System.out.println("\nAll tests completed!");
    }

}
