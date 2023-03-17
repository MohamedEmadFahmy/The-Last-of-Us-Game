package model.world;

public class Cell {
    private boolean isVisible;

    public boolean isVisible() {
        return isVisible;
    }
    public void setVisible(boolean x) {
        this.isVisible = x;
    }
    public Cell() {

    }
    public Cell(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
