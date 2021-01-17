package ch.weylandinator.util;

import java.util.Iterator;
import java.util.List;

import ch.weylandinator.model.CircuitElement;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class CircuitCanvas {
    private int VOLTAGE_SOURCE_RADIUS = 25;

    private GraphicsContext gc;

    private Canvas canvas;

    private List<CircuitElement> elements;

    private int cols;

    public CircuitCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void update(List<CircuitElement> elements, double totalResistance) {
        this.elements = elements;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        showCircuitInformation(totalResistance);

        printCircuit();
    }

    private void showCircuitInformation(double totalResistance) {
        if (totalResistance >= 0) {
            gc.fillText("R - Gesamtwiederstand: " + String.format("%.2f", totalResistance) + " Ohm", 10, 10);
        }
    }

    private void printCircuit() {
        cols = 0;
        Iterator<CircuitElement> it = elements.iterator();

        while (it.hasNext()) {
            CircuitElement element = it.next();

            switch (element.getType()) {
                case VOLTAGE_SOURCE:
                    printVoltageSource(element, getPosition());
                    break;
                case RESISTOR:
                    printResistor(element, getPosition());
                default:
                    break;
            }

            cols++;
        }
    }

    private void printVoltageSource(CircuitElement element, Point2D location) {
        int radius = VOLTAGE_SOURCE_RADIUS;
        int padding = 10;
        double x = location.getX();
        double y = location.getY();

        gc.setLineWidth(2.0);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.strokeLine(x, y - radius - padding, x, y + radius + padding);
        gc.fillText(element.getName(), x + radius + padding, y);
    }

    private void printResistor(CircuitElement element, Point2D location) {
        int width = 20;
        int height = 70;
        int padding = 10;
        double x = location.getX();
        double y = location.getY();

        gc.strokeLine(x, y - height / 2 - padding, x, y - height / 2);
        gc.strokeRect(x - width / 2, y - height / 2, width, height);
        gc.strokeLine(x, y + height / 2 + padding, x, y + height / 2);
        gc.fillText(element.getName(), x + width, y);
    }

    private Point2D getPosition() {
        // TODO improve layout
        return new Point2D(cols * 170 + 70, canvas.getHeight() / 2);
    }
}
