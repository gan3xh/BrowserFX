package application;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconButton extends Button {

    private static final String DEFAULT_STYLE = "-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;";
    private static final String PRESSED_STYLE = "-fx-background-color: transparent; -fx-padding: 3 1 1 3;";

    public IconButton(Image icon, double height, double width) {
        ImageView iconView = createIconView(icon, height, width);
        setGraphic(iconView);
        setStyle(DEFAULT_STYLE);
        configureButtonBehavior();
    }

    private ImageView createIconView(Image icon, double height, double width) {
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(height);
        iconView.setFitWidth(width);
        iconView.setPreserveRatio(true);
        return iconView;
    }

    private void configureButtonBehavior() {
        setOnMousePressed(event -> setStyle(PRESSED_STYLE));
        setOnMouseReleased(event -> setStyle(DEFAULT_STYLE));
    }
}