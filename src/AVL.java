
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
        this.root = img.getPixel(0, 0);
        this.bal = 0;
        this.L = null;
        this.R = null;
        this.build(img);
    }

    /**
     * Create a clone object from this
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

    /**
     * Iterate over an image to add all of its colors Only called by the
     * constructor
     *
     * @param img an ImagePNG object
     */
    private void build(ImagePNG img) {
        for (int x = 0; x < img.width(); x++) {
            for (int y = 0; y < img.height(); y++) {
                if (this.search(img.getPixel(x, y)) == null) {
                    this.add(img.getPixel(x, y));
                }
            }
        }
    }

    /**
     * Create a new AVL with a value as root
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
     * Return true if c1 < c2
     *
     * @param c1 a Color Object
     * @param c2 a Color Object
     * @return boolean
     */
    public boolean lowerThan(Color c1, Color c2) {
        if (c1.getRed() < c2.getRed()) {
            return true;
        }
        if (c1.getRed() == c2.getRed() && c1.getGreen() < c2.getGreen()) {
            return true;
        }
        if (c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() < c2.getBlue()) {
            return true;
        }
        return false;
    }

    /**
     * Return true if c1 > c2
     *
     * @param c1 a Color Object
     * @param c2 a Color Object
     * @return boolean
     */
    public boolean greaterThan(Color c1, Color c2) {
        if (c1.getRed() > c2.getRed()) {
            return true;
        }
        if (c1.getRed() == c2.getRed() && c1.getGreen() > c2.getGreen()) {
            return true;
        }
        if (c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() > c2.getBlue()) {
            return true;
        }
        return false;
    }

    /**
     * Return true if c1 == c2
     *
     * @param c1 a Color Object
     * @param c2 a Color Object
     * @return boolean
     */
    public boolean equals(Color c1, Color c2) {
        return c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue();
    }

    /**
     * Add a color to the AVL
     *
     * @param color a Color Object color must not already be in the AVL
     */
    public void add(Color color) {
        this.addHeightVar(color);
    }

    /**
     * Add a color to the AVL by recursive calls
     *
     * @param color an Color Object
     * @return int the difference of heigth of the AVL
     */
    public int addHeightVar(Color color) {
        int h;
        if (lowerThan(color, this.root)) {
            if (this.L != null) {
                h = -this.L.addHeightVar(color);
            } else {
                this.L = new AVL(color);
                h = -1;
            }
        } else {
            if (this.R != null) {
                h = this.R.addHeightVar(color);
            } else {
                this.R = new AVL(color);
                h = 1;
            }
        }
        if (h == 0) {
            return 0;
        } else {
            this.bal += h;
            this.balance();
            return (this.bal == 0) ? 0 : 1;
        }
    }

    /**
     * Balance the AVL depending on this.bal
     */
    public void balance() {
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
     */
    public void ROTL() {
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
     */
    public void ROTR() {
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
     * Remove an color
     *
     * @param color a Color Object color must be in the AVL
     */
    public void remove(Color color) {
        if (lowerThan(this.root, color)) {
            this.L.remove(color);
        } else if (greaterThan(this.root, color)) {
            this.R.remove(color);
        } else {
            if (this.L == null) {
                AVL tmp = this.R;
                this.root = tmp.root;
                this.L = tmp.L;
                this.R = tmp.R;
            } else if (this.R == null) {
                AVL tmp = this.L;
                this.root = tmp.root;
                this.L = tmp.L;
                this.R = tmp.R;
            } else {
                Color r = this.L.max();
                this.root = r;
                this.L.remove(r);
            }
        }
    }

    /**
     * Return the AVL having color as root or null if the AVL does not have a
     * color as node value
     *
     * @param color a Color Object
     * @return an AVL
     */
    public AVL search(Color color) {
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
     *
     * @return a Color Object
     */
    public Color max() {
        return (this.R == null) ? this.root : this.R.max();
    }

    @Override
    public String toString() {
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
}
