/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class CircularSuffixArray {

    private final CircularSuffix[] suffixArray;
    private final int stringLength;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Null Argument");
        }
        stringLength = s.length();
        suffixArray = new CircularSuffix[stringLength];

        for (int i = 0; i < stringLength; i++) {
            suffixArray[i] = new CircularSuffix(s, i);
        }

        // then sort the suffixArray
        Arrays.sort(suffixArray);

    }

    public int length() {
        return stringLength;
    }

    public int index(int i) {
        if (i >= stringLength || i < 0) {
            throw new IllegalArgumentException("");
        }
        return suffixArray[i].startIndex;
    }

    private void printUnsortedSuffix() {
        for (int i = 0; i < suffixArray.length; i++) {
            System.out.println(suffixArray[i].toString() + " i: " + suffixArray[i].getStartIndex());
        }
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        int startIndex;
        String suffix;

        private CircularSuffix(String s, int index) {
            suffix = s;
            startIndex = index;
        }

        private int getStartIndex() {
            return startIndex;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < suffix.length(); i++) {
                int sufIndex = (i + startIndex) % suffix.length();
                sb.append(suffix.charAt(sufIndex));
            }
            return sb.toString();
        }

        public int compareTo(CircularSuffix that) {
            // assuming all suffixs are same length and string?
            int thatOffset = that.getStartIndex();

            for (int i = 0; i < suffix.length(); i++) {
                int thisSufIndex = (i + startIndex) % suffix.length();
                int thatSufIndex = (i + thatOffset) % suffix.length();
                if (suffix.charAt(thisSufIndex) < that.suffix.charAt(thatSufIndex)) {
                    return -1;
                }
                else if (suffix.charAt(thisSufIndex) > that.suffix.charAt(thatSufIndex)) {
                    return 1;
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("WEEKEND");
        csa.printUnsortedSuffix();
        System.out.println(csa.index(0));

    }
}
