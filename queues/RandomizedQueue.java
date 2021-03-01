import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;            // number of elements on stack

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int randomNumber = StdRandom.uniform(n);
        Item item = a[randomNumber];
        for (int i = randomNumber; i < n - 1; i++) {
            a[i] = a[i + 1];
        }
        a[n - 1] = null;
        n--;
        // shrink size of array if necessary
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int randomNumber = StdRandom.uniform(n);
        return a[randomNumber];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIerator();
    }

    private class RandomizedQueueIerator implements Iterator<Item> {
        private int i;
        private final int[] randomindices; // generate randow index instead of Item[]

        public RandomizedQueueIerator() {
            i = n - 1;
            randomindices = new int[n];
            for (int j = 0; j < n; j++) {
                randomindices[j] = j;
            }
            StdRandom.shuffle(randomindices);
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[randomindices[i--]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-"))
                StdOut.print(rq.dequeue());
            else
                rq.enqueue(s);
        }
    }
}
