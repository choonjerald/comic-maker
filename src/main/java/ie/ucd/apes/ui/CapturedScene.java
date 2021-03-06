package ie.ucd.apes.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class CapturedScene extends HBox {
    private final ImageView imageView;
    private final Button moveLeftButton;
    private final Button moveRightButton;

    public CapturedScene(Image image) {
        StackPane stackPane = new StackPane();

        imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);

        //buttons for changing order of panels
        moveLeftButton = new Button("←");
        moveRightButton = new Button("→");
        moveLeftButton.setStyle("visibility: hidden;");
        moveRightButton.setStyle("visibility: hidden;");

        HBox orderButtonsHBox = new HBox();
        orderButtonsHBox.getChildren().addAll(moveLeftButton, moveRightButton);
        orderButtonsHBox.setAlignment(Pos.CENTER);

        //buttons become visible when hovered over image in scroll pane
        stackPane.setOnMouseEntered((e) -> {
            moveRightButton.setStyle("visibility: visible;");
            moveLeftButton.setStyle("visibility: visible;");
        });
        stackPane.setOnMouseExited((e) -> {
            moveRightButton.setStyle("visibility: hidden;");
            moveLeftButton.setStyle("visibility: hidden;");
        });

        stackPane.getChildren().addAll(imageView, orderButtonsHBox);

        getChildren().add(stackPane);
        HBox.setHgrow(stackPane, Priority.NEVER);
        getStyleClass().add("captured-scene");
        setFocusTraversable(true);
        setMargin(this, new Insets(10, 10, 10, 10));
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public Button getMoveLeftButton() {
        return moveLeftButton;
    }

    public Button getMoveRightButton() {
        return moveRightButton;
    }
}