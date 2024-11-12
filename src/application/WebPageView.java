package application;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;

public class WebPageView {

    private WebView browser;
    private WebEngine engine;
    private VBox layout;
    private TextField addressBar;

    public WebPageView() {
        initializeComponents();
    }

    private void initializeComponents() {
        browser = new WebView();
        engine = browser.getEngine();
        layout = new VBox();
        addressBar = new TextField();

        HBox controlBar = createControlBar();
        ProgressBar loadingBar = createLoadingBar();

        configureBrowserBehavior();

      
        VBox.setVgrow(browser, Priority.ALWAYS);
        
        layout.getChildren().addAll(controlBar, loadingBar, browser);
    }

    private HBox createControlBar() {
        HBox controlBar = new HBox(5); // 5 px spacing
        addressBar.setText("https://www.google.com");
        navigateToUrl("https://www.google.com");

        Button backButton = createNavigationButton("Back", this::goBack);
        Button forwardButton = createNavigationButton("Forward", this::goForward);
        Button refreshButton = createNavigationButton("Refresh", this::refreshPage);

        addressBar.setOnAction(e -> navigateToUrl(addressBar.getText()));

        HBox.setHgrow(addressBar, Priority.ALWAYS);
        controlBar.getChildren().addAll(backButton, forwardButton, refreshButton, addressBar);
        controlBar.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0;");

        return controlBar;
    }

    private Button createNavigationButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private ProgressBar createLoadingBar() {
        ProgressBar loadingBar = new ProgressBar();
        loadingBar.prefWidthProperty().bind(layout.widthProperty());
        loadingBar.setVisible(false);
        return loadingBar;
    }

    private void configureBrowserBehavior() {
        engine.locationProperty().addListener((observable, oldValue, newValue) -> addressBar.setText(newValue));

        engine.getLoadWorker().workDoneProperty().addListener(createLoadingListener());
    }

    private ChangeListener<Number> createLoadingListener() {
        return (observable, oldValue, newValue) -> {
            ProgressBar loadingBar = (ProgressBar) layout.getChildren().get(1);
            loadingBar.setVisible(true);
            loadingBar.setProgress(newValue.doubleValue() / 100);
            if (loadingBar.getProgress() >= 1) {
                loadingBar.setVisible(false);
                loadingBar.setProgress(0);
            }
        };
    }

    private void refreshPage() {
        engine.reload();
    }

    private void goBack() {
        if (engine.getHistory().getCurrentIndex() > 0) {
            engine.getHistory().go(-1);
        }
    }

    private void goForward() {
        if (engine.getHistory().getCurrentIndex() < engine.getHistory().getEntries().size() - 1) {
            engine.getHistory().go(1);
        }
    }

    private void navigateToUrl(String url) {
        String validUrl = validateUrl(url);
        if (validUrl != null) {
            addressBar.setText(validUrl);
            engine.load(validUrl);
        }
    }

    private String validateUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        try {
            new URL(url);
            return url;
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + url);
            return null;
        }
    }

    public VBox getContentPane() {
        return layout;
    }
}