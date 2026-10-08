import java.io.FileWriter;
import java.io.IOException;

public class File {
    private static final String PROJECT = "AllainAllain/";
    private static final String TARGET = PROJECT + "target/";

    private static final String TXT_FILE = ".txt";
    private static final String PNG_FILE = ".png";

    /**
     * Opens a PNG file with the default application.
     *
     * @param path Path to the PNG file.
     */
    protected static void open(String path) {
        if (!path.toLowerCase().endsWith(PNG_FILE)) {
            Utils.logError("The file is not a PNG file: " + path);
            return;
        }

        try {
            Process process = new ProcessBuilder("xdg-open", path).start();

            if (process.waitFor() == 0) {
                Utils.logSuccess("File " + path + " opened.");
                return;
            }

            Utils.logError("Unable to open file: " + path);
        } catch (IOException exception) {
            Utils.logError("Unable to open file: " + path);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            Utils.logError("Opening file was interrupted: " + path);
        }
    }

    /**
     * Saves an ImagePNG to a PNG file.
     *
     * <p>
     * <b>Complexity:</b> depends on {@link ImagePNG#save(String)}.
     * </p>
     *
     * @param filename The file name without the extension.
     * @param image    The image to save.
     */
    protected static void saveToPNG(String filename, ImagePNG image) {
        String path = TARGET + filename + "/" + filename + PNG_FILE;

        if (!createTargetDirectory(filename))
            return;

        try {
            image.save(path);
            Utils.logSuccess(path + " saved successfully.");
        } catch (IOException exception) {
            Utils.logError("An IO exception occurred while saving the image: " + exception.getMessage());
        }
    }

    /**
     * Save the string representation of an object to a text file.
     * <p>
     * <b>Complexity: O(n)</b>, where n is the length of the object's string
     * representation.
     * </p>
     *
     * @param filename The filename without the extension.
     * @param treeType The tree type to append to the file name.
     * @param o        The object to save.
     */
    protected static void saveToTXT(String filename, String treeType, Object o) {
        String path = TARGET + filename + "/" + filename + treeType + TXT_FILE;

        if (!createTargetDirectory(filename))
            return;

        try (FileWriter fw = new FileWriter(path)) {
            fw.write(o.toString());
            Utils.logSuccess(path + " saved successfully.\n");
        } catch (IOException exception) {
            Utils.logError("An IO exception occurred while saving the object: " + exception.getMessage());
        }
    }

    /**
     * Creates the required target directory if it does not exist.
     *
     * <p>
     * <b>Complexity: O(1)</b>.
     * </p>
     *
     * @param path The path of the directory to create.
     */
    private static boolean createTargetDirectory(String path) {
        java.io.File targetDirectory = new java.io.File(TARGET + path);

        if (targetDirectory.isDirectory() || targetDirectory.mkdirs())
            return true;

        Utils.logError("Unable to create directory: " + path);
        return false;
    }
}
