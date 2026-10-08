public class FileComparison {
    /**
     * Compare two image files by computing their EQM index and size difference.
     *
     * <p>
     * <b>Complexity:</b> depends on {@link ImagePNG#computeEQM(ImagePNG, ImagePNG)}
     * and on the image loading operations.
     * </p>
     */
    protected static void compareImages() {
        ImageWithFilename imageData1 = Utils.getImageWithFilename("First image filename: ");
        ImageWithFilename imageData2 = Utils.getImageWithFilename("\nSecond image filename: ");
        compareImages(imageData1, imageData2);
    }

    protected static void compareImages(ImageWithFilename imageData1, ImageWithFilename imageData2) {
        double eqm = ImagePNG.computeEQM(imageData1.image(), imageData2.image());
        System.out.printf("%nEQM index is %.2f percent.%n", eqm);

        compareImagesSizes(imageData1.filename(), imageData2.filename());
    }

    /**
     * Compare the sizes of two image files.
     *
     * <p>
     * <b>Complexity: O(1)</b>.
     * </p>
     *
     * @param filename1 The first image filename.
     * @param filename2 The second image filename.
     */
    private static void compareImagesSizes(String filename1, String filename2) {
        java.io.File file1 = new java.io.File(filename1);
        java.io.File file2 = new java.io.File(filename2);

        long sizeDifference = file1.length() - file2.length();

        if (sizeDifference < 0)
            System.out.printf("%s is smaller by %d bytes.%n", filename1, -sizeDifference);
        else if (sizeDifference > 0)
            System.out.printf("%s is smaller by %d bytes.%n", filename2, sizeDifference);
        else
            System.out.println("The two files have the same size.\n");
    }
}
