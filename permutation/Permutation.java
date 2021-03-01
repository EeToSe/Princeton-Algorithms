import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> permutation = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            permutation.enqueue(item);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(permutation.dequeue());
        }
    }
}
