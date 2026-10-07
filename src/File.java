import java.io.FileWriter;
import java.io.IOException;

public class File {
    private static final String TARGET = "target/";
    private static final String TXT_FILE = ".txt";
    private static final String PNG_FILE = ".png";

    /**
     * Creates the required target directory if it does not exist.
     * <p>
     * <b>Complexity: O(1)</b>.
     *
     * @param target The path of the directory to create.
     */
    private static void createTargetDirectory(String target) {
        java.io.File targetDirectory = new java.io.File(target);
        if (!targetDirectory.exists() && !targetDirectory.mkdirs())
            throw new IllegalStateException("Unable to create directory: " + target);
    }

    /**
     * Lets the user know the operation was successful.
     * <p>
     * <b>Complexity: O(1)</b>.
     *
     * @param path The path where the file was saved.
     *
     */
    private static void operationSuccess(String path) {
        System.out.printf("%s saved successfully.%n", path);
    }

    /**
     * Saves an ImagePNG to a PNG file.
     * <p>
     * <b>Complexity: O(1)</b>.
     *
     * @param filename The file name without the extension.
     * @param image    The image to save.
     */
    public static void saveToPNG(String filename, ImagePNG image) {
        String fullTargetDirectory = TARGET + filename;
        String fullFileName = filename + PNG_FILE;

        createTargetDirectory(fullTargetDirectory);
        String path = fullTargetDirectory + "/" + fullFileName;

        try {
            image.save(path);
            operationSuccess(path);
        } catch (IOException e) {
            System.err.println("An IO exception occurred while saving the image.");
        }
    }

    /**
     * Save the string representation of an object to a text file.
     * <p>
     * <b>Complexity: O(n)</b>, where n is the length of the object's string
     * representation.
     *
     * @param filename The filename without the extension.
     * @param treeType The tree type to append to the file name.
     * @param o        The object to save.
     */
    public static void saveToTXT(String filename, String treeType, Object o) {
        String fullFileName = filename + treeType + TXT_FILE;
        String fullTargetDirectory = TARGET + filename;
        String content = o.toString();

        createTargetDirectory(fullTargetDirectory);
        String path = fullTargetDirectory + "/" + fullFileName;

        try (FileWriter fw = new FileWriter(path)) {
            fw.write(content);
            operationSuccess(path);
        } catch (IOException exception) {
            System.err.println("An IO exception occurred while saving the object.");
        }
    }
}
