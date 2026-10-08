import java.io.IOException;
import java.util.Scanner;

public class Utils {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String GREEN = "\033[32m";
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[31m";

    /**
     * Prompts the user with the given query and returns the next token entered.
     *
     * <p>
     * <b>Complexity: O(n)</b>, where {@code n} is the length of the input token.
     * </p>
     *
     * @param query The message displayed to the user.
     * @return The user's input.
     */
    protected static String askForInput(String query) {
        System.out.print(query);
        return SCANNER.next();
    }

    /**
     * Builds the filename used to save a compressed image.
     *
     * <p>
     * The {@code .png} extension is removed from the original filename.
     * If a compression method or parameter is provided, they are appended
     * using the following format: {@code filename-methodparameter}.
     * </p>
     *
     * <p>
     * <b>Complexity: O(n)</b>, where {@code n} is the length of the filename.
     * </p>
     *
     * @param filename  The original image filename.
     * @param method    The compression method, or {@code null}.
     * @param parameter The compression parameter, or {@code null}.
     * @return The formatted filename without the {@code .png} extension.
     */
    public static String formatFilename(String filename, String method, String parameter) {
        String cleanedFilename = filename;

        int lastSeparator = Math.max(cleanedFilename.lastIndexOf('/'), cleanedFilename.lastIndexOf("\\"));

        if (lastSeparator >= 0)
            cleanedFilename = cleanedFilename.substring(lastSeparator + 1);

        if (cleanedFilename.endsWith(".png"))
            cleanedFilename = cleanedFilename.substring(0, cleanedFilename.length() - ".png".length());

        String compParameter = (parameter == null) ? "" : parameter;
        String compMethod = (method == null) ? "" : method;

        if (compMethod.isEmpty() && compParameter.isEmpty())
            return cleanedFilename;

        return cleanedFilename + "-" + compMethod + compParameter;
    }

    /**
     * Prompts the user for an image filename and attempts to load it.
     *
     * <p>
     * <b>Complexity:</b> depends on the image loading operations.
     * </p>
     *
     * @param query The message displayed when requesting the filename.
     * @return An {@link ImageWithFilename} containing the loaded image and its
     *         filename.
     */
    protected static ImageWithFilename getImageWithFilename(String query) {
        while (true) {
            String filename = askForInput(query);
            ImagePNG image = tryLoadImage(filename);

            if (image != null) {
                logSuccess("File " + filename + " loaded.");
                return new ImageWithFilename(image, filename);
            }

            System.err.printf("An IO exception occurred while loading '%s'.%n%n", filename);
        }
    }

    /**
     * Attempts to load an image from the given filename.
     *
     * <p>
     * If the image cannot be loaded because of an I/O exception,
     * {@code null} is returned.
     * </p>
     *
     * <p>
     * <b>Complexity:</b> depends on {@link ImagePNG#ImagePNG(String)}.
     * </p>
     *
     * @param filename The filename of the image to load.
     * @return The loaded image, or {@code null} if loading fails.
     */
    public static ImagePNG tryLoadImage(String filename) {
        try {
            return new ImagePNG(filename);
        } catch (IOException exception) {
            return null;
        }
    }

    /**
     * Prompts the user for a parameter value and attempts to parse it.
     *
     * @param query The message displayed when requesting the parameter.
     * @return The parsed integer.
     */
    protected static int getParameter(String query) {
        while (true) {
            String value = askForInput(query);
            int parameter = tryParseInteger(value);

            if (parameter >= 0)
                return parameter;

            System.err.printf("Invalid parameter provided (must be an integer): %s%n.", value);
        }
    }

    /**
     * Attempts to parse an integer from the string value.
     *
     * <p>
     * If the integer cannot be parsed because of a number format exception,
     * {@code -1} is returned.
     * </p>
     *
     * <p>
     * <b>Complexity: O(n)</b>, where {@code n} is the length of the value.
     * </p>
     *
     * @param value The string value to parse.
     * @return The parsed integer, or {@code -1} if the parse fails.
     */
    public static int tryParseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Displays a success message for a completed action.
     *
     * <p>
     * <b>Complexity: O(1)</b>.
     * </p>
     *
     * @param action The description of the successfully completed operation.
     */
    protected static void logSuccess(String action) {
        System.out.printf("%s[SUCCESS]%s %s%n", GREEN, RESET, action);
    }

    /**
     * Displays an error message for an action.
     *
     * <p>
     * <b>Complexity: O(1)</b>.
     * </p>
     *
     * @param error The error that occurred during an operation.
     */
    protected static void logError(String error) {
        System.out.printf("%s[ERROR]%s %s%n", RED, RESET, error);
    }
}
