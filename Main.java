public class Main {
    public static void main(String[] args) {
        // If "gui" argument passed, launch directly in GUI mode
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            GraphGUI.launch();
            return;
        }
        // Default: console mode (with option to launch GUI from inside)
        ConsoleApp.run();
    }
}
