package ie.ucd.apes.entity;

import javafx.scene.paint.Color;

import static ie.ucd.apes.entity.Constants.*;

public class Character {
    private String imageFileName;
    private boolean isFlipped;
    private boolean isMale;
    private Color skinColor;
    private Color hairColor;

    public Character(String imageFileName, boolean isFlipped, boolean isMale) {
        this.imageFileName = imageFileName;
        this.isFlipped = isFlipped;
        this.isMale = isMale;
        skinColor = DEFAULT_SKIN_COLOR;
        hairColor = DEFAULT_HAIR_COLOR;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public void flipOrientation() {
        isFlipped = !isFlipped;
    }

    public void changeGender() { isMale = !isMale; }

    public Color getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(Color skinColor) {
        this.skinColor = skinColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }
}
