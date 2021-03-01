import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int[][] tiles; // defensive copy
    private final int n; // size of board
    // private final int hamming; // cach the hamming distance
    // private final int manhattan; // cach the manhattan distance

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        // int hammingsum = 0;
        // int manhattansum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                // int target = n * i + j;
                // if (tiles[i][j] != 0 && target != tiles[i][j] - 1) {
                //     hammingsum++;
                //     int vertical = Math.abs(i - target / n);
                //     int horizontal = Math.abs(j - target % n);
                //     manhattansum += vertical + horizontal;
                // }
            }
        }
        // hamming = hammingsum;
        // manhattan = manhattansum;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        // return hamming;
        int displacment = 0;    // number of tiles in the right position
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != n * i + j + 1 && tiles[i][j] != 0) {
                    displacment++;
                }
            }
        }
        return displacment;   // total number minus the blank square
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // return manhattan;
        int sum = 0;
        int remainder = 0;
        int floor = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != n * i + j + 1 && tiles[i][j] != 0) {
                    remainder = (tiles[i][j] - 1) % n;
                    floor = (tiles[i][j] - 1 - remainder) / n;
                    sum += Math.abs(floor - i) + Math.abs(remainder - j);
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (that.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighboringBoards = new Stack<Board>();
        Board neighbor;
        int x1 = 0, y1 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    x1 = i;
                    y1 = j;
                }
            }
        }
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] direction : directions) {
            int x2 = x1 + direction[0];
            int y2 = y1 + direction[1];
            if (valid(x2, y2)) {
                neighbor = new Board(tiles);
                neighbor.exch(x1, y1, x2, y2);
                neighboringBoards.push(neighbor);
            }

        }
        return neighboringBoards;
    }

    // determine if coordinates are valid
    private boolean valid(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    // exchange two tiles
    private void exch(int x1, int y1, int x2, int y2) {
        int tmp = this.tiles[x1][y1];
        this.tiles[x1][y1] = this.tiles[x2][y2];
        this.tiles[x2][y2] = tmp;
    }

    // return a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = new Board(tiles);

        for (int i = 0; i < n * n - 1; i++) {
            int x = i / n;
            int y = i % n;
            int xx = (i + 1) / n;
            int yy = (i + 1) % n;
            if (tiles[x][y] != 0 && tiles[xx][yy] != 0) {
                twinBoard.exch(x, y, xx, yy);
                break;
            }
        }
        return twinBoard;
    }
}
