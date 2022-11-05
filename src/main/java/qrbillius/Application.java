package qrbillius;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import qrbillius.errors.ErrorConstants;
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

    private Settings settings;

    private final ObservableList<QRBillInfo> bills = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        settings = SettingsManager.load();

        uiResources = loadBundle("UI");
        errorResources = loadBundle("ErrorMessages");

        mainView = loadView("main-view.fxml");
        addView = loadView("add-view.fxml");
        settingsView = loadView("settings-view.fxml");

        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {

            var cause = e;
            while (cause.getCause() != null)
                cause = cause.getCause();

            var message = new ErrorMessage(ErrorConstants.UNHANDLED_EXCEPTION_OCCURRED, cause.getLocalizedMessage());
            Application.this.showErrorMessage(message);
        });

        stage.setScene(new Scene(mainView.root(), PREF_WIDTH, PREF_HEIGHT));
        stage.getScene().getStylesheets().add(loadStylesheet("style.css"));
        stage.setTitle(uiResources.getString("title"));
        stage.show();

        var thread = new Thread(this::preloadDocx4j);
        thread.start();
    }

    private void preloadDocx4j() {
        try {
            SpreadsheetMLPackage.createPackage();
        } catch (InvalidFormatException ignored) {
        }
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
            showErrorMessage(messages.get(0), result.getLineNumber());
            return;
        }

        var builder = new StringBuilder();

        for (var message : messages) {
            builder.append("- ");
            builder.append(message.getFormattedMessage(errorResources));
            builder.append('\n');
        }

        displayErrorText(builder.toString(), result.getLineNumber());
    }

    public void showErrorMessage(ErrorMessage message) {
        var text = message.getFormattedMessage(errorResources);
        displayErrorText(text, ErrorResult.NO_LINE_NUMBER);
    }

    public void showErrorMessage(ErrorMessage message, int lineNumber) {
        var text = message.getFormattedMessage(errorResources);
        displayErrorText(text, lineNumber);
    }

    private void displayErrorText(String text, int lineNumber) {
        if (lineNumber != ErrorResult.NO_LINE_NUMBER) {
            var lineInfo = String.format(errorResources.getString(ErrorConstants.LINE_INFO), lineNumber);
            text = String.format("%s: %s", lineInfo, text);
        }

        var dialog = new Alert(Alert.AlertType.ERROR, "");

        var t = new Text(text);
        t.setWrappingWidth(300);

        var p = new StackPane();
        StackPane.setMargin(t, new Insets(10, 10, 10, 10));
        p.getChildren().add(t);

        dialog.getDialogPane().setContent(p);
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

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ObservableList<QRBillInfo> getBills() {
        return bills;
    }

    public static void main(String[] args) {
        launch();
    }
}
