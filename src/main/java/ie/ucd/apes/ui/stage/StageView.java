package ie.ucd.apes.ui.stage;

import ie.ucd.apes.controller.PanelController;
import ie.ucd.apes.entity.Selection;
import ie.ucd.apes.ui.DialogueBox;
import ie.ucd.apes.ui.NarrativeBar;
import ie.ucd.apes.ui.ScrollingPane;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class StageView extends VBox {
    private final CharacterView characterView;
    private final DialogueView dialogueView;
    private final NarrativeView narrativeView;
    private final PanelController panelController;
    private final ScrollingPane scrollingPane;

    public StageView(CharacterView characterView, DialogueView dialogueView,
                     NarrativeView narrativeView, PanelController panelController,
                     ScrollingPane scrollingPane) {
        this.characterView = characterView;
        this.dialogueView = dialogueView;
        this.narrativeView = narrativeView;
        this.scrollingPane = scrollingPane;
        this.panelController = panelController;
        initView();
    }

    private void initView() {
        GridPane tiles = new GridPane();
        tiles.setMaxWidth(600);
        tiles.setHgap(15);

        NarrativeBar narrativeBarTop = narrativeView.getNarrativeBar(Selection.IS_TOP);
        NarrativeBar narrativeBarBottom = narrativeView.getNarrativeBar(Selection.IS_BOTTOM);
        tiles.add(narrativeBarTop, 0, 0, 2, 1);
        tiles.add(narrativeBarBottom, 0, 3, 2, 1);

        DialogueBox leftDialogueBox = dialogueView.getDialogueBox(Selection.IS_LEFT);
        DialogueBox rightDialogueBox = dialogueView.getDialogueBox(Selection.IS_RIGHT);
        tiles.add(leftDialogueBox, 0, 1);
        tiles.add(rightDialogueBox, 1, 1);
        GridPane.setValignment(leftDialogueBox, VPos.BOTTOM);
        GridPane.setValignment(rightDialogueBox, VPos.BOTTOM);
        GridPane.setHalignment(leftDialogueBox, HPos.CENTER);
        GridPane.setHalignment(rightDialogueBox, HPos.CENTER);

        // character models
        tiles.add(characterView.getCharacterImage(Selection.IS_LEFT), 0, 2);
        tiles.add(characterView.getCharacterImage(Selection.IS_RIGHT), 1, 2);

        // background
        tiles.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().add(tiles);
        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.NEVER);
    }

    public void saveToScrollingPane() {
        scrollingPane.addScene(takeScreenshot());
        panelController.savePanel();
    }

    private WritableImage takeScreenshot() {
        return snapshot(new SnapshotParameters(), null);
    }
}
