import java.awt.Color;
import java.util.ArrayList;
import java.io.IOException;

public class Test_AVL {

    public static boolean isSorted(ArrayList<Color> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            if (AVL.greaterThan(array.get(i), array.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    public static void testBuilders(ImagePNG img) {
        // AVL build by an image
        AVL avlImg = new AVL(img);
        String avlImgString = avlImg.dfs();

        // AVL build by an RQuadtree build by an image
        RQuadtree rqtImg = new RQuadtree(img);
        AVL avlRqt = new AVL(rqtImg);
        String avlRqString = avlRqt.dfs();

        // Deep-First Search must be the same in the two cases
        assert avlImgString.equals(avlRqString);
    }

    public static void testAdds(ImagePNG img) {
        
    }

    public static void main(String[] args) {
        try {
            ImagePNG image = new ImagePNG(args[0]);
            testBuilders(image);
        } catch (IOException e) {
            System.out.printf("An IO exception occurred.\n%s", e);
        }
    }
}
