import java.awt.Color;

/**
 * Enum to improve building the RQuadtree
 */
enum Safety {
    Unknown,
    Unsafe,
    Safe
}

public class RQuadtree {
    private final Point bottomRight;
    private final ImagePNG image;
    private final Point topLeft;

    private RQuadtree NW;
    private RQuadtree NE;
    private RQuadtree SE;
    private RQuadtree SW;

    private Color color;

    /**
     * Builds an RQuadtree from the given ImagePNG.
     *
     * @param image The ImagePNG to build the RQuadtree from.
     *
     * @implNote O(n²), because of the buildFromImage call.
     */
    public RQuadtree(ImagePNG image) {
        this(image, new Point(0, 0), new Point(image.width(), image.height()), Safety.Unknown);
    }

    /**
     * Builds a quadrant of the image.
     *
     * @param image            The ImagePNG to build the RQuadtree from.
     * @param topLeftPoint     Top-left coordinates (inclusive).
     * @param bottomRightPoint Bottom-right coordinates (exclusive).
     *
     * @implNote O(n²), because of the buildFromImage call.
     */
    private RQuadtree(ImagePNG image, Point topLeftPoint, Point bottomRightPoint, Safety level) {
        this.bottomRight = bottomRightPoint;
        this.topLeft = topLeftPoint;
        this.image = image;

        switch (level) {
            case Unknown:
                this.buildFromImage(image);
                break;

            case Unsafe:
                // Unsafe already tell us that we need to divide in 4 quadrants

                // Quick access to quadrant coordinates.
                int bRx = this.bottomRight.x, bRy = this.bottomRight.y;
                int tLx = this.topLeft.x, tLy = this.topLeft.y;
                int quadrantHeight = bRy - tLy;
                int quadrantWidth = bRx - tLx;
                Point middle = new Point(tLx + quadrantWidth / 2, tLy + quadrantHeight / 2);
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Unknown, Safety.Unknown, Safety.Unknown, Safety.Unknown);
                break;

            case Safe:
                // Safe means that in a Homogeneous Research we already could garanty the homogeneity of the quadrant
                // Allows not to run through this part once again
                this.color = image.getPixel(this.topLeft.x, this.topLeft.y);
                break;
        }
    }

    /**
     * Recursively build the RQuadtree from the given image.
     *
     * @param image The ImagePNG to build the RQuadtree from.
     *
     * @implNote O(n), n the number of pixels in the image.
     *           For example: a chessboard will have each square represented in the
     *           tree.
     */
    private void buildFromImage(ImagePNG image) {
        // Quick access to quadrant coordinates.
        int bRx = this.bottomRight.x, bRy = this.bottomRight.y;
        int tLx = this.topLeft.x, tLy = this.topLeft.y;
        int quadrantHeight = bRy - tLy;
        int quadrantWidth = bRx - tLx;

        // The current quadrant is either invalid or empty.
        if (quadrantWidth <= 0 || quadrantHeight <= 0)
            return;

        if (quadrantHeight == 1) {
            this.color = image.getPixel(tLx, tLy);
            return;
        }

        Point problematic = this.QuadrantHomogeneous(image);
        // The current quadrant is equivalent to a pixel size (1x1).
        if (problematic != null) {
            Point middle = new Point(tLx + quadrantWidth / 2, tLy + quadrantHeight / 2);
            // a 2x2 bit not homogeneous need to be divide in 4 (1 px is Safe)
            if (quadrantWidth == 2 && quadrantHeight == 2)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Safe, Safety.Safe, Safety.Safe, Safety.Safe);

            // Else with the (x, y) coordinates of the first problematic pixel found
            // We can determine 6 differents cases allowing to improve the complexity

            // Here we already ran through the NW quadrant entirely and found a problematic pixel in the NE quadrant (NW is safe and NE is unsafe)
            else if (problematic.y == middle.y - 1 && problematic.x >= middle.x)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Safe, Safety.Unsafe, Safety.Unknown, Safety.Unknown);
            // Here we found a problematic pixel in the NW quadrant (NW is unsafe)
            else if (problematic.y < middle.y && problematic.x < middle.x)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Unsafe, Safety.Unknown, Safety.Unknown, Safety.Unknown);
            // Here we found a problematic pixel in the NE quadrant (NE is unsafe)
            else if (problematic.y < middle.y && problematic.x >= middle.x)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Unknown, Safety.Unsafe, Safety.Unknown, Safety.Unknown);

            // Here we already ran through NW, NE and SW quadrants entirely and found a problematic pixel in the SE quadrant (NW, NE and SW are safe and SE is unsafe)
            else if (problematic.y == bRy - 1 && problematic.x >= middle.x)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Safe, Safety.Safe, Safety.Safe, Safety.Unsafe);
            // Here we already ran through NW and NE quadrants entirely and found a problematic pixel in the SW quadrant (NW and NE are safe and SW is unsafe)
            else if (problematic.y >= middle.y && problematic.x < middle.x)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Safe, Safety.Safe, Safety.Unknown, Safety.Unsafe);
            // Here we already ran trough NW and NE quadrants entirely and found a problematic pixel in the SE quadrant (NW and NE are safe and SE is unsafe)
            else if (problematic.y >= middle.y && problematic.x >= middle.x)
                this.divide(bRx, bRy, tLx, tLy, middle, Safety.Safe, Safety.Safe, Safety.Unsafe, Safety.Unknown);
            return;
        }

        this.color = image.getPixel(tLx, tLy);
    }

    /**
     * Divide an image in four quadrants
     * @param bRx the bottom right x coordinate
     * @param bRy the bottom right y coordinate
     * @param tLx the top left x coordinate
     * @param tLy the top left y coordinate
     * @param middle the middle point
     * @param snw the safety degree for NW quadrant
     * @param sne the safety degree for NE quadrant
     * @param sse the safety degree for SE quadrant
     * @param ssw the safety degree for SW quadrant
     */
    private void divide(int bRx, int bRy, int tLx, int tLy, Point middle, Safety snw, Safety sne, Safety sse, Safety ssw) {
            // Divide the current quadrant into four sub-quadrants (NW, NE, SE, SW).
            this.NW = new RQuadtree(image, this.topLeft, middle, snw);
            this.NE = new RQuadtree(image, new Point(middle.x, tLy), new Point(bRx, middle.y), sne);
            this.SE = new RQuadtree(image, middle, this.bottomRight, sse);
            this.SW = new RQuadtree(image, new Point(tLx, middle.y), new Point(middle.x, bRy), ssw);

    }

    /**
     * @param image The ImagePNG to check.
     * @return true if the quadrant is homogeneous (only contains one color).
     *
     * @implNote O(n²), check every pixel with a reference one.
     */
    private Point QuadrantHomogeneous(ImagePNG image) {
        // Reference color of the quadrant to check with.
        Color reference = image.getPixel(this.topLeft.x, this.topLeft.y);
        for (int y = this.topLeft.y; y < this.bottomRight.y; y++) {
            for (int x = this.topLeft.x; x < this.bottomRight.x; x++) {
                // The current color is different from the reference.
                if (!reference.equals(image.getPixel(x, y)))
                    return new Point(x, y);
            }
        }
        return null;
    }

    /**
     * @return The four children of the node in clockwise order.
     *
     * @implNote O(1).
     */
    protected RQuadtree[] getChildren() {
        return new RQuadtree[] { this.NW, this.NE, this.SE, this.SW };
    }

    /**
     * Recursively compress the tree according to the given luminance threshold.
     *
     * @param lambda Maximal luminance loss authorized.
     * @throws IllegalArgumentException If lambda is outside the range [0, 255].
     *
     * @implNote TODO
     */
    public void compressLambda(int lambda) {
        if (lambda < 0 || lambda > 255)
            throw new IllegalArgumentException("Lambda value must be between 0 and 255.");

        // Compress each existing child.
        for (RQuadtree child : this.getChildren())
            if (child != null)
                child.compressLambda(lambda);

        // An overleaf can be compressed.
        if (this.isOverLeaf()) {
            Color averageColor = this.averageColor();

            // The luminance threshold is respected.
            if (this.luminance(averageColor) <= lambda)
                this.processCompression(averageColor);
        }
    }

    /**
     * Recursively compress the tree according to the given number of leaves.
     *
     * @param phi The maximum number of leaves (must be a power of 4).
     *
     * @implNote TODO
     */
    public void compressPhi(int phi) {
        if (phi == 1) {
            this.compressToRoot();
            return;
        }

        if (phi < 1 || phi % 4 != 0)
            throw new IllegalArgumentException("Phi value must be positive or a power of 4.");

        if (this.color == null)
            for (RQuadtree child : this.getChildren())
                child.compressPhi(phi / 4);
    }

    /**
     * Compress the tree to a root only containing the average color
     *
     * @implNote TODO
     */
    private void compressToRoot() {
        if (this.color != null)
            return;

        for (RQuadtree child : this.getChildren())
            child.compressToRoot();

        Color averageColor = this.averageColor();
        this.processCompression(averageColor);
    }

    /**
     * @return true if all four children exist and are leaves.
     *
     * @implNote O(1).
     */
    private boolean isOverLeaf() {
        for (RQuadtree child : this.getChildren())
            if (child == null || !child.isLeaf())
                return false;
        return true;
    }

    /**
     * @return true if this node has no children.
     *
     * @implNote O(1).
     */
    protected boolean isLeaf() {
        for (RQuadtree child : this.getChildren())
            if (child != null)
                return false;
        return true;
    }

    /**
     * @return The average color of the quadrant.
     *
     * @implNote O(1).
     */
    private Color averageColor() {
        int sumRed = 0, sumGreen = 0, sumBlue = 0;
        for (RQuadtree child : this.getChildren()) {
            sumGreen += child.color.getGreen();
            sumBlue += child.color.getBlue();
            sumRed += child.color.getRed();
        }

        int childCount = this.getChildren().length;
        return new Color(sumRed / childCount, sumGreen / childCount, sumBlue / childCount);
    }

    /**
     * Compute the luminance function of a given color.
     *
     * @param color The color to compute the luminance from.
     * @return The luminance value.
     *
     * @implNote O(1).
     */
    private double luminance(Color color) {
        return 0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue();
    }

    /**
     * Prune the quadrant by removing its leaves and editing the node color to
     * the average one previously computed.
     *
     * @param color The color to assign to the quadrant.
     *
     * @implNote O(1).
     */
    private void processCompression(Color color) {
        this.color = color;
        this.NW = null;
        this.NE = null;
        this.SE = null;
        this.SW = null;
    }

    /**
     * @return The ImagePNG representation of the RQuadtree.
     *
     * @implNote TODO
     */
    public ImagePNG toPNG() {
        ImagePNG pngImage = new ImagePNG(image);
        this.buildToPNG(pngImage);
        return pngImage;
    }

    /**
     * Builds the ImagePNG representation of the RQuadtree.
     *
     * @param image The ImagePNG to build the RQuadtree on.
     *
     * @implNote TODO
     */
    private void buildToPNG(ImagePNG image) {
        if (this.color == null)
            for (RQuadtree child : this.getChildren())
                child.buildToPNG(image);
        else
            for (int x = this.topLeft.x; x < this.bottomRight.x; x++)
                for (int y = this.topLeft.y; y < this.bottomRight.y; y++)
                    image.setPixel(x, y, this.color);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.buildPath(builder);
        return builder.toString();
    }

    /**
     * Builds the path representation of the RQuadtree.
     *
     * @param builder The StringBuilder to save the path to.
     *
     * @implNote TODO
     */
    private void buildPath(StringBuilder builder) {
        if (this.color != null) {
            builder.append(ImagePNG.colorToHex(this.color));
            return;
        }

        if (this.NW == null) {
            builder.append("()");
            return;
        }

        builder.append("(");
        this.NW.buildPath(builder);
        builder.append(" ");
        this.NE.buildPath(builder);
        builder.append(" ");
        this.SE.buildPath(builder);
        builder.append(" ");
        this.SW.buildPath(builder);
        builder.append(")");
    }

    protected Color getColor() {
        return color;
    }
}
