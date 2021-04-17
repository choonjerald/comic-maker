package ie.ucd.apes.ui;

import ie.ucd.apes.entity.Constants;
import ie.ucd.apes.entity.DialogueType;
import ie.ucd.apes.entity.Selection;
import ie.ucd.apes.io.FileLoader;
import ie.ucd.apes.ui.stage.CharacterView;
import ie.ucd.apes.ui.stage.DialogueView;
import ie.ucd.apes.ui.stage.NarrativeView;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OptionsPane extends VBox {
    private final CharacterView characterView;
    private final DialogueView dialogueView;
    private final NarrativeView narrativeView;
    private final ScrollingPane scrollingPane;
    private MenuButton leftButton;
    private MenuButton rightButton;
    private Button flipButton;
    private Button genderButton;
    private Button speechButton;
    private Button thoughtButton;
    private Button topNarrativeButton;
    private Button bottomNarrativeButton;
    private Button saveButton;
    private Button deleteButton;

    public OptionsPane(CharacterView characterView, DialogueView dialogueView,
                       NarrativeView narrativeView, ScrollingPane scrollingPane) {
        this.characterView = characterView;
        this.dialogueView = dialogueView;
        this.narrativeView = narrativeView;
        this.scrollingPane = scrollingPane;
        initLeftAndRightButton();
        initFlipButton();
        initGenderButton();
        initSpeechButton();
        initThoughtButton();
        initNarrativeButtons();
        initSaveButton();
        initDeleteButton();

        GridPane optionsPane = new GridPane();

        optionsPane.add(leftButton, 0, 0, 1, 1);
        optionsPane.add(rightButton, 1, 0, 1, 1);
        optionsPane.add(flipButton, 0, 1, 1, 1);
        optionsPane.add(genderButton, 1, 1, 1, 1);
        optionsPane.add(speechButton, 0, 2, 1, 1);
        optionsPane.add(topNarrativeButton, 1, 2, 1, 1);
        optionsPane.add(thoughtButton, 0, 3, 1, 1);
        optionsPane.add(bottomNarrativeButton, 1, 3, 1, 1);
        optionsPane.add(saveButton, 0, 4, 1, 1);
        optionsPane.add(deleteButton, 1, 4, 1, 1);

        optionsPane.setHgap(5);
        optionsPane.setVgap(5);

        this.getChildren().add(optionsPane);
    }

    private void initSaveButton() {
        saveButton = new Button("", new ImageView("/buttons/save_button.png"));
        saveButton.setTooltip(new Tooltip("Save Scene"));
        saveButton.setMinWidth(82);
        saveButton.setOnMouseClicked((e) -> scrollingPane.saveToScrollingPane());
    }

    private void initDeleteButton() {
        deleteButton = new Button("", new ImageView("/buttons/delete_button.png"));
        deleteButton.setTooltip(new Tooltip("Delete Scene"));
        deleteButton.setMinWidth(82);
        deleteButton.setOnMouseClicked((e) -> scrollingPane.deleteFromScrollingPane());
    }

    private void initNarrativeButtons() {
        topNarrativeButton = new Button("", new ImageView("/buttons/text1_button.png"));
        topNarrativeButton.setTooltip(new Tooltip("Add Text Above"));
        topNarrativeButton.setMinWidth(82);
        topNarrativeButton.setOnMouseClicked((e) -> narrativeView.toggleNarrativeBar(Selection.IS_TOP));
        topNarrativeButton.setFocusTraversable(false);

        bottomNarrativeButton = new Button("", new ImageView("/buttons/text2_button.png"));
        bottomNarrativeButton.setTooltip(new Tooltip("Add Text Below"));
        bottomNarrativeButton.setMinWidth(82);
        bottomNarrativeButton.setOnMouseClicked((e) -> narrativeView.toggleNarrativeBar(Selection.IS_BOTTOM));
        bottomNarrativeButton.setFocusTraversable(false);
    }

    private void initThoughtButton() {
        thoughtButton = new Button("", new ImageView("/buttons/bubble2_button.png"));
        thoughtButton.setTooltip(new Tooltip("Add Thought Bubble"));
        thoughtButton.setFocusTraversable(false);
        thoughtButton.setOnMouseClicked((e) -> dialogueView.toggleFocusedDialogue(DialogueType.THOUGHT));
        thoughtButton.setMinWidth(82);
    }

    private void initSpeechButton() {
        speechButton = new Button("", new ImageView("/buttons/bubble1_button.png"));
        speechButton.setTooltip(new Tooltip("Add Speech Bubble"));
        speechButton.setFocusTraversable(false);
        speechButton.setOnMouseClicked((e) -> dialogueView.toggleFocusedDialogue(DialogueType.SPEECH));
        speechButton.setMinWidth(82);
    }

    private void initFlipButton() {
        flipButton = new Button("", new ImageView("/buttons/mirror_button.png"));
        flipButton.setTooltip(new Tooltip("Mirror Image"));
        flipButton.setFocusTraversable(false);
        flipButton.setOnMouseClicked((e) -> characterView.flipSelectedCharacterImage());
        flipButton.setMinWidth(82);
    }

    private void initLeftAndRightButton() {
        leftButton = new MenuButton("", new ImageView("/buttons/left_button.png"));
        leftButton.setTooltip(new Tooltip("Edit Left Side"));
        rightButton = new MenuButton("", new ImageView("/buttons/right_button.png"));
        rightButton.setTooltip(new Tooltip("Edit Right Side"));
        leftButton.setFocusTraversable(false);
        rightButton.setFocusTraversable(false);
        try {
            leftButton.getItems().add(loadCharactersMenuItem(Selection.IS_LEFT));
            rightButton.getItems().add(loadCharactersMenuItem(Selection.IS_RIGHT));
        } catch (IOException | URISyntaxException ioException) {
            System.out.println("Cannot load characters.");
        }
        leftButton.setMaxWidth(82);
        rightButton.setMaxWidth(82);
    }

    private void initGenderButton() {
        genderButton = new Button("", new ImageView("/buttons/gender_button.png"));
        genderButton.setTooltip(new Tooltip("Gender Change"));
        genderButton.setFocusTraversable(false);
        genderButton.setOnMouseClicked((e) -> characterView.changeGender());
        genderButton.setMinWidth(82);
    }

    private CustomMenuItem loadCharactersMenuItem(Selection selection) throws IOException, URISyntaxException {
        List<String> files = FileLoader.getFileNames(Constants.CHARACTER_FOLDER);
        Collections.sort(files);
        files.add(0, Constants.BLANK_IMAGE);
        ListView<String> listView = new ListView<>(FXCollections.observableList(files));
        listView.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (name.equals(Constants.BLANK_IMAGE)) {
                    setText(Constants.BLANK_IMAGE);
                    setGraphic(null);
                } else {
                    Image iconImage = new Image(Objects.requireNonNull(getClass()
                            .getResourceAsStream(String.format("/%s/%s", Constants.CHARACTER_FOLDER, name))));
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setSmooth(true);
                    imageView.setImage(iconImage);
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
        // update character listener
        listView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> characterView.updateCharacterImage(newValue, selection));
        FilteredList<String> filteredData = new FilteredList<>(listView.getItems(), s -> true);
        TextField filterInput = new TextField();
        filterInput.textProperty().addListener(obs -> {
            String filter = filterInput.getText();
            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            } else {
                filteredData.setPredicate(s -> s.contains(filter));
            }
        });
        listView.setItems(filteredData);
        BorderPane content = new BorderPane(listView);
        content.setTop(filterInput);
        return new CustomMenuItem(content, true);
    }
}
