/******************************************************************************
 *  Name:    Sheng Li
 *  NetID:   Steve
 *  Precept: P01
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data
 *                structures to determine the threshold. woot. woot.
 ******************************************************************************/

public class Percolation {
    private boolean[][] sites; // 2D sites with info blocked(0) or open(1)
    private WeightedQuickUnionUF uf; // union-find data type
    private WeightedQuickUnionUF uftop; // union-find data type
    private final int size; // size of the grid
    private int openSites; // number of open sites
    private final int topSite = 0; // index of the virtual top site
    private int bottomSite; // index of the virtual bottom site

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("grid size should be larger than 1");
        }
        this.size = n;
        this.sites = new boolean[size][size];
        // plus two virtual sites, top:[0], bottom[size*size+1]
        this.uf = new WeightedQuickUnionUF(size * size + 2);
        // plus only virtual top site used for isFull()
        this.uftop = new WeightedQuickUnionUF(size * size + 1);
        this.bottomSite = size * size + 1;
        this.openSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {

            // mark new sites as open
            sites[row][col] = true;
            openSites++;

            // connect it to all of its adjacent open sites

            // top row -> connect to topSite
            if (row == 0) {
                uf.union(shift(row, col), topSite);
                uftop.union(shift(row, col), topSite);
            }

            // bottom row -> connect to bottomSite
            if (row == size - 1) {
                uf.union(shift(row, col), bottomSite);
            }

            // check the site at bottom of itself
            if (row > 0 && isOpen(row - 1, col)) {
                uf.union(shift(row, col), shift(row - 1, col));
                uftop.union(shift(row, col), shift(row - 1, col));
            }

            // check the site on top of itself
            if (row < size - 1 && isOpen(row + 1, col)) {
                uf.union(shift(row, col), shift(row + 1, col));
                uftop.union(shift(row, col), shift(row + 1, col));
            }

            // check the site on left of itself
            if (col > 0 && isOpen(row, col - 1)) {
                uf.union(shift(row, col), shift(row, col - 1));
                uftop.union(shift(row, col), shift(row, col - 1));
            }

            // check the site on right of itself
            if (col < size - 1 && isOpen(row, col + 1)) {
                uf.union(shift(row, col), shift(row, col + 1));
                uftop.union(shift(row, col), shift(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row][col];
    }

    // is the site (row, col) full, i.e. connected to top?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return uftop.find(shift(row, col)) == uftop.find(topSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(topSite) == uf.find(bottomSite);
    }

    // Shift 2D coordinates to index
    private int shift(int row, int col) {
        return row * size + col + 1;
    }

    // within the prescribed range?
    private void validate(int row, int col) {
        // By convention, the row and column indices are integers between 1 and n
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("index out of range");
        }
    }

}
