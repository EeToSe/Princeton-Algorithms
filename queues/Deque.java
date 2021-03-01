import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n; // number of elements on queue
    private Node first; // beginning of queue
    private Node last; // end of queue


    // helper linked list class
    private class Node {
        private Item item;   // the item in the node
        private Node next;   // reference to next item
        private Node prev;  // reference to previous item
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (isEmpty()) last = first;
        else oldFirst.prev = first;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) {
          // to avoid loitering
          last = null;
        }
        else
          first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        last = last.prev;
        n--;
        if (isEmpty()) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove()
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;  // node containing current item

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
      Deque<Integer> deque = new Deque<Integer>();
      deque.addFirst(1);
      StdOut.println(deque.removeFirst());
      // StdOut.println(deque.isEmpty());
      // deque.addFirst(2);
      // deque.addFirst(3);
      // deque.addFirst(4);
      // deque.addFirst(5);
      // deque.addFirst(6);
      // deque.addFirst(7);
      // deque.addFirst(8);
      // StdOut.println("add 2-8");
      // for (int i:deque)
      //   StdOut.print(i);
      // StdOut.println("removeLast");
      // StdOut.println(deque.removeLast());
      // for (int i:deque)
      //   StdOut.print(i);
      // StdOut.println("removeLast");
      // StdOut.println(deque.removeLast());
      // for (int i:deque)
      //   StdOut.print(i);
      // StdOut.println("removeLast");
      // StdOut.println(deque.removeLast());
    }
}
