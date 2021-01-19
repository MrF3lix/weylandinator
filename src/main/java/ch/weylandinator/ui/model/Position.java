package ch.weylandinator.ui.model;

import javafx.geometry.Point2D;

public class Position {

    private int row;

    private int col;

    private boolean isReversed;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
        this.isReversed = false;
    }

    public Position(int row, int col, boolean isReversed) {
        this.row = row;
        this.col = col;
        this.isReversed = isReversed;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public Point2D getCoords() {
        return new Point2D(getColX(col), getRowY(row));
    }

    public boolean isNearPosition(Point2D otherCoords) {
        Point2D coords = getCoords();

        if(Math.abs(coords.getX() - otherCoords.getX()) < 50 && Math.abs(coords.getY() - otherCoords.getY()) < 50 ) {
            return true;
        }
        return false;
    }
    
    public static double getColX(int index) {
        return index * 170 + 70;
    }

    public static double getRowY(int index) {
        return index*100+100;
    }
}
