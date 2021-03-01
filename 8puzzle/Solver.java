/******************************************************************************
 *  Compilation:  javac-algs4 Solver.java
 *  Execution:    java-algs4 Solver puzzle01.txt
 *
 * Given an initial puzzle board, try to solve it with the minimum moves
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

/**
 * The {@code Solver} class tries to solve the puzzle board. It determines if the given board if
 * solvable. If solvablle, calculate the solution and smallest number of moves taken
 * <p>
 * This implementation uses A* algorithm with aid of the priority queue
 * <p>
 * For additional documentation, see
 * <a href= "https://eetose.github.io/docs/algorithm/8puzzle/">8puzzle</a>
 *
 * @author Sheng Li
 */

public class Solver {
    private final Board inital; // inital board
    private SearchNode minNode; // // priority queue for the initial board

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("argument is null");
        }
        this.inital = initial;

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(); // priority queue of the initial board
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();

        // inital the search node
        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode minNodeTwin; // priority queue for the initial's twin board
        // delete min from pq
        minNode = pq.delMin();
        minNodeTwin = pqTwin.delMin();

        // deleted node's board is the goal?
        while (!minNode.board.isGoal() && !minNodeTwin.board.isGoal()) {

            // add neighbor to pq
            for (Board neighbor : minNode.board.neighbors()) {
                if (minNode.moves == 0 || !neighbor.equals(minNode.parent.board)) {
                    pq.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
                }
            }

            // add neighbor to pqTwin
            for (Board neighbor : minNodeTwin.board.neighbors()) {
                if (minNodeTwin.moves == 0 || !neighbor.equals(minNodeTwin.parent.board)) {
                    pqTwin.insert(new SearchNode(neighbor, minNodeTwin.moves + 1, minNodeTwin));
                }
            }

            // delete min from pq
            minNode = pq.delMin();
            minNodeTwin = pqTwin.delMin();
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (minNode.board.isGoal()) {
            return true;
        }
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return minNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solutionseq = new Stack<Board>();
        while (minNode.parent != null) {
            solutionseq.push(minNode.board);
            minNode = minNode.parent;
        }
        solutionseq.push(inital);
        return solutionseq;
    }

    /*******************************************************
     * Helper class to represent the search node in game tree
     *******************************************************/
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board; // board to move
        private final int moves; // how many moves have made
        private final int priority; // priority = moves + manhattan
        private final SearchNode parent; // previous search node

        // constructor
        public SearchNode(Board board, int moves, SearchNode parent) {
            this.board = board;
            this.moves = moves;
            this.parent = parent;
            this.priority = moves + board.manhattan();
        }

        // define the way to compare two search nodes
        public int compareTo(SearchNode that) {
            return Integer.compare(priority, that.priority);
        }
    }
}
