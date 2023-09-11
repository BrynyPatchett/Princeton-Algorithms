/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node<Item> {
        Item data;
        Node<Item> next;
        Node<Item> previous;
    }

    private int size;
    private Node<Item> first;
    private Node<Item> last;

    public Deque() {
        first = null;
        last = first;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item i) {
        if (i == null) {
            throw new IllegalArgumentException();
        }

        // make the node
        Node<Item> node = new Node<>();
        node.data = i;
        node.next = first;
        size++;
        if (isEmpty()) {
            first = node;
            last = first;
            return;
        }
        first.previous = node;
        node.previous = null;
        first = node;
        return;

    }

    public void addLast(Item i) {
        if (i == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> node = new Node<>();
        node.data = i;
        node.next = null;
        node.previous = last;
        size++;
        if (isEmpty()) {
            first = node;
            last = first;
            return;
        }
        last.next = node;
        last = node;
        return;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item out = first.data;
        size--;
        if (first.next != null) {
            first = first.next;
            first.previous = null;
            return out;
        }
        first = null;
        last = first;
        return out;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item out = last.data;
        size--;
        if (last.previous != null) {
            last = last.previous;
            last.next = null;
            return out;
        }
        first = null;
        last = first;
        return out;
    }


    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(first);
    }

    // Iterator that moves from front to back (queue)
    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current;


        public DequeIterator(Node<Item> first) {
            current = first;
        }


        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item data = current.data;
            current = current.next;
            return data;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    public static void main(String[] args) {

        Deque<String> myD = new Deque<String>();

        myD.addLast("1");
        myD.addLast("2");
        myD.addLast("3");
        myD.addLast("4");
        myD.addLast("5");


        for (String s : myD) {
            for (String x : myD) {
                StdOut.print(s + "-" + x + " ");
            }
            StdOut.println();
        }

    }
}
