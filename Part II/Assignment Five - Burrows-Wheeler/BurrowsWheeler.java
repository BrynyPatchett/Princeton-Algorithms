/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    public static void transform() {

        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        int originalInSortedIndex = -1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int index = csa.index(i);
            if (index == 0) {
                originalInSortedIndex = i;
                sb.append(input.charAt(input.length() - 1));
            }
            else {
                sb.append(input.charAt(csa.index(i) - 1));
            }
        }
        BinaryStdOut.write(originalInSortedIndex);
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }

    public static void inverseTransform() {

        int originRow = BinaryStdIn.readInt();


        // while (!BinaryStdIn.isEmpty()) {
        //     char c = BinaryStdIn.readChar();
        //     sb.append(c);
        // }
        String decodedT = BinaryStdIn.readString();
        char[] t = decodedT.toCharArray();


        int N = decodedT.length();
        int[] count = new int[256 + 1];

        for (int i = 0; i < N; i++) {
            count[t[i] + 1]++;
        }

        // compute Cumulates
        for (int r = 0; r < 256; r++) {
            count[r + 1] += count[r];
        }
        int[] next = new int[t.length];
        char[] auxChar = new char[t.length];
        for (int i = 0; i < N; i++) {
            next[count[t[i]]] = i;
            auxChar[count[t[i]]++] = t[i];
        }

        for (int i = 0; i < N; i++) {
            t[i] = auxChar[i];
        }

        char[] suffixes = new char[t.length];

        for (int i = 0; i < t.length; i++) {

            suffixes[i] = t[i];
        }

        // HashMap<Character, Integer> charIndexs = new HashMap<Character, Integer>();
        // for (int i = 0; i < next.length; i++) {
        //     // if this is always zero can be one array
        //     // char startChar = suffixes[i][0];
        //     char startChar = suffixes[i];
        //     // think this needs to be optimized
        //
        //     for (int j = 0; j < t.length; j++) {
        //         if (startChar == decodedT.charAt(j)) {
        //             if (charIndexs.get(decodedT.charAt(j)) == null
        //                     || charIndexs.get(decodedT.charAt(j)) < j) {
        //                 next[i] = j;
        //                 charIndexs.put(decodedT.charAt(j), j);
        //                 break;
        //             }
        //         }
        //     }
        // }

        // sb = new StringBuilder();
        int startIndex = originRow;
        for (int c = 0; c < decodedT.length(); c++) {
            // if this is alwats 0 can be one array ?
            // char test = suffixes[startIndex][0];
            char test = suffixes[startIndex];
            BinaryStdOut.write(test);
            startIndex = next[startIndex];
        }

        // BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("");
        }
        if (args[0].equals("-")) {
            transform();
        }
        if (args[0].equals("+")) {
            inverseTransform();
        }
        return;
    }
}
