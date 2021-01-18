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
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
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
    private ChoiceBox<String> elementNames, parentElementOfNewElement, parentElementOfSelectedElement;

    @FXML
    private TextField name, start, end, elementValue, resistance, voltage, current;

    private CircuitCanvas canvas;

    @FXML
    private AnchorPane canvasContainer;

    private CircuitElement selectedElement;

    public MainController() {
        stateManager.addObserver(this);
    }

    /**
     * The update method is called every time the StateManager detects a change in
     * the circuit.
     */
    @Override
    public void updated() {

        double totalResistance;
        try {
            totalResistance = new ResistanceCalculator(stateManager.getRootElement()).getTotalResistance();
        } catch (Exception e) {
            totalResistance = -1;
        }

        canvas.setRoot(stateManager.getRootElement());
        canvas.setTotalResistance(totalResistance);

        if (selectedElement != null) {
            canvas.setSelectedElementName(selectedElement.getName());
        }

        updateElements();
        canvas.draw();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.canvas = new CircuitCanvas();
        canvasContainer.getChildren().add(canvas);

        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());

        canvas.setOnMouseClicked(event -> {
            String clickedElement = canvas.getClosesElement(new Point2D(event.getX(), event.getY()));
            selectElement(clickedElement);
        });

        elementType.getItems().setAll(CircuitElementType.values());
        elementType.getSelectionModel().selectFirst();

        fillCircuit();

        elementNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prevSelected, String selected) {
                selectElement(selected);
            }
        });
    }

    public void add_onAction() {
        try {

            String elementName = name.getText();
            if (elementName == null || elementName == "") {
                throw new IllegalArgumentException("Name cannot be empty!");
            }

            if (stateManager.findElementByName(elementName) != null) {
                throw new IllegalArgumentException("Name already exists!");
            }

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

            String parentElementName = parentElementOfNewElement.getSelectionModel().getSelectedItem();
            if (parentElementName != null) {
                newElement.setParentName(parentElementName);
            } else {
                if (stateManager.getAllElements().size() > 0) {
                    throw new IllegalArgumentException("Element needs a parent element!");
                }
            }

            stateManager.addElementToCircuit(newElement);

            name.clear();
            elementValue.clear();

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    public void update_onAction() {
        try {
            selectedElement = stateManager.findElementByName(elementNames.getValue());

            selectedElement.setResistance(Double.parseDouble(resistance.getText()));
            selectedElement.setVoltage(Double.parseDouble(voltage.getText()));
            selectedElement.setCurrent(Double.parseDouble(current.getText()));

            String newParentElement = parentElementOfSelectedElement.getSelectionModel().getSelectedItem();
            if (!selectedElement.getParentName().equals(newParentElement)
                    && !selectedElement.getName().equals(newParentElement)) {
                CircuitElement newElement = new CircuitElement(selectedElement);

                stateManager.deleteElementByName(selectedElement.getName());

                newElement.setParentName(newParentElement);
                stateManager.addElementToCircuit(newElement);
            }

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    public void delete_onAction() {
        try {
            stateManager.deleteElementByName(selectedElement.getName());
            selectedElement = null;
            updateSelectedElementValue();
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

    private void selectElement(String name) {
        selectedElement = stateManager.findElementByName(name);
        updateSelectedElementValue();

        canvas.setSelectedElementName(name);
        canvas.draw();
    }

    private void updateSelectedElementValue() {
        if (selectedElement != null) {
            parentElementOfSelectedElement.setValue(selectedElement.getParentName());
            resistance.setText(String.valueOf(selectedElement.getResistance()));
            voltage.setText(String.valueOf(selectedElement.getVoltage()));
            current.setText(String.valueOf(selectedElement.getCurrent()));
        } else {
            resistance.clear();
            voltage.clear();
            current.clear();
            parentElementOfSelectedElement.getSelectionModel().clearSelection();
        }
    }

    private void updateElements() {
        elementNames.getItems()
                .setAll(stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));
        parentElementOfNewElement.getItems()
                .setAll(stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));

        parentElementOfSelectedElement.getItems()
                .setAll(stateManager.getAllElements().stream().map(n -> n.getName()).collect(Collectors.toList()));

        if (selectedElement != null) {
            elementNames.setValue(selectedElement.getName());
        }
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
        r1.setParentName("U");
        stateManager.addElementToCircuit(r1);
    }
}
