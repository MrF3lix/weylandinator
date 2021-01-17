package ch.weylandinator.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.model.Position;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class CircuitCanvas {
    private int VOLTAGE_SOURCE_RADIUS = 25;

    private GraphicsContext gc;

    private Canvas canvas;

    private Map<CircuitElement, Position> canvasElements = new HashMap<>();

    private int totalRows = 0;
    private int totalCols = 0;

    private int curRow = 0;
    private int curCol = 0;

    public CircuitCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void update(CircuitElement root, double totalResistance) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Set main Voltage Source
        canvasElements = new HashMap<>();
        CircuitElement voltageSource = root.getChildElements().get(0);
        canvasElements.put(voltageSource, new Position(0, 0));

        curRow = -1;
        curCol = 1;
        setCanvasElements(voltageSource.getChildElements(), curRow);

        totalCols = canvasElements.entrySet().stream().max((x,y) -> x.getValue().getCol() - y.getValue().getCol()).get().getValue().getCol()+1;
        totalRows = canvasElements.entrySet().stream().max((x,y) -> x.getValue().getRow() - y.getValue().getRow()).get().getValue().getRow()+1;

        showCircuitInformation(totalResistance);

        printCircuit();
    }

    private void setCanvasElements(List<CircuitElement> elements, int level) {
        level++;
        if(elements.size() > 1) {
            curCol--;
        } 

        for(CircuitElement element : elements) {
            if(elements.size() > 1) {
                //TODO add connection element
                canvasElements.put(element, new Position(level, ++curCol));
            } else {
                canvasElements.put(element, new Position(level, curCol));
            }
            setCanvasElements(element.getChildElements(), level);
        }
    }

    private void showCircuitInformation(double totalResistance) {
        if (totalResistance >= 0) {
            gc.fillText("R - Gesamtwiederstand: " + String.format("%.2f", totalResistance) + " Ohm", 10, 10);
        }
    }

    private void printCircuit() {
        Iterator<Entry<CircuitElement, Position>> it = canvasElements.entrySet().iterator();

        while (it.hasNext()) {
            Entry<CircuitElement, Position> element = it.next();

            switch (element.getKey().getType()) {
                case VOLTAGE_SOURCE:
                    printVoltageSource(element.getKey(), element.getValue().getCoords());
                    break;
                case RESISTOR:
                    printResistor(element.getKey(), element.getValue().getCoords());
                default:
                    break;
            }
        }

        drawConnections();
    }

    private void printVoltageSource(CircuitElement element, Point2D location) {
        int radius = VOLTAGE_SOURCE_RADIUS;
        int padding = 25;
        double x = location.getX();
        double y = location.getY();

        gc.setLineWidth(2.0);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.strokeLine(x, y - radius - padding, x, y + radius + padding);
        gc.fillText(element.getName(), x + radius + padding, y);
    }

    private void printResistor(CircuitElement element, Point2D location) {
        int width = 20;
        int height = 80;
        int padding = 10;
        double x = location.getX();
        double y = location.getY();

        gc.strokeLine(x, y - height / 2 - padding, x, y - height / 2);
        gc.strokeRect(x - width / 2, y - height / 2, width, height);
        gc.strokeLine(x, y + height / 2 + padding, x, y + height / 2);
        gc.fillText(element.getName(), x + width, y);
    }

    private void drawConnections() {
        double width = Position.getColX(totalCols-1);

        double top = 50;
        double bottom = Position.getRowY(totalRows-1)+50;

        gc.strokeLine(70, top, width, top);
        gc.strokeLine(70, bottom, width, bottom);
        gc.strokeLine(Position.getColX(0), top, Position.getColX(0), bottom);
    }
}
