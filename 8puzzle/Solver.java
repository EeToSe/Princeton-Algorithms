import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Board inital; // inital board
    private int moves; // moves used to reach the goal
    private SearchNode goal; // flag the goal
    MinPQ<SearchNode> PQ = new MinPQ<>(); // priority queue

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("argument is null");
        }
        this.inital = initial;

        if (isSolvable()) {
            // insert the inital node(root) into priority queue
            PQ.insert(new SearchNode(initial, 0, null));
            SearchNode delNode = PQ.delMin();
            // either board itself or its twin could reach goal
            while (!delNode.board.isGoal()) {
                for (Board neighbor : delNode.board.neighbors()) {
                    // exclude the neighbor equals current node's parent
                    if (delNode.parent == null || !neighbor.equals(delNode.parent.board)) {
                        PQ.insert(new SearchNode(neighbor, delNode.moves + 1, delNode));
                    }
                }
                delNode = PQ.delMin();
            }
            // reach the goal
            moves = delNode.moves;
            goal = delNode;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return inital.isSolvable();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> solutionseq = new Stack<Board>();
        SearchNode current = goal;
        while (current.parent != null) {
            solutionseq.push(current.board);
            current = current.parent;
        }
        solutionseq.push(inital);
        return solutionseq;
    }

    // define a helper data type SearchNode
    private class SearchNode implements Comparable<SearchNode> {
        private Board board; // board to move
        private int moves; // how many moves have made
        private int priority; // priority = moves + manhattan
        private int manhattan; // borad's manhattan distance
        private SearchNode parent; // previous search node

        // constructor
        public SearchNode(Board board, int moves, SearchNode parent) {
            this.board = board;
            this.moves = moves;
            this.parent = parent;
            this.manhattan = board.manhattan();
            this.priority = moves + manhattan;
        }

        // define the way to compare two search nodes
        public int compareTo(SearchNode that) {
            if (priority == that.priority) {
                return Integer.compare(manhattan, that.manhattan);
            }
            else {
                return Integer.compare(priority, that.priority);
            }
        }
    }
}

