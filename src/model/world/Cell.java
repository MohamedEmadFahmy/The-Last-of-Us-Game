package model.world;

public abstract class Cell {
    private boolean isVisible;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean x) {
        this.isVisible = x;
    }

    public Cell() {

    }

    // public Cell(boolean isVisible) { feeh ala2
    // this.isVisible = isVisible;
    // }
}
