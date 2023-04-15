package qrbillius.views;

import qrbillius.Application;

/**
 * This class abstracts a JavaFX controller.
 */
public abstract class ViewController {

    /**
     * This function is called during initialization.
     */
    public abstract void init(Application app);

    /**
     * This function is called before the view is made active.
     */
    public abstract void show();
}
