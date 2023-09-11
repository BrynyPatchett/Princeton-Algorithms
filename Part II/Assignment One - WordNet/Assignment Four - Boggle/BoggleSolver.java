import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BoggleSolver {


    private final TSTCustom<Integer> trieDictionary;
    private HashMap<Integer, ArrayList<Character>> boggleList;
    private boolean[][] marked;

    public BoggleSolver(String[] dictonary) {
        trieDictionary = new TSTCustom<Integer>();

        for (int i = 0; i < dictonary.length; i++) {
            String s = dictonary[i];
            trieDictionary.put(s, calcualteScore(s));
        }


    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // does all the search work using DFS
        boggleList = new HashMap<Integer, ArrayList<Character>>();
        int rows = board.rows();
        int cols = board.cols();

        for (int y = 0; y < board.rows(); y++) {
            // for every col in the board
            for (int x = 0; x < board.cols(); x++) {
                if (isValidCoord(y + 1, x - 1, rows, cols)) {
                    int r = y + 1;
                    int c = x - 1;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }
                if (isValidCoord(y + 1, x, rows, cols)) {
                    int r = y + 1;
                    int c = x;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }
                if (isValidCoord(y + 1, x + 1, rows, cols)) {
                    int r = y + 1;
                    int c = x + 1;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }

                if (isValidCoord(y - 1, x - 1, rows, cols)) {
                    int r = y - 1;
                    int c = x - 1;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }
                if (isValidCoord(y - 1, x, rows, cols)) {
                    int r = y - 1;
                    int c = x;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }
                if (isValidCoord(y - 1, x + 1, rows, cols)) {
                    int r = y - 1;
                    int c = x + 1;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }

                if (isValidCoord(y, x - 1, rows, cols)) {
                    int r = y;
                    int c = x - 1;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }
                if (isValidCoord(y, x + 1, rows, cols)) {
                    int r = y;
                    int c = x + 1;
                    boggleList.putIfAbsent(r * rows + c, new ArrayList<Character>());
                    boggleList.get(r * rows + c).add(board.getLetter(r, c));
                }
            }
        }

        HashSet<String> wordsFound = new HashSet<String>();
        marked = new boolean[board.rows()][board.cols()];


        // for every row in the board
        for (int y = 0; y < board.rows(); y++) {
            // for every col in the board
            for (int x = 0; x < board.cols(); x++) {
                // run a DFS search for words
                for (String s : boardDFS(board, y, x)) {
                    wordsFound.add(s);
                }
            }
        }

        return wordsFound;
    }

    private int calcualteScore(String word) {
        int length = word.length();
        if (length < 3) {
            return 0;
        }
        if (length < 5) {
            return 1;
        }
        if (length < 6) {
            return 2;
        }
        if (length < 7) {
            return 3;
        }
        if (length < 8) {
            return 5;
        }
        return 11;

    }

    // might need to make recursive
    private Queue<String> boardDFS(BoggleBoard board, int originY, int originX) {
        StringBuilder sb = new StringBuilder();
        Queue<String> stringsFound = new Queue<String>();

        char c = board.getLetter(originY, originX);
        if (c == 'Q') {
            sb.append("QU");
        }
        else {
            sb.append(board.getLetter(originY, originX));
        }

        marked[originY][originX] = true;


        dfs(originY + 1, originX - 1, sb, board, stringsFound);
        dfs(originY + 1, originX, sb, board, stringsFound);
        dfs(originY + 1, originX + 1, sb, board, stringsFound);

        dfs(originY - 1, originX - 1, sb, board, stringsFound);
        dfs(originY - 1, originX, sb, board, stringsFound);
        dfs(originY - 1, originX + 1, sb, board, stringsFound);

        dfs(originY, originX - 1, sb, board, stringsFound);
        dfs(originY, originX + 1, sb, board, stringsFound);

        marked[originY][originX] = false;
        sb.deleteCharAt(sb.length() - 1);
        if (c == 'Q') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return stringsFound;
    }


    private void dfs(int y, int x, StringBuilder sb, BoggleBoard board,
                     Queue<String> stringsFound) {
        if (!isValidCoord(y, x, board.rows(), board.cols()) || marked[y][x]) {
            return;
        }
        marked[y][x] = true;


        char c = board.getLetter(y, x);
        if (c == 'Q') {
            sb.append("QU");
        }
        else {
            sb.append(board.getLetter(y, x));
        }


        // Create a faster invalidStringPrefix function
        if (invalidStringPrefix(sb.toString())) {
            sb.deleteCharAt(sb.length() - 1);
            if (sb.charAt(sb.length() - 1) == 'Q') {
                sb.deleteCharAt(sb.length() - 1);
            }
            marked[y][x] = false;
            return;
        }


        if (trieDictionary.contains(sb.toString())) {
            if (sb.length() > 2) {
                stringsFound.enqueue(sb.toString());
            }
        }

        dfs(y + 1, x - 1, sb, board, stringsFound);
        dfs(y + 1, x, sb, board, stringsFound);
        dfs(y + 1, x + 1, sb, board, stringsFound);

        dfs(y - 1, x - 1, sb, board, stringsFound);
        dfs(y - 1, x, sb, board, stringsFound);
        dfs(y - 1, x + 1, sb, board, stringsFound);

        dfs(y, x + 1, sb, board, stringsFound);
        dfs(y, x - 1, sb, board, stringsFound);

        marked[y][x] = false;
        sb.deleteCharAt(sb.length() - 1);
        if (c == 'Q') {
            sb.deleteCharAt(sb.length() - 1);
        }

    }

    public int scoreOf(String word) {
        if (trieDictionary.get(word) == null) {
            return 0;
        }
        return trieDictionary.get(word);
    }


    private boolean isValidCoord(int y, int x, int row, int col) {
        if (y < 0 || y > row - 1 || x < 0 || x > col - 1) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

    }

    private boolean invalidStringPrefix(String s) {

        return !trieDictionary.prefixExists(s);
    }
}
