
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        if (args.length > 3)
            throw new IllegalArgumentException("Too much arguments provided.");

        try {
            ImagePNG image = new ImagePNG(args[0]);
            double index = ImagePNG.computeEQM(image, image);
            System.out.printf("Image EQM index: %.2f\n", index);
        } catch (IOException e) {
            System.out.printf("An IO exception occurred.\n%s", e);
        }
    }
}
