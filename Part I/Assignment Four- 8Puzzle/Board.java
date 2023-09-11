/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 *****************************************************************************/

import edu.princeton.cs.algs4.Queue;

public final class Board {

    private final int boardTiles[];
    private final int nSize;
    private boolean isGoal;
    private int hamming;
    private int manhattan;

    public Board(int[][] tiles) {
        // create n x n bored from passed in tiles.

        nSize = tiles.length;
        boardTiles = new int[nSize * nSize];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                boardTiles[flattenIndex(j, i)] = tiles[i][j];
            }
        }

        // calcualte hamming and if is goal;
        hamming = 0;
        manhattan = 0;
        isGoal = true;

        // for even element execpt the last (should be a 0)
        for (int i = 0; i < nSize * nSize; i++) {

            if (boardTiles[i] != i + 1 && boardTiles[i] != 0) {
                hamming++;
                isGoal = false;
                // Do manhattan math here;

                int y1 = (i / nSize);
                int x1 = (i % nSize);

                int y2 = ((boardTiles[i] - 1) / nSize);
                int x2 = ((boardTiles[i] - 1) % nSize);

                int man = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                manhattan += man;

            }
        }

    }

    private int flattenIndex(int x, int y) {

        if (y >= 0 && y < nSize && x >= 0 && x < nSize) {
            return y * nSize + x;
        }
        throw new ArrayIndexOutOfBoundsException();
    }


    public String toString() {
        String out = "";
        out += nSize;
        for (int i = 0; i < nSize; i++) {
            out += "\n";
            for (int j = 0; j < nSize; j++) {
                out += (" " + boardTiles[flattenIndex(j, i)]);
            }
        }
        out += "\n";
        return out;

    }

    public int dimension() {
        return nSize;
    }

    public int hamming() {

        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public Iterable<Board> neighbors() {

        Queue<Board> neighbours = new Queue<Board>();

        int zeroLocationX = 0;
        int zeroLocationY = 0;
        for (int i = 0; i < boardTiles.length; i++) {

            if (boardTiles[i] == 0) {
                zeroLocationY = i / nSize;
                zeroLocationX = i % nSize;
            }
        }

        int flatIndex = flattenIndex(zeroLocationX, zeroLocationY);
        // swap up
        if (zeroLocationY - 1 >= 0) {
            neighbours.enqueue(
                    copyAndSwap(flatIndex, flattenIndex(zeroLocationX, zeroLocationY - 1)));
        }


        // swap down
        if (zeroLocationY + 1 < nSize) {
            neighbours.enqueue(
                    copyAndSwap(flatIndex, flattenIndex(zeroLocationX, zeroLocationY + 1)));
        }
        // swap left

        if (zeroLocationX - 1 >= 0) {
            neighbours.enqueue(
                    copyAndSwap(flatIndex, flattenIndex(zeroLocationX - 1, zeroLocationY)));
        }

        // swap right

        if (zeroLocationX + 1 < nSize) {
            neighbours.enqueue(
                    copyAndSwap(flatIndex, flattenIndex(zeroLocationX + 1, zeroLocationY)));
        }

        return neighbours;
    }


    private Board copyAndSwap(int firstSwapIndex, int secondSwapIndex) {

        int newgrid[][] = new int[nSize][nSize];
        int tilesCopy[] = new int[nSize * nSize];

        for (int i = 0; i < boardTiles.length; i++) {
            tilesCopy[i] = boardTiles[i];
        }

        tilesCopy[firstSwapIndex] = boardTiles[secondSwapIndex];
        tilesCopy[secondSwapIndex] = boardTiles[firstSwapIndex];

        for (int j = 0; j < tilesCopy.length; j++) {
            newgrid[j / nSize][j % nSize] = tilesCopy[j];
        }

        return new Board(newgrid);

    }


    public Board twin() {

        int firstSwapIndex = 0;
        int secondSwapIndex = 0;

        for (int i = 0; i < boardTiles.length - 1; i++) {
            if ((i + 1) % nSize != 0 && boardTiles[i] != 0 && boardTiles[(i + 1)] != 0) {
                firstSwapIndex = i;
                secondSwapIndex = i + 1;
                break;
            }
        }

        Board twin = copyAndSwap(firstSwapIndex, secondSwapIndex);

        return twin;
    }


    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;

        if (that.dimension() != this.dimension()) return false;
        if (that.isGoal != this.isGoal()) return false;
        if (that.hamming() != this.hamming()) return false;
        if (that.manhattan() != this.manhattan()) return false;

        for (int i = 0; i < boardTiles.length; i++) {
            if (this.boardTiles[i] != that.boardTiles[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        // int array[][] = {
        //         { 1, 2, 3 },
        //         { 4, 5, 6 },
        //         { 7, 8, 0 }
        // };

        // int array[][] = {
        //         { 8, 1, 3 },
        //         { 4, 0, 2 },
        //         { 7, 6, 5 }
        // };

        // int array[][] = {
        //         { 0, 1, 2 },
        //         { 3, 4, 5 },
        //         { 6, 7, 8 }
        // };

        // int array[][] = {
        //         { 6, 1, 3 },
        //         { 4, 2, 5 },
        //         { 7, 8, 0 }
        // };

        int array[][] = {
                { 1, 0, 3 },
                { 4, 2, 5 },
                { 7, 8, 6 }
        };

        Board b = new Board(array);
        System.out.println(b.toString());
        System.out.println(b.isGoal());


        System.out.println("Twin: ");
        System.out.println(b.twin());


        System.out.println("Neighbours: ");


        Iterable<Board> n = b.neighbors();

        for (Board nb : n) {
            System.out.println(nb.toString());
        }

    }
}
