package qrbillius;

/** This the entry point of the App. It exists to solve a weird bug in JavaFX.
 * The class loader fails to find the JavaFX libraries if the Application.main() is
 * called directly and not via this indirection.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.main(args);
    }
}
