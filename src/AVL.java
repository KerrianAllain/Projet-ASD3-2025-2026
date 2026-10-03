import java.awt.Color;

public class AVL implements Cloneable {
    private Color root;
    private int bal;
    private AVL L;
    private AVL R;

    public AVL(ImagePNG img) {
        this.root = img.getPixel(0, 0);
        this.bal = 0;
        this.L = null;
        this.R = null;
        this.build(img);
    }

    @Override
    public AVL clone() {
        try {
            return (AVL) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    private void build(ImagePNG img) {
        for (int x = 0; x < img.width(); x++) {
            for (int y= 0; y < img.height(); y++) {
                if (this.search(img.getPixel(x, y)) == null) {
                    this.add(img.getPixel(x, y));
                }
            }
        }
    }

    private AVL(Color c) {
        this.root = c;
        this.bal = 0;
        this.L = null;
        this.R = null;
    }

    public boolean lowerThan(Color c1, Color c2) {
        if (c1.getRed() < c2.getRed()) return true;
        if (c1.getRed() == c2.getRed() && c1.getGreen() < c2.getGreen()) return true;
        if (c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() < c2.getBlue()) return true;
        return false;
    }

    public boolean greaterThan(Color c1, Color c2) {
        if (c1.getRed() > c2.getRed()) return true;
        if (c1.getRed() == c2.getRed() && c1.getGreen() > c2.getGreen()) return true;
        if (c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() > c2.getBlue()) return true;
        return false;
    }

    public boolean equals(Color c1, Color c2) {
        return c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue();
    }

    public void add(Color color) {
        int _ = this.add_height(color);
    }

    public int add_height(Color color) {
        int h;
        if (lowerThan(color, this.root)) {
            if (this.L != null) {
                h = - this.L.add_height(color);
            } else {
                this.L = new AVL(color);
                h = -1;
            }
        } else {
            if (this.R != null) {
                h = this.R.add_height(color);
            } else {
                this.R = new AVL(color);
                h = 1;
            }
        }
        if (h == 0) return 0;
        else {
            this.bal += h;
            this.balance();
            return (this.bal == 0) ? 0 : 1;
        }
    }

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

    public void ROTR() {
        AVL B = this.L.clone();
        int a = this.bal;
        int b = B.bal;
        this.L = B.R;
        AVL nvThis = this.clone();
        B.R = nvThis;
        this.bal = a - Math.max(b, 0) + 1;
        B.bal = Math.min(Math.min(a + 2, a + b + 2), b + 1);
        this.root = B.root;
        bal = B.bal;
        this.L = B.L;
        this.R = B.R;
    }

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
