package ch.weylandinator.controller;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import ch.weylandinator.model.Circuit;
import ch.weylandinator.model.Element;
import ch.weylandinator.model.ElementType;
import ch.weylandinator.state.StateManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController implements Initializable {
    private StateManager stateManager = StateManager.getInstance();
    private GraphicsContext gc;

    @FXML
    private TextArea output;

    @FXML
    private ChoiceBox<ElementType> elementType;

    @FXML
    private ChoiceBox<String> elementNames;

    @FXML
    private TextField name, start, end, elementValue, resistance, voltage, current;

    @FXML
    private Canvas canvas;

    private Element selectedElement;

    public void add_onAction() {
        Element newElement = new Element();
        newElement.setName(name.getText());
        newElement.setType(elementType.getSelectionModel().getSelectedItem());
        newElement.setStartPosition(Integer.parseInt(start.getText()));
        newElement.setEndPosition(Integer.parseInt(end.getText()));

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

        stateManager.addElementToCircuit(newElement);

        name.clear();
        start.clear();
        end.clear();
        elementValue.clear();

        updateOutput();
        updateElements();
        displayCircuit();
    }

    public void update_onAction() {
        selectedElement.setResistance(Integer.parseInt(resistance.getText()));
        selectedElement.setVoltage(Integer.parseInt(voltage.getText()));
        selectedElement.setCurrent(Integer.parseInt(current.getText()));
    }

    public void delete_onAction() {
        stateManager.getCircuit().removeElementFromCircuit(selectedElement.getName());

        updateOutput();
        updateElements();
        displayCircuit();
    }

    public void updateOutput() {
        // TODO use Observable-Pattern for this
        output.setText(stateManager.getState());
    }

    public void updateSelectedElementValue() {
        // TODO use Observable-Pattern for this
        resistance.setText(String.valueOf(selectedElement.getResistance()));
        voltage.setText(String.valueOf(selectedElement.getVoltage()));
        current.setText(String.valueOf(selectedElement.getCurrent()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        elementType.getItems().setAll(ElementType.values());
        elementType.getSelectionModel().selectFirst();

        gc = canvas.getGraphicsContext2D();

        fillCircuit();
        displayCircuit();

        updateOutput();
        updateElements();

        elementNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prevSelected,
                    String selected) {
                selectedElement = stateManager.getCircuit().getElementByName(selected);
                updateSelectedElementValue();
            }
        });
    }

    public void updateElements() {
        elementNames.getItems().setAll(
                stateManager.getCircuit().getElements().stream().map(n -> n.getName()).collect(Collectors.toList()));
    }
    
    public void displayCircuit(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        List<Element> elements = stateManager.getCircuit().getElements();

        for (int i = 0; i < elements.size(); i++) {
            displayElement(getCanvasPosition(i), elements.get(i).getName());
        }
        
    }
    
    public void displayElement(Point point, String name){
        int width = 80;
        int height = 30;
        gc.strokeRoundRect(point.x - width/2 , point.y- height/2, width, height, 10, 10);
        gc.strokeText(name, point.x - width/2, point.y- height/2 + 20);
    }

    /**
     * This method will give you the coordinates
     * of a point on a 5x5 point-grid.
     * 
     * 0 1 2 3 4 ...
     * 5 6 7 8 9 ...
     * etc
     * 
     * @param index 
     * @return Point The coordinates
     */
    public Point getCanvasPosition(int index){
        int gridSize = 4;
        
        int size = (int)canvas.getHeight();
        int spacing = Math.round(size/(gridSize+1));
        
        return new Point(index%gridSize * spacing + spacing, (index/gridSize) * spacing + spacing);
    }

    // for testing
    private void fillCircuit() {
        Element e1 = new Element();
        e1.setName("Voltage Source");
        e1.setType(ElementType.VOLTAGE_SOURCE);
        e1.setStartPosition(1);
        e1.setEndPosition(2);
        stateManager.addElementToCircuit(e1);

        Element e2 = new Element();
        e2.setName("RESISTOR 1");
        e2.setType(ElementType.RESISTOR);
        e2.setStartPosition(2);
        e2.setEndPosition(3);
        stateManager.addElementToCircuit(e2);

        Element e3 = new Element();
        e3.setName("RESISTOR 2");
        e3.setType(ElementType.RESISTOR);
        e3.setStartPosition(3);
        e3.setEndPosition(4);
        stateManager.addElementToCircuit(e3);

        Element e4 = new Element();
        e4.setName("RESISTOR 3");
        e4.setType(ElementType.RESISTOR);
        e4.setStartPosition(4);
        e4.setEndPosition(5);
        stateManager.addElementToCircuit(e4);

        Element e5 = new Element();
        e5.setName("RESISTOR 4");
        e5.setType(ElementType.RESISTOR);
        e5.setStartPosition(3);
        e5.setEndPosition(5);
        stateManager.addElementToCircuit(e5);
    }
}
