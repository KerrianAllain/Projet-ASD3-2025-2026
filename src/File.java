
import java.io.FileWriter;
import java.io.IOException;

public class File {
    protected static final String TARGET = "target/";
    protected static final String TXT_FILE = ".txt";
    protected static final String PNG_FILE = ".png";

    /**
     * Creates the required target directory if it does not exist.
     */
    private static void createTargetDirectory(String target) {
        java.io.File targetDirectory = new java.io.File(target);
        if (!targetDirectory.exists())
            targetDirectory.mkdirs();
    }

    /**
     * Let the user know the operation is a success.
     *
     * @param targetDirectory The directory where the file has been saved to.
     * @param filename        The filename where the content has been saved to.
     */
    private static void operationSuccess(String targetDirectory, String filename) {
        System.out.printf("%s/%s saved successfully.\n", targetDirectory, filename);
    }

    /**
     * Saves the RQuadtree to an ImagePNG file.
     *
     * @param sourceImage The source image to use for the save operation.
     * @param filename    The file name to save to.
     */
    public static void saveToPNG(String filename, ImagePNG image) {
        String fullTargetDirectory = TARGET + filename;
        String fullFileName = filename + PNG_FILE;
        createTargetDirectory(fullTargetDirectory);

        try {
            image.save(fullTargetDirectory + "/" + fullFileName);
            operationSuccess(fullTargetDirectory, fullFileName);
        } catch (IOException e) {
            System.err.println("An IO exception occurred while saving the RQuadtree image.");
        }
    }

    /**
     * Save the toString representation of an Object in a .txt file.
     *
     * @param filename The file name without the extension.
     * @param o        The Object to save.
     */
    public static void saveToTXT(String filename, String treeType, Object o) {
        String fullFileName = filename + treeType + TXT_FILE;
        String fullTargetDirectory = TARGET + filename;

        createTargetDirectory(fullTargetDirectory);
        String content = o.toString();

        try (FileWriter fw = new FileWriter(fullTargetDirectory + "/" + fullFileName)) {
            fw.write(content);
            operationSuccess(fullTargetDirectory, fullFileName);
        } catch (IOException exception) {
            System.err.println(exception);
        }
    }
}
