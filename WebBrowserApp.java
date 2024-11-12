package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WebBrowserApp extends Application {

    @Override
    public void start(Stage mainWindow) {
        TabPane tabContainer = createTabPane();
        BorderPane mainLayout = createMainLayout(tabContainer);
        Scene mainScene = createMainScene(mainLayout);
        
        configureMainWindow(mainWindow, mainScene);
    }

    private TabPane createTabPane() {
        TabPane tabContainer = new TabPane();
        tabContainer.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        return tabContainer;
    }

    private BorderPane createMainLayout(TabPane tabContainer) {
        BorderPane mainLayout = new BorderPane();
        Button newTabButton = createNewTabButton(tabContainer);
        Button downloadButton = createDownloadButton();
        
        HBox topBar = new HBox(5, newTabButton, downloadButton);
        topBar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px;");
        
        mainLayout.setTop(topBar);
        mainLayout.setCenter(tabContainer);
        
        Tab initialTab = new Tab("Initial Tab", new WebPageView().getContentPane());
        tabContainer.getTabs().add(initialTab);
        
        return mainLayout;
    }

    private Button createNewTabButton(TabPane tabContainer) {
        Button newTabButton = new Button("+");
        newTabButton.setOnAction(event -> {
            tabContainer.getTabs().add(new Tab("New Tab", new WebPageView().getContentPane()));
            tabContainer.getSelectionModel().selectLast();
        });
        return newTabButton;
    }

    private Button createDownloadButton() {
        Button downloadButton = new Button("Downloads");
        downloadButton.setOnAction(event -> openDownloadManager());
        return downloadButton;
    }

    private void openDownloadManager() {
        Platform.runLater(() -> {
            javax.swing.SwingUtilities.invokeLater(() -> {
                FileRetrievalManager manager = new FileRetrievalManager();
                manager.setVisible(true);
            });
        });
    }

    private Scene createMainScene(BorderPane mainLayout) {
        Scene mainScene = new Scene(mainLayout);
        mainScene.getStylesheets().add(getClass().getResource("BrowserStyles.css").toExternalForm());
        return mainScene;
    }

    private void configureMainWindow(Stage mainWindow, Scene mainScene) {
        mainWindow.setScene(mainScene);
        mainWindow.setTitle("BrowserFXv2");
        mainWindow.setMaximized(true);
        mainWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}