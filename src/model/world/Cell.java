package model.world;

public abstract class Cell {
    private boolean isVisible;

    public Cell() {
        isVisible = false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean x) {
        this.isVisible = x;
    }

}
