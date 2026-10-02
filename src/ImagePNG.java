import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePNG {
    private final BufferedImage image; // The bitmap

    /**
     * Default constructor.
     *
     * @param filename The image file to load.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     *                     This class is the general class of exceptions produced by
     *                     failed or interrupted I/O operations.
     */
    public ImagePNG(String filename) throws IOException {
        File file = new File(filename);
        this.image = ImageIO.read(file);
    }

    /**
     * Copy constructor.
     *
     * @param png The ImagePNG to copy.
     */
    public ImagePNG(ImagePNG png) {
        ColorModel cm = png.image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = png.image.copyData(null);
        this.image = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Clone method.
     *
     * @return The cloned image.
     */
    @Override
    public ImagePNG clone() throws CloneNotSupportedException {
        try {
            return (ImagePNG) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Save an ImagePNG to a file.
     *
     * @param filename The file's name to save to.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     *                     This class is the general class of exceptions produced by
     *                     failed or interrupted I/O operations.
     */
    public void save(String filename) throws IOException {
        File file = new File(filename);
        ImageIO.write(image, "png", file);
    }

    /**
     * Get the number of pixels in X dimension.
     *
     * @return The width of the image.
     */
    public int width() {
        return image.getWidth();
    }

    /**
     * Get the number of pixels in Y dimension.
     *
     * @return The height of the image.
     */
    public int height() {
        return image.getHeight();
    }

    /**
     * Get the Color of the matching Pixel coordinates.
     *
     * @param x The coordinate in X dimension.
     * @param y The coordinate in Y dimension.
     * @return The Color associated to the coordinates.
     */
    public Color getPixel(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    /**
     * Set the Color of the matching Pixel coordinates.
     *
     * @param x     The coordinate in X dimension.
     * @param y     The coordinate in Y dimension.
     * @param color The Color to assign to the coordinates.
     */
    public void setPixel(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    /**
     * Static function to convert a color to an hexadecimal code.
     *
     * @param color The color to convert.
     * @return The hexadecimal code value of the color.
     */
    public static String colorToHex(Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }

    /**
     * Static function to convert an hexadecimal code to a color.
     *
     * @param hex The hexadecimal code to convert.
     * @return The Color of the hexadecimal code value.
     */
    public static Color hexToColor(String hex) {
        return new Color(Integer.parseUnsignedInt(hex, 16));
    }

    /**
     * Static function to compute the EQM index between a reference and its copy.
     *
     * @param reference The ImagePNG reference.
     * @param copy      The ImagePNG copy.
     * @return The computed index.
     *
     * @implNote https://fr.wikipedia.org/wiki/Peak_Signal_to_Noise_Ratio
     */
    public static double computeEQM(ImagePNG reference, ImagePNG copy) {
        double height = reference.height();
        double width = reference.width();
        double eqm = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color referenceColor = reference.getPixel(x, y);
                double referenceGreen = referenceColor.getGreen() / 255.0;
                double referenceBlue = referenceColor.getBlue() / 255.0;
                double referenceRed = referenceColor.getRed() / 255.0;

                Color copyColor = copy.getPixel(x, y);
                double copyRed = copyColor.getRed() / 255.0;
                double copyBlue = copyColor.getBlue() / 255.0;
                double copyGreen = copyColor.getGreen() / 255.0;

                double computation = Math.pow(referenceRed - copyRed, 2)
                        + Math.pow(referenceGreen - copyGreen, 2)
                        + Math.pow(referenceBlue - copyBlue, 2);
                eqm += computation / (3 * height * width);
            }
        }

        // Round the percentage to the hundredth
        return Math.ceil(10000 * (1 - eqm)) / 100.0;
    }
}
