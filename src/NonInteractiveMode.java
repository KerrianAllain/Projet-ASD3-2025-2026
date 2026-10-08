public class NonInteractiveMode {
    /**
     * Runs the project in non-interactive mode.
     *
     * @param args The command-line arguments.
     */
    protected void runProject(String[] args) {
        if (args.length != 3) {
            System.err.println("[USAGE] Non-interactive mode: <filename> <compression method> <parameter>.");
            return;
        }

        String filename = args[0];
        String method = args[1].toLowerCase();

        int parameterValue = Utils.tryParseInteger(args[2]);
        if (parameterValue < 0) {
            Utils.logError("Invalid parameter value: " + args[2]);
            return;
        }

        ImagePNG image = Utils.tryLoadImage(filename);
        if (image == null) {
            Utils.logError("Invalid filename: " + filename);
            return;
        }

        RQuadtree rqTree = new RQuadtree(image);
        if (!applyCompression(rqTree, method, parameterValue))
            return;

        ImagePNG compressedImage = rqTree.toPNG();
        String imageName = Utils.formatFilename(filename, method, String.valueOf(parameterValue));

        AVL avlTree = new AVL(rqTree);
        saveResults(imageName, compressedImage, rqTree, avlTree);
        runFileComparison(image, filename, compressedImage, imageName);
    }

    /**
     * Applies the required compression method to the RQuadtree.
     *
     * @param tree      The RQuadtree to compress.
     * @param method    The compression method to use.
     * @param parameter The parameter to provide to the compression.
     * @return {@code true} if the compression was successfully applied,
     *         {@code false} otherwise
     */
    private static boolean applyCompression(RQuadtree tree, String method, int parameter) {
        try {
            switch (method) {
                case "lambda":
                    tree.compressLambda(parameter);
                    return true;
                case "phi":
                    tree.compressPhi(parameter);
                    return true;
                default:
                    Utils.logError("Invalid compression method provided: " + method + ".\n");
                    return false;
            }
        } catch (IllegalArgumentException exception) {
            Utils.logError(exception.getMessage());
            return false;
        }
    }

    /**
     * Saves the resulting files in the target directory.
     *
     * @param imageName       The formatted filename.
     * @param compressedImage The compressed RQuadtree image.
     * @param rqTree          The RQuadtree.
     * @param avlTree         The AVL tree.
     */
    private static void saveResults(String imageName, ImagePNG compressedImage, RQuadtree rqTree, AVL avlTree) {
        File.saveToPNG(imageName, compressedImage);
        File.saveToTXT(imageName, "R", rqTree.toString());
        File.saveToTXT(imageName, "AVL", avlTree.toString());
    }

    /**
     * Runs the file comparison between the original file and the compressed file.
     *
     * @param image           The original image.
     * @param filename        The original filename.
     * @param compressedImage The compressed image.
     * @param imageName       The formatted filename.
     */
    private static void runFileComparison(ImagePNG image, String filename, ImagePNG compressedImage, String imageName) {
        ImageWithFilename imageData2 = new ImageWithFilename(compressedImage, imageName + ".png");
        ImageWithFilename imageData1 = new ImageWithFilename(image, filename);
        FileComparison.compareImages(imageData1, imageData2);
    }
}
