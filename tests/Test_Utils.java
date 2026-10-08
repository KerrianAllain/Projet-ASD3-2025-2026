import java.util.Objects;

public class Test_Utils {
    private static void check(boolean comparison, boolean status) {
        if (comparison != status)
            throw new IllegalArgumentException("Comparison: " + comparison + ", status: " + status);
    }

    public static void testFormatFilename() {
        check(Objects.equals(Utils.formatFilename("i", "lambda", "20"), "i-lambda20"), true);
        check(Objects.equals(Utils.formatFilename("i", "phi", "20"), "i-phi20"), true);

        check(Objects.equals(Utils.formatFilename("i", "lambda", "128"), "i-phi128"), false);
        check(Objects.equals(Utils.formatFilename("i", "phi", "128"), "i-lambda128"), false);
    }

    public static void testTryLoadImage() {
        check(Objects.isNull(Utils.tryLoadImage("images/invalidImage.png")), true);
        check(Objects.isNull(Utils.tryLoadImage("invalidFolder/i.png")), true);

        check(Objects.isNull(Utils.tryLoadImage("images/1024-cube.png")), false);
        check(Objects.isNull(Utils.tryLoadImage("images/i.png")), false);
    }

    public static void testTryLoadParameter() {
        check(Objects.equals(Utils.tryParseInteger("i.png"), -1), true);
        check(Objects.equals(Utils.tryParseInteger("abc"), -1), true);

        check(Objects.equals(Utils.tryParseInteger("20"), -1), false);
        check(Objects.equals(Utils.tryParseInteger("4"), -1), false);
    }

    public static void main(String[] args) {
        try {
            testFormatFilename();
            testTryLoadImage();
            testTryLoadParameter();
        } catch (IllegalArgumentException exception) {
            System.err.printf("Objects mismatch: %s%n", exception.getMessage());
            System.exit(1);
        }
    }
}
