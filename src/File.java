package AllainAllain.src;

import java.io.FileWriter;
import java.io.IOException;

public class File {

    /**
     * Save the toString representation af an object in a txt file
     *
     * @param filename The file name without the extention
     *
     * @param o an Object to save
     */
    public static void saveAsTxt(String filename, Object o) {
        String content = o.toString();
        try {
            FileWriter fw = new FileWriter(filename + ".txt");
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            System.out.printf("An IO exception occurred.\n%s", e);
        }
    }
}
