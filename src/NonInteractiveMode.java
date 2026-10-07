import java.io.IOException;

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
        String compressionMethod = args[1].toLowerCase();
        int parameter = getParameter(args[2]);

        ImagePNG image = loadImage(filename);
        RQuadtree rqTree = new RQuadtree(image);
        AVL avlTree = new AVL(rqTree);

        applyCompression(rqTree, compressionMethod, parameter);

        String imageFilename = formatFilename(filename, compressionMethod, parameter);

        ImagePNG pngOfRQT = rqTree.toPNG();
        File.saveToPNG(imageFilename, pngOfRQT);

        File.saveToTXT(imageFilename, "R", rqTree.toString());
        File.saveToTXT(imageFilename, "AVL", avlTree.toString());

        double eqm = ImagePNG.computeEQM(image, pngOfRQT);
        System.out.printf("\nEQM index is of %.2f percent.\n", eqm);
    }

    /**
     * Parse the string argument into an integer.
     * <p>
     * <b>Complexity: O(n)</b>, where n is the length of the argument.
     *
     * @param argument The string representation of the integer.
     * @return The parsed integer.
     */
    private static int getParameter(String argument) {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            System.err.printf("Invalid parameter provided (must be an integer).\n%s\n", exception);
            System.exit(1);
            return -1;
        }
    }

    /**
     * Loads an image from the given filename.
     * <p>
     * <b>Complexity: O(n)</b>, where n is the number of pixels in the image.
     *
     * @param filename The name of the image file to load.
     * @return The loaded image.
     */
    private static ImagePNG loadImage(String filename) {
        try {
            return new ImagePNG(filename);
        } catch (IOException exception) {
            System.err.printf("An IO exception occurred.\n%s\n", exception);
            System.exit(1);
            return null;
        }
    }

    /**
     * Applies the selected compression method to the given tree.
     * <p>
     * <b>Complexity: TODO</b>
     *
     * @param tree              The tree to compress.
     * @param compressionMethod The compression method to apply.
     * @param parameter         The parameter of the compression method.
     */
    private static void applyCompression(RQuadtree tree, String compressionMethod, int parameter) {
        switch (compressionMethod) {
            case "lambda":
                tree.compressLambda(parameter);
                break;

            case "phi":
                tree.compressPhi(parameter);
                break;

            default:
                System.err.printf("Invalid compression method provided: %s\n", compressionMethod);
                System.exit(1);
        }
    }

    /**
     * Formats a filename for the save operations.
     * <p>
     * <b>Complexity: O(n)</b>, where n is the length of the filename.
     *
     * @param filename          The original image filename.
     * @param compressionMethod The compression method used.
     * @param parameter         The parameter of the compression method.
     * @return The formatted filename.
     */
    private static String formatFilename(String filename, String compressionMethod, int parameter) {
        String filenameOnly = filename.replace("images/", "").replace(".png", "");
        return filenameOnly + "-" + compressionMethod + parameter;
    }
}
