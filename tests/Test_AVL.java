import java.awt.Color;
import java.io.IOException;

public class Test_AVL {

    public static void testBuilders(ImagePNG img) {
        // AVL build by an image
        AVL avlImg = new AVL(img);
        String avlImgString = avlImg.dfs();

        // AVL build by an RQuadtree build by an image
        RQuadtree rqtImg = new RQuadtree(img);
        AVL avlRqt = new AVL(rqtImg);
        String avlRqString = avlRqt.dfs();

        // Deep-First Search must be the same in the two cases
        if(! avlImgString.equals(avlRqString)) System.err.println("Builders do not return the same representation");
    }

    public static void testRemoveAdds() throws AssertionError {
        try {
            ImagePNG i = new ImagePNG("images/i.png");
            AVL avl = new AVL(i);
            if (! avl.toString().equals("(000000)ffffff")) System.err.println("The AVL tree for i.png isn't right");

            // Removing a leaf
            avl.remove("000000");
            if (! avl.toString().equals("ffffff")) System.err.println("The AVL tree for i.png without #000000 isn't right");

            // Adding 2 values such as we need a Rotation Right
            avl.add("808080");
            avl.add("000000");
            if (! avl.toString().equals("(000000)808080(ffffff)")) System.err.println("Problem in Rotation Right");

            // Removing a root having to subtrees
            avl.remove("808080");
            if (! avl.toString().equals("000000(ffffff)")) System.err.println("Problem in removing a root having to subtrees");

            // Removing
            avl.remove("ffffff");
            if (! avl.toString().equals("000000")) System.err.println("Problem in removing last leaf");

            // Adding 2 values such as we need a Rotation Left
            avl.add("808080");
            avl.add("ffffff");
            if (! avl.toString().equals("(000000)808080(ffffff)")) System.err.println("Problem in Rotation Left");

            // Removing
            avl.remove("000000");
            avl.remove("808080");

            // Adding 2 values such as we need a Double Rotation Right
            avl.add("000000");
            avl.add("808080");
            if (! avl.toString().equals("(000000)808080(ffffff)")) System.err.println("Problem in Double Rotation Right");

            // Removing
            avl.remove("ffffff");
            avl.remove("808080");

            // Adding 2 values such as we need a Double Rotation Left
            avl.add("ffffff");
            avl.add("808080");
            if (! avl.toString().equals("(000000)808080(ffffff)")) System.err.println("Problem in Double Rotation Left");

            avl.add("4e4e4e");
            avl.add("242424");
            avl.add("dadada");
            // Testing removing
            avl.remove("808080");
            if (! avl.toString().equals("((000000)242424)4e4e4e((dadada)ffffff)")) System.err.println("Problem in removing root");

            // Removing until the we have an empty AVL
            AVL avl2 = new AVL(i);
            avl2.remove("000000");
            avl2.remove("ffffff");
            if (! avl2.toString().equals("")) System.err.println("Problem in removing the last node");

            // Adding a value to an empty AVL
            avl2.add("808080");
            if (! avl2.toString().equals("808080")) System.err.println("Problem in adding a node to an empty AVL");
        } catch (IOException e) {
            System.out.println("testRemoveAdds can not be called");
        }
    }

    public static void main(String[] args) {
        try {
            ImagePNG image = new ImagePNG(args[0]);
            testBuilders(image);
            testRemoveAdds();
        } catch (IOException e) {
            System.out.printf("An IO exception occurred.\n%s", e);
        } catch (AssertionError e) {
            System.err.println(e.getMessage());
        }
    }
}
