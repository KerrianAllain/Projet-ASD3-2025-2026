
import java.awt.Color;

public class RQuadtree {
    private final Point bottomRight;
    private final ImagePNG image;
    private final Point topLeft;
    private RQuadtree NO;
    private RQuadtree NE;
    private RQuadtree SE;
    private RQuadtree SO;
    private Node node;

    /**
     * Default public constructor building the RQuadtree from a given ImagePNG.
     *
     * @param image The ImagePNG to use to build the RQuadtree.
     */
    public RQuadtree(ImagePNG image) {
        this(image, new Point(0, 0), new Point(image.width(), image.height()));
    }

    /**
     * Private constructor to define the image,
     * and the coordinates of the current quadrant.
     *
     * @param image            The ImagePNG to build from.
     * @param topLeftPoint     Top-left coordinates (inclusive).
     * @param bottomRightPoint Bottom-right coordinates (exclusive).
     */
    private RQuadtree(ImagePNG image, Point topLeftPoint, Point bottomRightPoint) {
        this.bottomRight = bottomRightPoint;
        this.topLeft = topLeftPoint;
        this.image = image;
        this.node = null;
        this.NO = null;
        this.NE = null;
        this.SE = null;
        this.SO = null;

        this.buildFromImage();
    }

    private void buildFromImage() {
        // Quick access to quadrant coordinates.
        int bRx = this.bottomRight.x, bRy = this.bottomRight.y;
        int tLx = this.topLeft.x, tLy = this.topLeft.y;
        int quadrantHeight = bRy - tLy;
        int quadrantWidth = bRx - tLx;

        // The current quadrant is either invalid or empty.
        if (quadrantWidth <= 0 || quadrantHeight <= 0)
            return;

        // The current quadrant is equivalent to a pixel size (1x1).
        if (this.isQuadrantHomogeneous()) {
            this.node = new Node(new Point(tLx, tLy), this.image.getPixel(tLx, tLy));
            return;
        }

        // Divide the current quadrant into four sub-quadrants (NO, NE, SE, SO).
        Point middle = new Point(tLx + quadrantWidth / 2, tLy + quadrantHeight / 2);
        this.NO = new RQuadtree(this.image, new Point(tLx, tLy), new Point(middle.x, middle.y));
        this.NE = new RQuadtree(this.image, new Point(middle.x, tLy), new Point(bRx, middle.y));
        this.SO = new RQuadtree(this.image, new Point(tLx, middle.y), new Point(middle.x, bRy));
        this.SE = new RQuadtree(this.image, new Point(middle.x, middle.y), new Point(bRx, bRy));
    }

    private boolean isQuadrantHomogeneous() {
        // First color of the quadrant to check with.
        Color firstColor = this.image.getPixel(this.topLeft.x, this.topLeft.y);
        for (int x = this.topLeft.x; x < this.bottomRight.x; x++) {
            for (int y = this.topLeft.y; y < this.bottomRight.y; y++) {
                // The current color is different from the first color seen.
                if (!firstColor.equals(this.image.getPixel(x, y)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.buildPath(builder);
        return builder.toString();
    }

    private void buildPath(StringBuilder builder) {
        if (this.node != null) {
            builder.append(ImagePNG.colorToHex(this.node.color));
            return;
        }

        if (this.NO == null) {
            builder.append("()");
            return;
        }

        builder.append("(");
        this.NO.buildPath(builder);
        builder.append(" ");
        this.NE.buildPath(builder);
        builder.append(" ");
        this.SE.buildPath(builder);
        builder.append(" ");
        this.SO.buildPath(builder);
        builder.append(")");
    }
}
