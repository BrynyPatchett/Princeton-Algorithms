/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INIT_CAPACITY = 8;
    private Item[] data;
    private int index;

    public RandomizedQueue() {
        data = (Item[]) new Object[INIT_CAPACITY];
        index = 0;
    }

    public boolean isEmpty() {
        return index == 0;
    }


    public int size() {
        return index;
    }

    // add item to queue if room else double size and add;
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item added");
        }

        if (index == data.length) {
            resize(data.length * 2);
        }
        data[index++] = item;
        // check if numElements = size if yes double size of array; and copy over to new array with modulo the origal size// add new item at end /

    }

    // remove item from queue from random position if there is items, remove item and swap with last item, check numelements, if 1/4 or something, shrink the array
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // generate random index to remove;
        int randIndex = StdRandom.uniformInt(index);
        Item outItem = data[randIndex];
        // set the data item to be removed to the data in the last index and remove the last one
        data[randIndex] = data[index - 1];
        data[index - 1] = null;
        // remove last item;
        index--;
        if (index > 0 && index == data.length / 4) {
            resize(data.length / 2);
        }
        return outItem;

    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < index; i++) {
            newArray[i] = data[i];
        }
        data = newArray;
    }

    // sample Item randomly
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int randIndex = StdRandom.uniformInt(index);
        Item outItem = data[randIndex];
        return outItem;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueItorator<Item>(data, size());
    }

    private class RandomizedQueueItorator<Item> implements Iterator<Item> {
        private Item[] randData;
        private int currenIndex;

        public RandomizedQueueItorator(Item[] data, int numElems) {
            randData = (Item[]) new Object[numElems];
            
            for (int i = 0; i < numElems; i++) {
                randData[i] = data[i];
            }
            StdRandom.shuffle(randData);
            currenIndex = 0;
        }

        public boolean hasNext() {
            return currenIndex < randData.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item itemOut = randData[currenIndex];
            currenIndex++;
            return itemOut;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {

        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        rq.enqueue(6);

        for (Integer i : rq) {
            System.out.print(i + " ");
        }
        StdOut.println(" ");
        for (Integer j : rq) {
            System.out.print(j + " ");
        }

    }

}
