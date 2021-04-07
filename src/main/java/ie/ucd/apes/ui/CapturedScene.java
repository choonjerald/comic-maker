package ie.ucd.apes.ui;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CapturedScene extends HBox {
    private final int position;
    private final ImageView imageView;

    public CapturedScene(Image image, int position) {
        imageView = new ImageView(image);
        this.position = position;
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);
        getChildren().add(imageView);
        HBox.setHgrow(imageView, Priority.NEVER);
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

    public int getPosition() {
        return position;
    }
}