/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class MoveToFront {


    public static void encode() {
        ArrayList<Character> asciiChars = new ArrayList<Character>();
        for (char c = 0; c < 256; c++) {
            asciiChars.add(c);
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = asciiChars.indexOf(c);
            asciiChars.remove(index);
            asciiChars.add(0, c);
            BinaryStdOut.write((char) index);
        }
        BinaryStdOut.flush();

    }

    public static void decode() {
        ArrayList<Character> asciiChars = new ArrayList<Character>();
        for (char c = 0; c < 256; c++) {
            asciiChars.add(c);
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = asciiChars.get(index);
            asciiChars.remove(index);
            asciiChars.add(0, c);
            BinaryStdOut.write(c);

        }
        BinaryStdOut.flush();
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("");
        }
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
        return;

    }
}
