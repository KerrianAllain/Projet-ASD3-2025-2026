
import java.io.FileWriter;
import java.io.IOException;

public class File {
    protected static final String TARGET = "target/";
    protected static final String TXT_FILE = ".txt";
    protected static final String PNG_FILE = ".png";

    /**
     * Creates the TARGET directory if it does not exist.
     */
    protected static void createTargetDirectory() {
        java.io.File targetDirectory = new java.io.File(TARGET);
        if (!targetDirectory.exists())
            targetDirectory.mkdirs();
    }

    /**
     * Save the toString representation of an Object in a .txt file.
     *
     * @param filename The file name without the extension.
     * @param o        The Object to save.
     */
    public static void saveToTxt(String filename, Object o) {
        createTargetDirectory();

        String content = o.toString();
        try (FileWriter fw = new FileWriter(TARGET + filename + TXT_FILE)) {
            fw.write(content);
        } catch (java.io.FileNotFoundException exception) {
            System.err.printf("No such file or directory: %s.", filename);
        } catch (IOException exception) {
            System.err.println(exception);
        }
    }
}
