package qrbillius;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import qrbillius.config.*;
import qrbillius.errors.ErrorConstants;
import qrbillius.errors.ErrorMessage;
import qrbillius.errors.ErrorResult;
import qrbillius.qrbill.QRBillInfo;
import qrbillius.views.ViewController;
import qrbillius.views.ViewInfo;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class Application extends javafx.application.Application {

    private static final int PREF_WIDTH = 900;
    private static final int PREF_HEIGHT = 600;

    private Stage stage;

    private ResourceBundle uiResources;
    private ResourceBundle errorResources;

    private ApplicationConfiguration applicationConfiguration;
    private ImportConfiguration importConfiguration;
    private ExportConfiguration exportConfiguration;
    private ProfileConfiguration profileConfiguration;

    private ViewInfo mainView;
    private ViewInfo importView;
    private ViewInfo addView;
    private ViewInfo exportView;
    private ViewInfo configView;


    private final ObservableList<QRBillInfo> bills = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        uiResources = loadBundle("UI");
        errorResources = loadBundle("ErrorMessages");

        applicationConfiguration = ConfigurationManager.loadApplicationConfiguration();
        importConfiguration = ConfigurationManager.loadImportConfiguration();
        exportConfiguration = ConfigurationManager.loadExportConfiguration();
        profileConfiguration = ConfigurationManager.loadProfileConfiguration();

        mainView = loadView("main-view.fxml");
        importView = loadView("import-view.fxml");
        addView = loadView("add-view.fxml");
        exportView = loadView("export-view.fxml");
        configView = loadView("config-view.fxml");

        setUncaughtExceptionHandler();

        stage.setScene(new Scene(mainView.root(), PREF_WIDTH, PREF_HEIGHT));
        stage.getScene().getStylesheets().add(loadStylesheet("style.css"));
        stage.setTitle(uiResources.getString("title"));
        stage.show();
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

    private void setUncaughtExceptionHandler() {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();

            var cause = e;
            while (cause.getCause() != null)
                cause = cause.getCause();

            var message = new ErrorMessage(ErrorConstants.UNHANDLED_EXCEPTION_OCCURRED, cause.getLocalizedMessage());
            Application.this.showErrorMessage(message);
        });
    }

    private void displayErrorText(String text, int lineNumber) {
        if (lineNumber != ErrorResult.NO_LINE_NUMBER) {
            var lineInfo = String.format(errorResources.getString(ErrorConstants.LINE_INFO), lineNumber);

            // insert a newline if the error text spans over multiple lines.
            if (text.lines().count() > 1) {
                text = String.format("%s: \n%s", lineInfo, text);
            } else {
                text = String.format("%s: %s", lineInfo, text);
            }
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

    /**
     * Switch the application to the provided view.
     */
    public void switchView(ViewInfo view) {
        view.controller().show();
        stage.getScene().setRoot(view.root());
    }

    /**
     * Display an error dialog for the error result.
     * Does nothing if there are no errors in the result.
     */
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

    /**
     * Display an error for a single error message.
     */
    public void showErrorMessage(ErrorMessage message) {
        var text = message.getFormattedMessage(errorResources);
        displayErrorText(text, ErrorResult.NO_LINE_NUMBER);
    }

    /**
     * Display an error for a single error message that has line number information.
     */
    public void showErrorMessage(ErrorMessage message, int lineNumber) {
        var text = message.getFormattedMessage(errorResources);
        displayErrorText(text, lineNumber);
    }

    /**
     * If it exists, return the lasted opened folder.
     * Otherwise, null.
     */
    public File getLastOpenedFolder() {
        var folder = new File(applicationConfiguration.lastOpenedFolder());
        return folder.exists() ? folder : null;
    }

    /**
     * Stores the last opened folder if it is not null.
     */
    public void setLastedOpenedFolder(File folder) {
        if (folder != null) {
            var config = new ApplicationConfiguration(folder.getAbsolutePath());
            setApplicationConfiguration(config);
        }
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

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
        try {
            ConfigurationManager.saveApplicationConfiguration(applicationConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImportConfiguration getImportConfiguration() {
        return importConfiguration;
    }

    public void setImportConfiguration(ImportConfiguration importConfiguration) {
        this.importConfiguration = importConfiguration;
        try {
            ConfigurationManager.saveImportConfiguration(importConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: maybe display error?
        }
    }

    public ExportConfiguration getExportConfiguration() {
        return exportConfiguration;
    }

    public void setExportConfiguration(ExportConfiguration exportConfiguration) {
        this.exportConfiguration = exportConfiguration;
        try {
            ConfigurationManager.saveExportConfiguration(exportConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: maybe display error?
        }
    }

    public ProfileConfiguration getProfileConfiguration() {
        return profileConfiguration;
    }

    public void setProfileConfiguration(ProfileConfiguration profileConfiguration) {
        this.profileConfiguration = profileConfiguration;
        try {
            ConfigurationManager.saveProfileConfiguration(profileConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
            var error = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            showErrorMessage(error);
        }
    }

    public ViewInfo getMainView() {
        return mainView;
    }

    public ViewInfo getImportView() {
        return importView;
    }

    public ViewInfo getAddView() {
        return addView;
    }

    public ViewInfo getExportView() {
        return exportView;
    }

    public ViewInfo getConfigView() {
        return configView;
    }

    public ObservableList<QRBillInfo> getBills() {
        return bills;
    }

    public static void main(String[] args) {
        launch();
    }
}