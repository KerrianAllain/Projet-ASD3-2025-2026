
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            InteractiveMode.runProject(args);
            return;
        }

        NonInteractiveMode.runProject(args);
    }
}
