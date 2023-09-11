/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int gridSize;
    private int[] grid;
    private WeightedQuickUnionUF percs;
    private int openCount = 0;
    private int vTop;
    private int vBot;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "integer range below 0, grid starts from 1,1 top left");
        }

        gridSize = n;
        grid = new int[gridSize * gridSize];
        percs = new WeightedQuickUnionUF(gridSize * gridSize + 2);
        vTop = gridSize * gridSize;
        vBot = gridSize * gridSize + 1;


    }

    public void open(int row, int col) {


        int index = xyTo1D(row, col);

        if (!isOpen(row, col)) {
            openCount++;
            grid[index] = 1;
        }

        // Connect to virtual top if the first row full because anything attached to Vtop is 'Full'
        if (row == 1) {
            percs.union(index, vTop);
            grid[index] = 2;
        }

        // Connect index with virtual bottom (only Percs cares about virtual bottom)
        if (row == gridSize) {
            percs.union(index, vBot);
        }

        // look up  if we are atleast one away from the edge
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                percs.union(index, xyTo1D(row - 1, col));
                if (isFull(row - 1, col)) {
                    grid[index] = 2;
                    if (isOpen(row + 1, col)) grid[xyTo1D(row + 1, col)] = 2;
                }
            }

        }
        // look down if we are atleast one away from the edge
        if (row < gridSize) {
            if (isOpen(row + 1, col)) {
                percs.union(index, xyTo1D(row + 1, col));
                if (isFull(row + 1, col)) {
                    grid[index] = 2;
                    if (isOpen(row - 1, col)) grid[xyTo1D(row - 1, col)] = 2;
                }

            }
        }

        // look right if we are atleast one away from the edge
        if (col < gridSize) {
            if (isOpen(row, col + 1)) {
                percs.union(index, xyTo1D(row, col + 1));
                if (isFull(row, col + 1)) {
                    grid[index] = 2;
                    if (isOpen(row, col - 1)) grid[xyTo1D(row, col - 1)] = 2;
                }

            }
        }
        // look left  if we are atleast one away from the edge
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                percs.union(index, xyTo1D(row, col - 1));
                if (isFull(row, col - 1)) {
                    grid[index] = 2;
                    if (isOpen(row, col + 1)) grid[xyTo1D(row, col + 1)] = 2;
                }
            }
        }


    }


    public boolean isOpen(int row, int col) {
        isValid(row, col);
        int index = xyTo1D(row, col);
        return grid[index] == 1 || grid[index] == 2;
    }

    public boolean isFull(int row, int col) {
        isValid(row, col);
        int index = xyTo1D(row, col);
        if (grid[index] == 2) {
            return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        if (percs.find(vTop) == percs.find(vBot)) {
            return true;
        }
        return false;
    }

    private int xyTo1D(int row, int col) {
        isValid(row, col);
        int rowSetback = row - 1;
        int colSetback = col - 1;
        return (colSetback + (rowSetback * (gridSize)));
    }

    private boolean isValid(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IllegalArgumentException("Invalid: row:" + row + " col: " + col);
        }
        return true;
    }


    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 1);
        p.open(1, 2);
        System.out.println(p.percs.find(1) == p.percs.find(0));
    }
}
