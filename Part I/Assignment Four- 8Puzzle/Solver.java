/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public final class Solver {

    private int moves = 0;
    private Stack<Board> boardMoves = new Stack<Board>();
    private boolean isSolvable = false;

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return boardMoves;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException("");
        }


        MinPQ<SearchNode> game = new MinPQ<SearchNode>();
        MinPQ<SearchNode> gameTwin = new MinPQ<SearchNode>();

        // Make a node from initial
        Board initialTwin = initial.twin();
        SearchNode board = new SearchNode(null, initial);
        SearchNode boardTwin = new SearchNode(null, initialTwin);

        game.insert(board);
        gameTwin.insert(boardTwin);

        boolean gameSolved = false;
        boolean twinSolved = false;

        SearchNode finalNode = null;

        while (gameSolved == false && twinSolved == false) {


            SearchNode currentBoard = game.delMin();
            if (currentBoard.gameBoard.isGoal() != true) {
                for (Board neighbour : currentBoard.gameBoard.neighbors()) {
                    if (currentBoard.previous == null) {
                        SearchNode newNode = new SearchNode(currentBoard, neighbour);
                        game.insert(newNode);
                        continue;
                    }
                    if (currentBoard.previous.gameBoard.equals(neighbour) == false) {
                        SearchNode newNode = new SearchNode(currentBoard, neighbour);
                        game.insert(newNode);
                    }
                }
            }
            else {
                finalNode = currentBoard;
                gameSolved = true;
                moves = currentBoard.moves;
                break;
            }


            SearchNode currentTwinBoard = gameTwin.delMin();
            if (currentTwinBoard.gameBoard.isGoal() != true) {
                for (Board neighbour : currentTwinBoard.gameBoard.neighbors()) {
                    if (currentTwinBoard.previous == null) {
                        SearchNode newNode = new SearchNode(currentTwinBoard, neighbour);
                        gameTwin.insert(newNode);
                        continue;
                    }
                    if (currentTwinBoard.previous.gameBoard.equals(neighbour) == false) {
                        SearchNode newNode = new SearchNode(currentTwinBoard, neighbour);
                        gameTwin.insert(newNode);
                    }
                }
            }
            else {
                twinSolved = true;
                moves = -1;
                break;
            }


        }

        if (!twinSolved) {
            while (finalNode != null) {
                boardMoves.push(finalNode.gameBoard);
                finalNode = finalNode.previous;
            }
            isSolvable = true;
        }
        else {
            isSolvable = false;
            boardMoves = null;
        }

    }


    private class SearchNode implements Comparable<SearchNode> {
        int manhattan;
        int moves;
        int priority;
        Board gameBoard;
        SearchNode previous;


        private SearchNode(SearchNode _previous, Board _board) {
            gameBoard = _board;
            if (_previous == null) {
                moves = 0;
                priority = 0;
            }
            else {
                moves = _previous.moves + 1;
                manhattan = gameBoard.manhattan();
                previous = _previous;
            }
            priority = moves + manhattan;

        }


        public int compareTo(SearchNode other) {
            return this.priority - other.priority;
        }
    }


    public static void main(String[] args) {


        int tiles[][] = {
                { 1, 0, 3 },
                { 4, 2, 5 },
                { 7, 8, 6 }
        };
        // int tiles[][] = {
        //         { 1, 2, 3 },
        //         { 4, 5, 6 },
        //         { 7, 8, 0 }
        // };


        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);

    }
}
