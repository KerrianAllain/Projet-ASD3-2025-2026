import java.util.Arrays;
import java.util.List;

public class InteractiveMode {
    private static final List<String> DEFAULT_ACTIONS = Arrays.asList("menu", "compare", "open", "quit");
    private static final List<String> RQ_TREE_ACTIONS = Arrays.asList(
            "buildR", "compressPhi", "compressLambda", "saveRtoPNG", "saveRtoTXT");
    private static final List<String> AVL_TREE_ACTIONS = Arrays.asList(
            "avlFromImage", "avlFromR", "saveAVLtoTXT", "avlAction");
    private static final String SEPARATOR = "────────────────";

    private String latestCompressionParameter;
    private String latestCompressionMethod;
    private ImagePNG defaultImage;
    private RQuadtree rqTree;
    private String filename;
    private AVL avlTree;

    /**
     * Runs the project in interactive mode.
     */
    public void runProject() {
        System.out.println("Running project in interactive mode.");
        this.displayMenu();

        while (true) {
            String operation = Utils.askForInput("\nOperation to perform: ");

            if (DEFAULT_ACTIONS.contains(operation))
                this.processDefaultAction(operation);
            else {
                if (RQ_TREE_ACTIONS.contains(operation))
                    this.processRQTreeAction(operation);
                else if (AVL_TREE_ACTIONS.contains(operation))
                    this.processAVLTreeAction(operation);
                else
                    Utils.logError("This operation is not available.");
            }
        }
    }

    /**
     * Displays the available operations in a formatted menu.
     *
     * <p>
     * <b>Complexity: O(n)</b>, where {@code n} is the size of the largest list.
     * </p>
     */
    private void displayMenu() {
        System.out.println();

        System.out.printf("┌%s┬%s┬%s┐%n", SEPARATOR, SEPARATOR, SEPARATOR);
        System.out.printf("│ %-14s │ %-14s │ %-14s │%n", "RQ", "AVL", "Default");
        System.out.printf("├%s┼%s┼%s┤%n", SEPARATOR, SEPARATOR, SEPARATOR);

        int numberOfRows = Math.max(RQ_TREE_ACTIONS.size(), AVL_TREE_ACTIONS.size());

        for (int i = 0; i < numberOfRows; i++) {
            String defaultAction = (i < DEFAULT_ACTIONS.size()) ? DEFAULT_ACTIONS.get(i) : "";
            String avlAction = (i < AVL_TREE_ACTIONS.size()) ? AVL_TREE_ACTIONS.get(i) : "";
            String rqAction = (i < RQ_TREE_ACTIONS.size()) ? RQ_TREE_ACTIONS.get(i) : "";
            System.out.printf("│ %-14s │ %-14s │ %-14s │%n", rqAction, avlAction, defaultAction);
        }

        System.out.printf("└%s┴%s┴%s┘%n", SEPARATOR, SEPARATOR, SEPARATOR);
    }

    /**
     * Initializes the requested tree if it has not already been created.
     *
     * <p>
     * The tree is built from the default source image.
     * If the requested tree has already been initialized, this method does nothing.
     * </p>
     *
     * <p>
     * <b>Complexity:</b> depends on the tree being initialized.
     * </p>
     *
     * @param treeType The type of tree to initialize: {@code "R"} for a RQuadtree
     *                 or {@code "AVL"} for an AVL tree.
     */
    private void initializeTree(String treeType) {
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
                throw new IllegalArgumentException("Invalid tree type: " + treeType);
        }
    }

    /**
     * Prompts the user to load the default source image if not defined yet.
     */
    private void loadImageIfNeeded() {
        if (this.defaultImage != null)
            return;

        ImageWithFilename imageData = Utils.getImageWithFilename("Source image filename: ");
        this.defaultImage = imageData.image();
        this.filename = imageData.filename();
    }

    private void processDefaultAction(String operation) {
        switch (operation) {
            case "menu":
                this.displayMenu();
                break;

            case "compare":
                FileComparison.compareImages();
                break;

            case "open":
                String path = Utils.askForInput("Image filename to open: ");
                File.open(path);
                break;

            case "quit":
                Utils.logSuccess("Quitting project.");
                System.exit(0);
        }
    }

    /**
     * Process an operation on the RQuadtree.
     *
     * <p>
     * <b>Complexity:</b> depends on the operation being performed.
     * </p>
     *
     * @param operation The operation to perform.
     */
    private void processRQTreeAction(String operation) {
        loadImageIfNeeded();

        if (!"buildR".equals(operation))
            this.initializeTree("R");

        switch (operation) {
            case "buildR":
                this.rqTree = new RQuadtree(this.defaultImage);
                this.latestCompressionParameter = null;
                this.latestCompressionMethod = null;

                Utils.logSuccess("The RQuadtree is built from the source image.");
                break;

            case "compressPhi":
                int phiParameter = Utils.getParameter("Phi value: ");
                rqTree.compressPhi(phiParameter);

                this.latestCompressionParameter = Integer.toString(phiParameter);
                this.latestCompressionMethod = "phi";

                Utils.logSuccess("Phi compression terminated.");
                break;

            case "compressLambda":
                int lambdaParameter = Utils.getParameter("Lambda value: ");
                this.rqTree.compressLambda(lambdaParameter);

                this.latestCompressionParameter = Integer.toString(lambdaParameter);
                this.latestCompressionMethod = "lambda";

                Utils.logSuccess("Lambda compression terminated.");
                break;

            case "saveRtoPNG":
                ImagePNG rqTreeToPNG = this.rqTree.toPNG();
                File.saveToPNG(Utils.formatFilename(
                        this.filename,
                        this.latestCompressionMethod,
                        this.latestCompressionParameter),
                        rqTreeToPNG);
                break;

            case "saveRtoTXT":
                File.saveToTXT(Utils.formatFilename(
                        this.filename,
                        this.latestCompressionMethod,
                        this.latestCompressionParameter),
                        "R", this.rqTree);
                break;

            default:
                throw new IllegalArgumentException("Unknown RQuadtree operation: " + operation);
        }
    }

    /**
     * Process an operation on the AVL tree.
     *
     * <p>
     * <b>Complexity:</b> depends on the operation being performed.
     * </p>
     *
     * @param operation The operation to perform.
     */
    private void processAVLTreeAction(String operation) {
        loadImageIfNeeded();

        if (!operation.startsWith("avlFrom"))
            this.initializeTree("AVL");

        switch (operation) {
            case "avlFromImage":
                this.avlTree = new AVL(this.defaultImage);

                Utils.logSuccess("The AVL tree is built from the source image.");
                break;

            case "avlFromR":
                this.initializeTree("R");
                this.avlTree = new AVL(this.rqTree);

                Utils.logSuccess("The AVL tree is built from the RQuadtree.");
                break;

            case "saveAVLtoTXT":
                File.saveToTXT(Utils.formatFilename(
                        this.filename,
                        this.latestCompressionMethod,
                        this.latestCompressionParameter),
                        "AVL", this.avlTree);
                break;

            case "avlAction":
                this.performAVLAction();
                break;

            default:
                throw new IllegalArgumentException("Unknown RQuadtree operation: " + operation);
        }
    }

    /**
     * Performs an action on the current AVL tree.
     */
    private void performAVLAction() {
        String avlAction = Utils.askForInput("What action to perform on the AVL: ");
        String hexColor = Utils.askForInput("What is the hexadecimal color: ");

        try {
            switch (avlAction) {
                case "find":
                    AVL foundTree = this.avlTree.search(hexColor);

                    if (foundTree == null)
                        Utils.logError("The hexadecimal color has not been found.");
                    else
                        Utils.logSuccess("Representation of the resulting AVL:\n" + foundTree.toString());
                    break;

                case "insert":
                    avlTree.add(hexColor);

                    Utils.logSuccess("Hexadecimal color added to the AVL.");
                    break;

                case "remove":
                    avlTree.remove(hexColor);

                    Utils.logSuccess("Hexadecimal color removed to the AVL.");
                    break;

                default:
                    Utils.logError("Invalid AVL action.");
                    break;
            }
        } catch (NumberFormatException exception) {
            Utils.logError("An error occurred:\n" + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            Utils.logError("Illegal argument provided: " + exception.getMessage());
        }
    }
}
