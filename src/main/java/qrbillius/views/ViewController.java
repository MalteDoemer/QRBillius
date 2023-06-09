package qrbillius.views;

import javafx.scene.input.KeyCombination;
import qrbillius.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * This class abstracts a JavaFX controller.
 */
public abstract class ViewController {

    protected Application app;

    protected Map<KeyCombination, Runnable> keyboardShortcuts;

    public ViewController() {
        this.keyboardShortcuts = new HashMap<>();
    }

    /**
     * This function is called during initialization.
     */
    public void init(Application app) {
        this.app = app;
    }

    /**
     * This function is called before the view is made active.
     */
    public void show() {
        registerKeyboardShortcuts();
    }

    /**
     * This function is called when this view is made inactive.
     */
    public void hide() {
        unregisterKeyboardShortcuts();
    }

    /**
     * This function registers all keyboard shortcuts for this view.
     * This function must be called after the scene is initialized.
     */
    protected void registerKeyboardShortcuts() {
        var scene = app.getStage().getScene();
        scene.getAccelerators().putAll(keyboardShortcuts);
    }

    /**
     * This function removes all keyboard shortcuts for this view.
     * This function must be called after the scene is initialized.
     */
    protected void unregisterKeyboardShortcuts() {
        var scene = app.getStage().getScene();
        for (var key : keyboardShortcuts.keySet()) {
            scene.getAccelerators().remove(key);
        }
    }
}
