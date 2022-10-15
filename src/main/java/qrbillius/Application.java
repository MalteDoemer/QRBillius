package qrbillius;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import qrbillius.views.ViewController;
import qrbillius.views.ViewInfo;

import java.io.IOException;
import java.util.ResourceBundle;

public class Application extends javafx.application.Application {

    private static final int PREF_WIDTH = 800;
    private static final int PREF_HEIGHT = 500;

    private Stage stage;

    private ResourceBundle uiResources;

    private ViewInfo mainView;

    private ViewInfo addView;

    private ViewInfo settingsView;

    private SettingsManager settings = new SettingsManager();

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        settings.load();

        uiResources = loadBundle("UI");

        mainView = loadView("main-view.fxml");
        addView = loadView("add-view.fxml");
        settingsView = loadView("settings-view.fxml");

        stage.setScene(new Scene(mainView.root(), PREF_WIDTH, PREF_HEIGHT));
        stage.getScene().getStylesheets().add(loadStylesheet("style.css"));
        stage.setTitle(uiResources.getString("title"));
        stage.show();
    }

    public void switchView(ViewInfo view) {
        view.controller().show();
        stage.getScene().setRoot(view.root());
    }

    private ViewInfo loadView(String name) throws IOException {
        var loader = new FXMLLoader(Application.class.getResource(String.format("views/%s", name)), uiResources);
        var root = loader.<Parent>load();
        var controller = loader.<ViewController>getController();

        controller.init(this);
        return new ViewInfo(root, controller);
    }

    private String loadStylesheet(String name) {
        var resource = Application.class.getResource(String.format("styles/%s", name));
        assert resource != null;
        return resource.toString();
    }

    private ResourceBundle loadBundle(String name) {
        return ResourceBundle.getBundle(String.format("qrbillius/bundles/%s", name));
    }


    public Stage getStage() {
        return stage;
    }

    public ResourceBundle getUiResources() {
        return uiResources;
    }

    public ViewInfo getMainView() {
        return mainView;
    }

    public ViewInfo getAddView() {
        return addView;
    }

    public ViewInfo getSettingsView() {
        return settingsView;
    }

    public SettingsManager getSettings() {
        return settings;
    }

    public static void main(String[] args) {
        launch();
    }
}
