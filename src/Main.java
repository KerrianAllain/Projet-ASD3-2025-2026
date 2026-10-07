public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            new InteractiveMode().runProject();
            return;
        }

        new NonInteractiveMode().runProject(args);
    }
}
