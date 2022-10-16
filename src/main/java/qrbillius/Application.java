package qrbillius;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import net.codecrete.qrbill.generator.Bill;
import qrbillius.errors.ErrorMessage;
import qrbillius.errors.ErrorResult;
import qrbillius.qrbill.QRBillInfo;
import qrbillius.views.ViewController;
import qrbillius.views.ViewInfo;

import java.io.IOException;
import java.util.ResourceBundle;

public class Application extends javafx.application.Application {

    private static final int PREF_WIDTH = 800;
    private static final int PREF_HEIGHT = 500;

    private Stage stage;

    private ResourceBundle uiResources;
    private ResourceBundle errorResources;

    private ViewInfo mainView;

    private ViewInfo addView;

    private ViewInfo settingsView;

    private final SettingsManager settings = new SettingsManager();

    private final ObservableList<QRBillInfo> bills = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        settings.load();

        uiResources = loadBundle("UI");
        errorResources = loadBundle("ErrorMessages");

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

    public void showErrorResult(ErrorResult result) {
        if (!result.hasErrors())
            return;

        var messages = result.getErrorMessages();

        if (messages.size() == 1) {
            showErrorMessage(messages.get(0));
            return;
        }

        var builder = new StringBuilder();

        for (var message : messages) {
            builder.append("- ");
            builder.append(message.getFormattedMessage(errorResources));
            builder.append('\n');
        }

        var dialog = new Alert(Alert.AlertType.ERROR, builder.toString());
        dialog.initOwner(stage.getOwner());
        dialog.showAndWait();
    }

    public void showErrorMessage(ErrorMessage message) {
        var text = message.getFormattedMessage(errorResources);
        var dialog = new Alert(Alert.AlertType.ERROR, text);
        dialog.initOwner(stage.getOwner());
        dialog.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public ResourceBundle getUiResources() {
        return uiResources;
    }

    public ResourceBundle getErrorResources() {
        return errorResources;
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

    public ObservableList<QRBillInfo> getBills() {
        return bills;
    }

    public static void main(String[] args) {
        launch();
    }
}
