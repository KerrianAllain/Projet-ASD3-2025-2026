import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InteractiveMode {
    private static final List<String> RQ_TREE_ACTIONS = Arrays.asList(
            "buildR", "compressPhi", "compressLambda", "saveToPNG", "saveRtoTXT");
    private static final List<String> AVL_TREE_ACTIONS = Arrays.asList(
            "avlFromImage", "avlFromR", "saveAVLToTXT", "avlAction");
    private static final String SEPARATOR = "──────────────────────";
    private static final Scanner scanner = new Scanner(System.in);
    private static final String IMAGES_DIRECTORY = "images/";

    private String latestCompressionParameter;
    private String latestCompressionMethod;
    private String cleanedFileName;
    private ImagePNG defaultImage;
    private RQuadtree rqTree;
    private String filename;
    private AVL avlTree;

    /**
     * Runs the project in interactive mode.
     */
    public void runProject() {
        this.filename = this.askForInput("Image name (images/ is automatically added): ");
        this.defaultImage = loadImage(IMAGES_DIRECTORY + this.filename);

        this.displayMenu();

        boolean keepRunning = true;
        while (keepRunning) {
            String operation = this.askForInput("\nOperation to perform: ");

            if (operation.equals("menu"))
                this.displayMenu();
            else if (RQ_TREE_ACTIONS.contains(operation))
                this.processRQTreeAction(operation);
            else if (operation.equals("compare"))
                this.compareImages();
            else if (AVL_TREE_ACTIONS.contains(operation))
                this.processAVLTreeAction(operation);
            else if (operation.equals("quit"))
                keepRunning = false;
            else
                System.out.println("This operation is not available.");
        }
    }

    /**
     * Display the query and return the user input.
     * <p>
     * <b>Complexity: O(1)</b>.
     *
     * @param query The query for the user.
     * @return The user input.
     */
    private String askForInput(String query) {
        System.out.print(query);
        return scanner.next();
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
            ImagePNG image = new ImagePNG(filename);
            System.out.printf("[SUCCESS] File %s loaded.%n", filename);
            return image;
        } catch (IOException exception) {
            System.err.printf("An IO exception occurred.\n%s\n", exception);
            System.exit(1);
            return null;
        }
    }

    /**
     * Displays the options menu to the user.
     */
    private void displayMenu() {
        System.out.println();

        System.out.printf("┌%s┬%s┐%n", SEPARATOR, SEPARATOR);
        System.out.printf("│ %-20s │ %-20s │%n", "RQ", "AVL");
        System.out.printf("├%s┼%s┤%n", SEPARATOR, SEPARATOR);

        for (int i = 0; i < Math.max(RQ_TREE_ACTIONS.size(), AVL_TREE_ACTIONS.size()); i++) {
            String avlAction = (i < AVL_TREE_ACTIONS.size()) ? AVL_TREE_ACTIONS.get(i) : "";
            String rqAction = (i < RQ_TREE_ACTIONS.size()) ? RQ_TREE_ACTIONS.get(i) : "";
            System.out.printf("│ %-20s │ %-20s │%n", rqAction, avlAction);
        }

        System.out.printf("└%s┴%s┘%n", SEPARATOR, SEPARATOR);
    }

    /**
     * Check if the tree is defined yet.
     * <p>
     * <b>Complexity: varies depending the tree to define</b>.
     *
     * @param tree The tree to determine if it is defined.
     * @return true if the tree is not null.
     */
    private void defineTree(String treeType) {
        switch (treeType) {
            case "R":
                if (this.rqTree == null)
                    this.rqTree = new RQuadtree(this.defaultImage);
                break;

            case "AVL":
                if (this.avlTree == null)
                    this.avlTree = new AVL(this.defaultImage);
                break;

            default:
                System.err.println("Invalid tree type provided.");
                System.exit(1);
        }
    }

    /**
     * Displays a success message for the given action.
     * <p>
     * <b>Complexity: O(1)</b>.
     *
     * @param action The action that was successfully performed.
     */
    private void operationSuccess(String action) {
        System.out.printf("[SUCCESS] %s%n", action);
    }

    private String formatFileName() {
        this.cleanedFileName = this.filename.replace(".png", "");
        String compM = (this.latestCompressionMethod == null) ? "" : this.latestCompressionMethod;
        String compP = (this.latestCompressionParameter == null) ? "" : this.latestCompressionParameter;
        String prefix = (compM.isEmpty() && compP.isEmpty()) ? "" : "-" + compM + compP;
        return this.cleanedFileName + prefix;
    }

    /**
     * Process an operation for a RQuadtree.
     * <p>
     * <b>Complexity: varies depending the operation to perform</b>.
     *
     * @param operation The operation to perform.
     */
    private void processRQTreeAction(String operation) {
        if (!operation.equals("buildR"))
            this.defineTree("R");

        switch (operation) {
            case "buildR":
                this.rqTree = new RQuadtree(this.defaultImage);
                this.latestCompressionParameter = null;
                this.latestCompressionMethod = null;

                this.operationSuccess("The RQuadtree is built from the source image.");
                break;

            case "compressPhi":
                String phiValue = this.askForInput("Phi value: ");
                rqTree.compressPhi(getParameterOfValue(phiValue));

                this.latestCompressionParameter = phiValue;
                this.latestCompressionMethod = "phi";

                this.operationSuccess("Phi compression terminated.");
                break;

            case "compressLambda":
                String lambdaValue = this.askForInput("Lambda value: ");
                this.rqTree.compressLambda(getParameterOfValue(lambdaValue));

                this.latestCompressionParameter = lambdaValue;
                this.latestCompressionMethod = "lambda";

                this.operationSuccess("Lambda compression terminated.");
                break;

            case "saveToPNG":
                ImagePNG rqTreeToPNG = this.rqTree.toPNG();
                File.saveToPNG(this.formatFileName(), rqTreeToPNG);
                break;

            case "saveRtoTXT":
                File.saveToTXT(this.formatFileName(), "R", this.rqTree);
                break;

            default:
                break;
        }
    }

    /**
     * Parse the string argument into an integer.
     * <p>
     * <b>Complexity: O(n)</b>, where n is the length of the argument
     *
     * @param argument The string representation of the integer.
     * @return The parsed integer.
     */
    private static int getParameterOfValue(String argument) {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            System.err.printf("Invalid parameter provided (must be an integer).\n%s\n", exception);
            System.exit(1);
            return -1;
        }
    }

    /**
     * Compare two image files with their EQM index and their size difference.
     * <p>
     * <b>Complexity: O(n)</b>.
     */
    private void compareImages() {
        String filename1 = this.askForInput("Enter first filename: ");
        ImagePNG image1 = loadImage(filename1);

        String filename2 = this.askForInput("\nEnter second filename: ");
        ImagePNG image2 = loadImage(filename2);

        double eqm = ImagePNG.computeEQM(image1, image2);
        System.out.printf("\nEQM index is of %.2f percent.\n", eqm);
        this.compareImagesSizes(filename1, filename2);
    }

    /**
     * Compare the sizes of two image files.
     * <p>
     * <b>Complexity: O(1)</b>.
     *
     * @param filename1 The path of the first image file.
     * @param filename2 The path of the second image file.
     */
    private void compareImagesSizes(String filename1, String filename2) {
        java.io.File file1 = new java.io.File(filename1);
        java.io.File file2 = new java.io.File(filename2);

        long sizeDifference = file1.length() - file2.length();
        if (sizeDifference < 0)
            System.out.printf("%s is smaller of %d bytes.", filename1, -sizeDifference);
        else if (sizeDifference > 0)
            System.out.printf("%s is smaller of %d bytes.", filename2, sizeDifference);
        else
            System.out.println("The two files have the same size.");
    }

    /**
     * Process an operation for an AVL.
     * <p>
     * <b>Complexity: varies depending the operation to perform</b>.
     *
     * @param operation The operation to perform.
     */
    private void processAVLTreeAction(String operation) {
        if (!operation.startsWith("avlFrom"))
            this.defineTree("AVL");

        switch (operation) {
            case "avlFromImage":
                this.avlTree = new AVL(this.defaultImage);

                this.operationSuccess("The AVL tree is built from the source image.");
                break;

            case "avlFromR":
                this.defineTree("R");
                this.avlTree = new AVL(this.rqTree);

                this.operationSuccess("The AVL tree is built from the RQuadtree.");
                break;

            case "saveAVLToTXT":
                File.saveToTXT(this.formatFileName(), "AVL", this.avlTree);
                break;

            case "avlAction":
                this.performAVLAction();
                break;

            default:
                break;
        }
    }

    private void performAVLAction() {
        String avlAction = this.askForInput("What action to perform on the AVL: ");
        String hexColor = this.askForInput("What is the hexadecimal color: ");

        try {
            switch (avlAction) {
                case "find":
                    AVL foundTree = avlTree.search(hexColor);
                    if (foundTree == null)
                        System.out.println("The hexadecimal color has not been found.");
                    else
                        System.out.printf("Representation of the resulting AVL:\n%s", foundTree.toString());
                    break;

                case "insert":
                    avlTree.add(hexColor);
                    this.operationSuccess("Hexadecimal color added to the AVL.");
                    break;

                case "remove":
                    avlTree.remove(hexColor);
                    this.operationSuccess("Hexadecimal color removed to the AVL.");
                    break;

                default:
                    System.out.println("Invalid AVL action.");
                    break;
            }
        } catch (NumberFormatException exception) {
            System.err.printf("An error occurred:%n%s", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            System.err.printf("Illegal argument provided: %s", exception.getMessage());
        }
    }
}
