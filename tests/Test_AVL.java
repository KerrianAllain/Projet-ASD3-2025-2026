package Projet.tests;

import java.awt.Color;
import java.util.ArrayList;
import AllainAllain.src;

public class Test_AVL {

    public static boolean isSorted(ArrayList<Color> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            if (greaterThan(array.get(i), array.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
