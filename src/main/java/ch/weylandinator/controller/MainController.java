package ch.weylandinator.controller;

import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.model.CircuitElementType;
import ch.weylandinator.state.CircuitObserver;
import ch.weylandinator.state.StateManager;
import ch.weylandinator.util.Calculator;
import ch.weylandinator.util.CircuitCanvas;
import ch.weylandinator.util.Formula;
import ch.weylandinator.util.ResistanceCalculator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ch.weylandinator.model.CircuitElementType.RESISTOR;
import static ch.weylandinator.model.CircuitElementType.VOLTAGE_SOURCE;

public class MainController implements Initializable, CircuitObserver {
    private StateManager stateManager = StateManager.getInstance();

    @FXML
    private ChoiceBox<CircuitElementType> elementType;

    @FXML
    private ChoiceBox<String> elementNames, availableElements;

    @FXML
    private TextField name, start, end, elementValue, resistance, voltage, current;

    @FXML
    private Canvas canvas;

    private CircuitElement selectedElement;

    private CircuitCanvas circuitCanvas;

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

        double totalResistance;
        try {
            totalResistance = new ResistanceCalculator(stateManager.getRootElement()).getTotalResistance();
        } catch (Exception e) {
            totalResistance = -1;
        }

        List<CircuitElement> elements = stateManager.getAllElements();
        circuitCanvas.update(elements, totalResistance);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        circuitCanvas = new CircuitCanvas(canvas);

        elementType.getItems().setAll(CircuitElementType.values());
        elementType.getSelectionModel().selectFirst();

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
            CircuitElement newElement = new CircuitElement();
            newElement.setName(name.getText());
            newElement.setType(elementType.getSelectionModel().getSelectedItem());

            switch (newElement.getType()) {
                case RESISTOR:
                    newElement.setResistance(Double.parseDouble(elementValue.getText()));
                    break;
                case VOLTAGE_SOURCE:
                    newElement.setVoltage(Double.parseDouble(elementValue.getText()));
                    break;
                case LOAD:
                    newElement.setCurrent(Double.parseDouble(elementValue.getText()));
                    break;
                default:
                    break;
            }

            String parentElementName = availableElements.getSelectionModel().getSelectedItem();
            if (parentElementName != null) {
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
            selectedElement.setResistance(Double.parseDouble(resistance.getText()));
            selectedElement.setVoltage(Double.parseDouble(voltage.getText()));
            selectedElement.setCurrent(Double.parseDouble(current.getText()));

            // TODO use observer
            updated();
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
        calculator.solveCircuit(stateManager.getAllElements());

        Formula formula = Formula.URI;
        Map<String, Double> variableMap = new HashMap<>();
        variableMap.put("R", 1.4);
        variableMap.put("I", 100.0);

        updated();
    }

    private void updateSelectedElementValue() {
        resistance.setText(String.valueOf(selectedElement.getResistance()));
        voltage.setText(String.valueOf(selectedElement.getVoltage()));
        current.setText(String.valueOf(selectedElement.getCurrent()));
    }

    private void updateElements() {
        elementNames.getItems()
                .setAll(stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));
        availableElements.getItems()
                .setAll(stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));
    }

    private void fillCircuit() {
        CircuitElement e1 = new CircuitElement();
        e1.setName("U");
        e1.setType(VOLTAGE_SOURCE);
        stateManager.addElementToCircuit(e1);

        CircuitElement r1 = new CircuitElement();
        r1.setName("r1");
        r1.setType(RESISTOR);
        r1.setResistance(1000d);
        stateManager.addElementToCircuit("U", r1);

        CircuitElement r2 = new CircuitElement();
        r2.setName("r2");
        r2.setType(RESISTOR);
        r2.setResistance(500d);
        stateManager.addElementToCircuit("r1", r2);
    }
}
