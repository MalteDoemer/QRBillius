package qrbillius;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import qrbillius.views.ViewController;
import qrbillius.views.ViewInfo;

import java.io.IOException;
import java.util.ResourceBundle;

public class QRBilliusApplication extends Application {

    private static final int PREF_WIDTH = 800;
    private static final int PREF_HEIGHT = 500;

    private Stage stage;

    private ResourceBundle uiResources;

    private ViewInfo mainView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        uiResources = loadBundle("UI");

        mainView = loadView("main-view.fxml");

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
        var loader = new FXMLLoader(QRBilliusApplication.class.getResource(String.format("views/%s", name)), uiResources);
        var root = loader.<Parent>load();
        var controller = loader.<ViewController>getController();

        controller.init(this);
        return new ViewInfo(root, controller);
    }

    private String loadStylesheet(String name) {
        var resource = QRBilliusApplication.class.getResource(String.format("styles/%s", name));
        assert resource != null;
        return resource.toString();
    }

    private ResourceBundle loadBundle(String name) {
        return ResourceBundle.getBundle(String.format("qrbillius/bundles/%s", name));
    }

    public static void main(String[] args) {
        launch();
    }
}
