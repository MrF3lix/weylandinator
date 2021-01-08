package ch.weylandinator.controller;

import ch.weylandinator.model.Element;
import ch.weylandinator.model.ElementType;
import ch.weylandinator.state.CircuitObserver;
import ch.weylandinator.state.StateManager;
import ch.weylandinator.util.Calculator;
import ch.weylandinator.util.Formula;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ch.weylandinator.model.ElementType.RESISTOR;
import static ch.weylandinator.model.ElementType.VOLTAGE_SOURCE;

public class MainController implements Initializable, CircuitObserver
{
    private StateManager stateManager = StateManager.getInstance();
    private GraphicsContext gc;

    @FXML
    private ChoiceBox<ElementType> elementType;

    @FXML
    private ChoiceBox<String> elementNames, availableElements;

    @FXML
    private TextField name, start, end, elementValue, resistance, voltage, current;

    @FXML
    private Canvas canvas;

    private Element selectedElement;

    private double resultCurrent;

    public MainController() {
        stateManager.addObserver(this);
    }

    /**
     * The update method is called every time the StateManager detects a change in
     * the circuit.
     */
    @Override
    public void updated() {
        updateElements();
        displayCircuit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        elementType.getItems().setAll(ElementType.values());
        elementType.getSelectionModel().selectFirst();

        gc = canvas.getGraphicsContext2D();

        fillCircuit();

        elementNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prevSelected,
                    String selected) {
                selectedElement = stateManager.findElementByName(selected);
                updateSelectedElementValue();
            }
        });
    }

    public void add_onAction() {
        try {
            Element newElement = new Element();
            newElement.setName(name.getText());
            newElement.setType(elementType.getSelectionModel().getSelectedItem());

            switch (newElement.getType()) {
                case RESISTOR:
                    newElement.setResistance(Integer.parseInt(elementValue.getText()));
                    break;
                case VOLTAGE_SOURCE:
                    newElement.setVoltage(Integer.parseInt(elementValue.getText()));
                    break;
                case LOAD:
                    newElement.setCurrent(Integer.parseInt(elementValue.getText()));
                    break;
                default:
                    break;
            }

            String parentElementName = availableElements.getSelectionModel().getSelectedItem();
            if(parentElementName != null) {
                stateManager.addElementToCircuit(parentElementName, newElement);
            } else {
                stateManager.addElementToCircuit(newElement);
            }

            name.clear();
            elementValue.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText("Make sure to only use numeric values on the Value field.");

            alert.showAndWait();
        }
    }

    public void update_onAction() {
        try {
            selectedElement.setResistance(Integer.parseInt(resistance.getText()));
            selectedElement.setVoltage(Integer.parseInt(voltage.getText()));
            selectedElement.setCurrent(Integer.parseInt(current.getText()));
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText("Input fields only allow numeric values.");

            alert.showAndWait();
        }
    }

    public void delete_onAction() {
        try {
            stateManager.deleteElementByName(selectedElement.getName());
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText("Make sure to select an Element before deleting it.");

            alert.showAndWait();
        }
    }

    public void solve_onAction() {
        Calculator calculator = new Calculator();
        
        // ---> 
        
        calculator.solveCircuit(stateManager.getAllElements());
        

        Formula formula = Formula.URI;
        Map<String, Double> variableMap = new HashMap<>();
        variableMap.put("R", 1.4);
        variableMap.put("I", 100.0);

        resultCurrent = calculator.solve(formula, variableMap, "U");
        updated();
    }

    private void updateSelectedElementValue() {
        resistance.setText(String.valueOf(selectedElement.getResistance()));
        voltage.setText(String.valueOf(selectedElement.getVoltage()));
        current.setText(String.valueOf(selectedElement.getCurrent()));
    }

    private void updateElements() {
        elementNames.getItems().setAll(
                stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));
        availableElements.getItems().setAll(
            stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));
    }

    private void displayCircuit() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        showCircuitInformation();

        List<Element> elements = stateManager.getAllElements();

        for (int i = 0; i < elements.size(); i++) {
            displayElement(getCanvasPosition(i), elements.get(i).getName());
        }

    }

    private void showCircuitInformation() {
        gc.fillText("U - Spannung: ??? V", 10, 10);
        gc.fillText("R - Wiederstand: ??? Ohm", 10, 30);
        gc.fillText("I - Strom: " + resultCurrent + " A", 10, 50);
    }

    private void displayElement(Point point, String name) {
        int width = 100;
        int height = 30;
        gc.strokeRoundRect(point.x - width / 2, point.y - height / 2, width, height, 10, 10);
        gc.fillText(name, point.x - width / 2 + 5, point.y - height / 2 + 20);
    }

    /**
     * This method will give you the coordinates of a point on a 5x5 point-grid.
     * 
     * 0 1 2 3 4 ... 5 6 7 8 9 ... etc
     * 
     * @param index
     * @return Point The coordinates
     */
    private Point getCanvasPosition(int index) {
        int gridSize = 4;

        int size = (int) canvas.getHeight();
        int spacing = Math.round(size / (gridSize + 1));

        return new Point(index % gridSize * spacing + spacing, (index / gridSize) * spacing + spacing);
    }

    // for testing
    private void fillCircuit() {
        Element e1 = new Element();
        e1.setName("Voltage Source");
        e1.setType(VOLTAGE_SOURCE);
        stateManager.addElementToCircuit(e1);

        Element r1 = new Element();
        r1.setName("r1");
        r1.setType(RESISTOR);
        stateManager.addElementToCircuit("Voltage Source", r1);

        Element r2 = new Element();
        r2.setName("r2");
        r2.setType(RESISTOR);
        stateManager.addElementToCircuit("r1", r2);
    }
}
