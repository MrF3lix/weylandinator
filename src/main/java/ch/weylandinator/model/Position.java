package ch.weylandinator.model;

import javafx.geometry.Point2D;

public class Position {

    private int row;

    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Point2D getCoords() {
        return new Point2D(getColX(col), getRowY(row));
    }
    
    public static double getColX(int index) {
        return index * 170 + 70;
    }

    public static double getRowY(int index) {
        return index*100+100;
    }
}
