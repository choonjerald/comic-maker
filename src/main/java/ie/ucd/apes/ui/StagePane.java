package ie.ucd.apes.ui;

import ie.ucd.apes.controller.CharacterController;
import ie.ucd.apes.controller.DialogueController;
import ie.ucd.apes.controller.NarrativeBarController;
import ie.ucd.apes.entity.Constants;
import ie.ucd.apes.entity.DialogueType;
import ie.ucd.apes.entity.PruneLevel;
import ie.ucd.apes.entity.Selection;
import ie.ucd.apes.utils.ColorUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Optional;


public class StagePane extends VBox {
    private final CharacterController characterController;
    private final DialogueController dialogueController;
    private final NarrativeBarController narrativeBarController;
    private CharacterImage characterLeftView;
    private CharacterImage characterRightView;
    private DialogueBox leftDialogueBox;
    private DialogueBox rightDialogueBox;
    private ColorPane colorPane;
    private NarrativeBar narrativeBarTop;
    private NarrativeBar narrativeBarBottom;

    public StagePane(CharacterController characterController, DialogueController dialogueController, NarrativeBarController narrativeBarController) {
        this.characterController = characterController;
        this.dialogueController = dialogueController;
        this.narrativeBarController = narrativeBarController;
        this.colorPane = null;
        initView();
    }
     
    private void initView() {
        GridPane tiles = new GridPane();
        tiles.setMaxWidth(600);
        tiles.setHgap(15);

        initNarrativeBars();
        tiles.add(narrativeBarTop,0,0, 2, 1);
        tiles.add(narrativeBarBottom,0,3, 2, 1);

        initDialogues();
        tiles.add(leftDialogueBox, 0, 1);
        tiles.add(rightDialogueBox, 1, 1);
        GridPane.setValignment(leftDialogueBox, VPos.BOTTOM);
        GridPane.setValignment(rightDialogueBox, VPos.BOTTOM);
        GridPane.setHalignment(leftDialogueBox, HPos.CENTER);
        GridPane.setHalignment(rightDialogueBox, HPos.CENTER);

        // character models
        initCharacters();
        tiles.add(characterLeftView, 0, 2);
        tiles.add(characterRightView, 1, 2);

        // background
        tiles.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().add(tiles);
        HBox.setHgrow(this, Priority.ALWAYS);
    }

    private void initNarrativeBars() {
        narrativeBarTop = new NarrativeBar(narrativeBarController.getNarrativeText(Selection.IS_TOP));
        narrativeBarBottom = new NarrativeBar(narrativeBarController.getNarrativeText(Selection.IS_BOTTOM));
        narrativeBarTop.setVisible(narrativeBarController.isVisible(Selection.IS_TOP));
        narrativeBarBottom.setVisible(narrativeBarController.isVisible(Selection.IS_BOTTOM));
        narrativeBarTop.setOnMouseClicked((e) -> showNarrativeBarPopUp(Selection.IS_TOP));
        narrativeBarBottom.setOnMouseClicked((e) -> showNarrativeBarPopUp(Selection.IS_BOTTOM));
    }

    public void toggleNarrativeBar(Selection selection) {
        if (selection.equals(Selection.IS_TOP)) {
            narrativeBarController.toggleNarrative(selection);
            narrativeBarTop.setVisible(narrativeBarController.isVisible(selection));
            narrativeBarTop.setManaged(narrativeBarController.isVisible(selection));
        }   else {
            narrativeBarController.toggleNarrative(selection);
            narrativeBarBottom.setVisible(narrativeBarController.isVisible(selection));
            narrativeBarBottom.setManaged(narrativeBarController.isVisible(selection));
        }
    }
    
    private void showNarrativeBarPopUp(Selection selection) {
        TextInputDialog popup = new TextInputDialog(narrativeBarController.getNarrativeText(selection));
        popup.setTitle("Narrative Bar");
        if(selection.equals(Selection.IS_TOP)){
            popup.setHeaderText("Enter text for the top bar");
        }else{
            popup.setHeaderText("Please enter text for the bottom bar");
        }
        Optional<String> result = popup.showAndWait();
        result.ifPresent(text -> setNarrativeText(text, selection));
    }

    private void setNarrativeText(String text,Selection selection){
        NarrativeBar narrativeBar = getNarrativeBar(selection);
        narrativeBarController.setNarrativeText(selection, text);
        narrativeBar.setText(text);
    }
    //currently narrativeBarBottom is just a placeholder
    private NarrativeBar getNarrativeBar(Selection selection){
        return selection.equals(Selection.IS_TOP) ? narrativeBarTop : narrativeBarBottom;
    }



    private void initDialogues() {
        leftDialogueBox = new DialogueBox(dialogueController.getDialogueText(Selection.IS_LEFT));
        rightDialogueBox = new DialogueBox(dialogueController.getDialogueText(Selection.IS_RIGHT));
        leftDialogueBox.setVisible(dialogueController.isVisible(Selection.IS_LEFT));
        rightDialogueBox.setVisible(dialogueController.isVisible(Selection.IS_RIGHT));
        leftDialogueBox.setOnMouseClicked((e) -> showSpeechPopup(Selection.IS_LEFT));
        rightDialogueBox.setOnMouseClicked((e) -> showSpeechPopup(Selection.IS_RIGHT));
    }

    public void toggleFocusedDialogue(DialogueType dialogueType) {
        Selection selection = getFocusedDialogueSelection();
        if (selection != null) {
            DialogueBox dialogueBox = getDialogueBox(selection);
            if (dialogueType.equals(dialogueController.getDialogueType(selection))) {
                dialogueController.toggleDialogue(selection);
            } else {
                // different dialogue type than current type, only toggle if it's not visible
                if (!dialogueController.isVisible(selection)) {
                    dialogueController.toggleDialogue(selection);
                }
                // change type
                dialogueController.setDialogueType(selection, dialogueType);
            }
            dialogueBox.setDialogueStyle(selection, dialogueType);
            dialogueBox.setVisible(dialogueController.isVisible(selection));
            dialogueBox.setManaged(dialogueController.isVisible(selection));
        }
    }

    private Selection getFocusedDialogueSelection() {
        Selection selection = null;
        if (characterLeftView.isFocused()) {
            selection = Selection.IS_LEFT;
        } else if (characterRightView.isFocused()) {
            selection = Selection.IS_RIGHT;
        }
        return selection;
    }

    private void showSpeechPopup(Selection selection) {
        TextInputDialog popup = new TextInputDialog(dialogueController.getDialogueText(selection));
        popup.setTitle("Speech Dialogue");
        if (selection.equals(Selection.IS_LEFT)) {
            popup.setHeaderText("Please enter the speech text for the left character");
        } else {
            popup.setHeaderText("Please enter the speech text for the right character");
        }
        Optional<String> result = popup.showAndWait();
        result.ifPresent(text -> setDialogueText(text, selection));
    }

    private void setDialogueText(String text, Selection selection) {
        DialogueBox dialogueBox = getDialogueBox(selection);
        dialogueController.setDialogueText(selection, text);
        dialogueBox.setText(text);
    }

    private DialogueBox getDialogueBox(Selection selection) {
        return selection.equals(Selection.IS_LEFT) ? leftDialogueBox :
                rightDialogueBox;
    }

    

    private void initCharacters() {
        characterLeftView = new CharacterImage(characterController.renderCharacterImage(Selection.IS_LEFT));
        characterRightView = new CharacterImage(characterController.renderCharacterImage(Selection.IS_RIGHT));
        if (characterController.isFlipped(Selection.IS_LEFT)) {
            characterLeftView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        if (characterController.isFlipped(Selection.IS_RIGHT)) {
            characterRightView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

        characterLeftView.setFocusTraversable(true);
        characterRightView.setFocusTraversable(true);

        characterLeftView.setOnMouseClicked((e) -> {
            characterLeftView.requestFocus();
            colorPane.setSkinColorSelector(characterController.getSkinColor(Selection.IS_LEFT));
            colorPane.setHairColorSelector(characterController.getHairColor(Selection.IS_LEFT));
        });
        characterRightView.setOnMouseClicked((e) -> {
            characterRightView.requestFocus();
            colorPane.setSkinColorSelector(characterController.getSkinColor(Selection.IS_RIGHT));
            colorPane.setHairColorSelector(characterController.getHairColor(Selection.IS_RIGHT));
        });
    }

    // handle all rendering except orientation
    public void renderCharacterImage(Selection selection) {
        if (selection == null) {
            return;
        }
        ImageView imageView = getImageView(selection);
        imageView.setImage(characterController.renderCharacterImage(selection));
        if (imageView.getImage() != null) {
            // render by layers
            renderGender(imageView, selection);
            renderSkinColor(imageView, selection);
            renderHairColor(imageView, selection);
        }
    }

    public void updateCharacterImage(String newImageName, Selection selection) {
        characterController.resetState(selection);
        characterController.setCharacterImageFileName(newImageName, selection);
        renderCharacterImage(selection);
        ImageView imageView = getImageView(selection);
        colorPane.setSkinColorSelector(characterController.getSkinColor(selection));
        colorPane.setHairColorSelector(characterController.getHairColor(selection));
        getCharacterImage(selection).requestFocus();
        if (characterController.isFlipped(selection)) {
            imageView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
    }

    public void flipSelectedCharacterImage() {
        Selection selection = getFocusedCharacterSelection();
        if (selection != null) {
            characterController.flipCharacterOrientation(selection);
            flipImageViewOrientation(getImageView(selection));
        }
    }

    public void setColorPane(ColorPane colorPane) {
        this.colorPane = colorPane;
    }

    private void flipImageViewOrientation(ImageView imageView) {
        if (imageView.getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)) {
            imageView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            imageView.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    public void changeGender() {
        Selection selection = getFocusedCharacterSelection();
        if (selection != null) {
            characterController.changeGender(selection);
            renderCharacterImage(selection);
        }
    }

    private void renderGender(ImageView imageView, Selection selection) {
        if (characterController.isMale(selection)) {
            ColorUtils.changeColor(imageView, Constants.DEFAULT_WIG_COLOR, Constants.REPLACEMENT_WIG_COLOR, PruneLevel.LOW);
            ColorUtils.changeColor(imageView, Constants.RIBBON_COLOR, Constants.REPLACEMENT_RIBBON_COLOR, PruneLevel.LOW);
        }
    }

    public void changeSkinColor(Color newSkinColor) {
        Selection selection = getFocusedCharacterSelection();
        if (selection != null) {
            characterController.setSkinColor(selection, newSkinColor);
            renderCharacterImage(selection);
        }
    }

    private void renderSkinColor(ImageView imageView, Selection selection) {
        Color characterSkinColor = characterController.getSkinColor(selection);
        ColorUtils.changeColor(imageView, Constants.DEFAULT_SKIN_COLOR, characterSkinColor, PruneLevel.NONE);
        if (characterController.isMale(selection)) {
            ColorUtils.changeColor(imageView, Constants.LIPS_COLOR, characterSkinColor, PruneLevel.MEDIUM);
        }
    }

    public void changeHairColor(Color newHairColor) {
        Selection selection = getFocusedCharacterSelection();
        if (selection != null) {
            characterController.setHairColor(selection, newHairColor);
            renderCharacterImage(selection);
        }
    }

    private void renderHairColor(ImageView imageView, Selection selection) {
        Color characterHairColor = characterController.getHairColor(selection);
        ColorUtils.changeColor(imageView, Constants.DEFAULT_HAIR_COLOR, characterHairColor, PruneLevel.LOW);
        if (!characterController.isMale(selection)) {
            ColorUtils.changeColor(imageView, Constants.DEFAULT_WIG_COLOR, characterHairColor, PruneLevel.LOW);
        }
    }

    private Selection getFocusedCharacterSelection() {
        Selection selection = null;
        if (characterLeftView.isFocused()) {
            selection = Selection.IS_LEFT;
        } else if (characterRightView.isFocused()) {
            selection = Selection.IS_RIGHT;
        }
        return selection;
    }

    private CharacterImage getCharacterImage(Selection selection) {
        return selection.equals(Selection.IS_LEFT) ? characterLeftView :
                characterRightView;
    }

    private ImageView getImageView(Selection selection) {
        return selection.equals(Selection.IS_LEFT) ? characterLeftView.getImageView() :
                characterRightView.getImageView();
    }


}
