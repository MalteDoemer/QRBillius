package qrbillius.views;

import javafx.scene.Parent;


/**
 * This class combines the actual layout of the view and the controller together.
 */
public record ViewInfo(Parent root, ViewController controller) {
}
