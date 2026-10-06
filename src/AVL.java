
import java.awt.Color;

public class AVL implements Cloneable {

    private Color root;
    private int bal;
    private AVL L;
    private AVL R;

    /**
     * Create an AVL using a builder for an image
     *
     * @param img an ImagePNG object
     */
    public AVL(ImagePNG img) {
        this.root = null;
        this.bal = 0;
        this.L = null;
        this.R = null;
        this.builImg(img);
    }

    /**
     * Create an AVL using a builder for a RQuadtree
     *
     * @param quadtree an RQuadtree Object
     */
    public AVL(RQuadtree quadtree) {
        this.root = null;
        this.bal = 0;
        this.L = null;
        this.R = null;
        this.buildQuadTree(quadtree);
    }

    /**
     * Iterate over a RQuadtree to add all of its colors Only called by the
     * constructor
     * <p>
     * Complexity : O(n)
     *
     * @param quadtree an RQuadtree
     */
    private void buildQuadTree(RQuadtree quadtree) {
        if (quadtree.isLeaf()) {
            // This part contain a single color
            this.add(quadtree.getColor());
        } else {
            // Iterate over the four quadrants
            RQuadtree[] children = quadtree.getChildren();
            for (RQuadtree child : children) {
                if (child != null) {
                    this.buildQuadTree(child);
                }
            }
        }
    }

    /**
     * Iterate over an image to add all of its colors
     * <p>
     * Only called by the constructor
     * <p>
     * Complexity : O(log(n))
     *
     * @param img an ImagePNG object
     */
    private void builImg(ImagePNG img) {
        for (int x = 0; x < img.width(); x++) {
            for (int y = 0; y < img.height(); y++) {
                this.add(img.getPixel(x, y));
            }
        }
    }

    /**
     * Create a new AVL with a value as root
     * <p>
     * Complexity : θ(1)
     *
     * @param c a Color object
     */
    private AVL(Color c) {
        this.root = c;
        this.bal = 0;
        this.L = null;
        this.R = null;
    }

    /**
     * Return true if c1 < c2 <p>
     * Complexity : θ(1)
     *
     * @param c1 a Color Object
     * @param c2 a Color Object
     * @return boolean
     */
    public static boolean lowerThan(Color c1, Color c2) {
        if (c1.getRed() < c2.getRed()) {
            return true;
        }
        if (c1.getRed() == c2.getRed() && c1.getGreen() < c2.getGreen()) {
            return true;
        }
        return c1.getRed() == c2.getRed()
                && c1.getGreen() == c2.getGreen()
                && c1.getBlue() < c2.getBlue();
    }

    /**
     * Return true if c1 > c2
     * <p>
     * Complexity : θ(1)
     *
     * @param c1 a Color Object
     * @param c2 a Color Object
     * @return boolean
     */
    public static boolean greaterThan(Color c1, Color c2) {
        if (c1.getRed() > c2.getRed()) {
            return true;
        }
        if (c1.getRed() == c2.getRed() && c1.getGreen() > c2.getGreen()) {
            return true;
        }
        return c1.getRed() == c2.getRed()
                && c1.getGreen() == c2.getGreen()
                && c1.getBlue() > c2.getBlue();
    }

    /**
     * Return true if c1 == c2
     * <p>
     * Complexity : θ(1)
     *
     * @param c1 a Color Object
     * @param c2 a Color Object
     * @return boolean
     */
    public static boolean equals(Color c1, Color c2) {
        return c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue();
    }

    /**
     * Add a color to the AVL
     *
     * @param hex a String object containing the hexadecimal code of the color
     * to add
     */
    public void add(String hex) throws NumberFormatException {
        Color c = ImagePNG.hexToColor(hex);
        this.add(c);
    }

    /**
     * Add a color to the AVL
     * <p>
     * Complexity : O(log(n))
     *
     * @param color a Color Object color must not already be in the AVL
     */
    protected void add(Color color) {
        if (this.root == null) {
            this.root = color;
        } else {
            this.addHeightVar(color);
        }
    }

    /**
     * Add a color to the AVL by recursive calls
     * <p>
     * Complexity : O(log(n))
     *
     * @param color an Color Object
     * @return int the difference of heigth of the AVL
     */
    private int addHeightVar(Color color) {
        int h;
        if (lowerThan(color, this.root)) {
            // If color < this.root then it must be add to the right subtree
            if (this.L != null) {
                h = -this.L.addHeightVar(color);
            } else {
                this.L = new AVL(color);
                h = -1;
            }
        } else if (greaterThan(color, this.root)) {
            // If color > this.root then it must be add to the right subtree
            if (this.R != null) {
                h = this.R.addHeightVar(color);
            } else {
                this.R = new AVL(color);
                h = 1;
            }
        } else {
            // Else then this.root = color so we don't need to add it again
            return 0;
        }
        if (h == 0) {
            return 0;
        } else {
            // If there is a variantion in height it might need a rebalance
            this.bal += h;
            this.balance();
            return (this.bal == 0) ? 0 : 1;
        }
    }

    /**
     * Balance the AVL depending on this.bal
     * <p>
     * Complexity : θ(1)
     */
    private void balance() {
        if (this.bal == 2) {
            if (this.R.bal >= 0) {
                this.ROTL();
            } else {
                this.R.ROTR();
                this.ROTL();
            }
        } else if (this.bal == -2) {
            if (this.L.bal <= 0) {
                this.ROTR();
            } else {
                this.L.ROTL();
                this.ROTR();
            }
        }
    }

    /**
     * Make a rotation to the left to rebalance
     * <p>
     * Complexity : θ(1)
     */
    private void ROTL() {
        AVL B = this.R.clone();
        int a = this.bal;
        int b = B.bal;
        this.R = B.L;
        AVL nvThis = this.clone();
        B.L = nvThis;
        nvThis.bal = a - Math.max(b, 0) - 1;
        B.bal = Math.min(Math.min(a - 2, a + b - 2), b - 1);
        this.root = B.root;
        bal = B.bal;
        this.L = B.L;
        this.R = B.R;
    }

    /**
     * Make a rotation to the right to rebalance
     * <p>
     * Complexity : θ(1)
     */
    private void ROTR() {
        AVL B = this.L.clone();
        int a = this.bal;
        int b = B.bal;
        this.L = B.R;
        AVL nvThis = this.clone();
        B.R = nvThis;
        nvThis.bal = a - Math.min(b, 0) + 1;
        B.bal = Math.max(Math.max(a + 2, a + b + 2), b + 1);
        this.root = B.root;
        bal = B.bal;
        this.L = B.L;
        this.R = B.R;
    }

    /**
     * Remove a color
     * <p>
     * Complexity : O(log(n))
     *
     * @param hex a String object containing the hexadecimal code of the color
     */
    public void remove(String hex) throws NumberFormatException {
        Color c = ImagePNG.hexToColor(hex);
        this.removeHeightVar(c);
    }

    /**
     * Remove a color
     * <p>
     * Complexity : O(log(n))
     *
     * @param color a Color Object color must be in the AVL
     */
    private int removeHeightVar(Color color) {
        int h;
        if (lowerThan(color, this.root)) {
            // If color < this.root so it must be in this.L
            if (this.L.isLeaf()) {
                // If this.L is a leaf this means that there only color in this subtree
                this.L = null;
                h = (this.R == null) ? +1 : 0;
            } else {
                h = this.L.removeHeightVar(color);
            }
        } else if (greaterThan(color, this.root)) {
            // If color < this.root so it must be in this.R
            if (this.R.isLeaf()) {
                // If this.R is a leaf this means that there only color in this subtree
                this.R = null;
                h = (this.L == null) ? -1 : 0;
            } else {
                h = -this.R.removeHeightVar(color);
            }
        } else {
            // Else this means that this.root == color
            if (this.isLeaf()) {
                // Only possible if the hole tree (not just subtree) contain only one color
                this.root = null;
                return -1;
            } else if (this.L == null && this.R != null) {
                // If there is nothing on the left we just keep the right subtree
                this.root = this.R.root;
                this.bal = this.R.bal;
                this.L = this.R.L;
                this.R = this.R.R;
                return -1;
            } else if (this.R == null) {
                // If there is nothing on the right we just keep the left subtree
                this.root = this.L.root;
                this.bal = this.L.bal;
                this.R = this.L.R;
                this.L = this.L.L;
                return -1;
            } else {
                // Else we put the maximum of the left subtree
                // (the maximum value under this.root) as new root and
                // remove it from the left subtree
                this.root = this.L.max();
                if (this.L.isLeaf()) {
                    // Then this.L can be remove
                    this.L = null;
                    h = 1;
                }
                else h = this.L.remove_max();
            }
        }
        if (h == 0) {
            return 0;
        } else {
            // If there is a variantion in height it might need a rebalance
            this.bal += h;
            this.balance();
            return (this.bal == 0) ? -1 : 0;
        }
    }

    /**
     * Return if the root of AVL is a leaf
     * <p>
     * Complexity : θ(1)
     *
     * @return a boolean
     */
    private boolean isLeaf() {
        return this.L == null && this.R == null;
    }

    /**
     * Remove the maximum color from the AVL
     * <p>
     * Complexity : O(log(n))
     *
     * @return the height variation
     */
    private int remove_max() {
        int h;
        if (this.R == null) {
            // If this.R is null this means this.root is the maximum value
            // So we just keep the left subtree
            this.root = this.L.root;
            this.bal = this.L.bal;
            this.R = this.L.R;
            this.L = this.L.L;
            return -1;
        } else if (this.R.isLeaf()) {
            // If this.R is a leaf so we just need to remove this node
            this.R = null;
            h = (this.L == null) ? -1 : 0;
        } else {
            // Else we continue with the right subtree because it is a  Binary Search Tree
            h = this.R.remove_max();
        }
        if (h == 0) {
            return 0;
        } else {
            // If there is a variantion in height it might need a rebalance
            this.bal += h;
            this.balance();
            return (this.bal == 0) ? -1 : 0;
        }
    }

    /**
     * Return the AVL having color as root or null if the AVL does not have a
     * color as node value
     * <p>
     * Complexity : O(log(n))
     *
     * @param hex a String object containing the hexadecimal code of the color
     * @return an AVL containg color as root
     */
    public AVL search(String hex) throws NumberFormatException {
        Color c = ImagePNG.hexToColor(hex);
        return this.search(c);
    }

    /**
     * Return the AVL having color as root or null if the AVL does not have a
     * color as node value
     * <p>
     * Complexity : O(log(n))
     *
     * @param color a Color Object
     * @return an AVL containing color as root
     */
    private AVL search(Color color) {
        if (equals(this.root, color)) {
            return this;
        } else if (lowerThan(color, this.root)) {
            if (this.L != null) {
                return this.L.search(color);
            } else {
                return null;
            }
        } else {
            if (this.R != null) {
                return this.R.search(color);
            } else {
                return null;
            }
        }
    }

    /**
     * Return the max node value from the AVL
     * <p>
     * Complexity : O(log(n))
     *
     * @return a Color Object
     */
    private Color max() {
        return (this.R == null) ? this.root : this.R.max();
    }

    @Override
    /**
     * Create a String representation of tha AVL
     * <p>
     * Complexity : O(log(n))
     *
     * @return a String value
     */
    public String toString() {
        if (this.root == null) return "";
        String res = new String();
        if (this.L != null) {
            res += '(' + this.L.toString() + ')';
        }

        res += ImagePNG.colorToHex(this.root);

        if (this.R != null) {
            res += '(' + this.R.toString() + ')';
        }
        return res;
    }

    /**
     * Return the Deep-First Search representation of the AVL
     * <p>
     * Complexity : θ(1)
     *
     * @return a String value
     */
    public String dfs() {
        if (this.root == null) return "";
        String res = new String();
        if (this.L != null) {
            res += this.L.dfs();
        }

        res += ' ' + ImagePNG.colorToHex(this.root) + ' ';

        if (this.R != null) {
            res += this.R.dfs();
        }
        return res;
    }

    /**
     * Create a clone object from this
     * <p>
     * Complexity : θ(1)
     *
     * @return an AVL object
     */
    @Override
    public AVL clone() {
        AVL nv = new AVL(this.root);
        nv.bal = this.bal;
        nv.L = this.L;
        nv.R = this.R;
        return nv;
    }
}
