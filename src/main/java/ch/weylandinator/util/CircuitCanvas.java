package ch.weylandinator.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.model.Position;
import ch.weylandinator.model.ResizableCanvas;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircuitCanvas extends ResizableCanvas {
    private int VOLTAGE_SOURCE_RADIUS = 25;

    private GraphicsContext gc;

    private Map<CircuitElement, Position> canvasElements = new HashMap<>();

    private int totalRows = 0;
    private int totalCols = 0;

    private int curRow = 0;
    private int curCol = 0;

    private CircuitElement root;
    private double totalResistance;

    private String selectedElementName;

    public CircuitCanvas() {
        this.gc = getGraphicsContext2D();
    }
    
    @Override
    public void draw() {
        update();
    }

    public void setRoot(CircuitElement root) {
        this.root = root;
    }

    public void setTotalResistance(double totalResistance) {
        this.totalResistance = totalResistance;
    }

    public void setSelectedElementName(String selectedElementName) {
        this.selectedElementName = selectedElementName;
    }

    public String getClosesElement(Point2D clickLocation) {

        for(Entry<CircuitElement, Position> canvasElement : canvasElements.entrySet()) {

            if(canvasElement.getValue().isNearPosition(clickLocation)) {
                return canvasElement.getKey().getName();
            }

        }

        return null;
    }

    private void update() {
        gc.clearRect(0, 0, getWidth(), getHeight());

        if(root == null) {
            return;
        }

        //Set main Voltage Source
        canvasElements = new HashMap<>();
        CircuitElement voltageSource = root.getChildElements().get(0);
        canvasElements.put(voltageSource, new Position(0, 0, true));

        curRow = -1;
        curCol = 1;
        setCanvasElements(voltageSource.getChildElements(), curRow);

        totalCols = canvasElements.entrySet().stream().max((x,y) -> x.getValue().getCol() - y.getValue().getCol()).get().getValue().getCol()+1;
        totalRows = canvasElements.entrySet().stream().max((x,y) -> x.getValue().getRow() - y.getValue().getRow()).get().getValue().getRow()+1;

        showCircuitInformation();

        printCircuit();
    }

    private void setCanvasElements(List<CircuitElement> elements, int level) {
        level++;
        if(elements.size() > 1) {
            curCol--;
        } 

        for(CircuitElement element : elements) {
            if(elements.size() > 1) {
                canvasElements.put(element, new Position(level, ++curCol));
            } else {
                canvasElements.put(element, new Position(level, curCol));
            }
            setCanvasElements(element.getChildElements(), level);
        }
    }

    private void showCircuitInformation() {
        if (totalResistance >= 0) {
            gc.fillText("R - Gesamtwiederstand: " + String.format("%.2f", totalResistance) + " Ohm", 10, 25);
        }
    }

    private void printCircuit() {
        Iterator<Entry<CircuitElement, Position>> it = canvasElements.entrySet().iterator();

        while (it.hasNext()) {
            Entry<CircuitElement, Position> element = it.next();

            for(CircuitElement child : element.getKey().getChildElements()) {
                Optional<Entry<CircuitElement, Position>> childElement = canvasElements.entrySet().stream().filter(c -> child.getName().equals(c.getKey().getName())).findFirst();

                if(childElement.isPresent()) {
                    drawConnection(element.getValue(), childElement.get().getValue());
                }
            }

            if(element.getKey().getName().equals(selectedElementName)) {
                gc.setStroke(Color.BLUE);
            }

            switch (element.getKey().getType()) {
                case VOLTAGE_SOURCE:
                    printVoltageSource(element.getKey(), element.getValue().getCoords());
                    break;
                case RESISTOR:
                    printResistor(element.getKey(), element.getValue().getCoords());
                default:
                    break;
            }

            gc.setStroke(Color.BLACK);
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

        if(element.getChildElements().size() == 0) {
            double bottom = Position.getRowY(totalRows-1)+50;
            gc.strokeLine(x, y + height / 2, x,bottom);
        }
    }

    private void drawConnection(Position startPosition, Position endLocation) {
        Point2D start = startPosition.getCoords();
        Point2D end = endLocation.getCoords();

        if(startPosition.isReversed()) {
            gc.strokeLine(start.getX(), start.getY()-50, end.getX(), end.getY()-50);
        } else {
            gc.strokeLine(start.getX(), start.getY()+50, end.getX(), end.getY()-50);
        }
    }

    private void drawConnections() {
        double width = Position.getColX(totalCols-1);
        double top = 50;
        double bottom = Position.getRowY(totalRows-1)+50;

        gc.strokeLine(70, bottom, width, bottom);
        gc.strokeLine(Position.getColX(0), top, Position.getColX(0), bottom);
    }
}
