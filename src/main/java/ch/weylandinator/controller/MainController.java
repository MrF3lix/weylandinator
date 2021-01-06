package ch.weylandinator.controller;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ch.weylandinator.model.Circuit;
import ch.weylandinator.model.Element;
import ch.weylandinator.model.ElementType;
import ch.weylandinator.state.StateManager;
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
    private TextField name, start, end;
    
    @FXML
    private Canvas canvas;

    public void add_onAction() {
        Element newElement = new Element();
        newElement.setName(name.getText());
        newElement.setType(elementType.getSelectionModel().getSelectedItem());
        newElement.setStartPosition(Integer.parseInt(start.getText()));
        newElement.setEndPosition(Integer.parseInt(end.getText()));

        stateManager.addElementToCircuit(newElement);

        name.clear();
        start.clear();
        end.clear();

        updateOutput();
    }

    public void updateOutput() {
        //TODO use Observable-Pattern for this
        output.setText(stateManager.getState());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        elementType.getItems().setAll(ElementType.values());
        elementType.getSelectionModel().selectFirst();

        gc = canvas.getGraphicsContext2D();
        
        fillCircuit();
        displayCircuit();
    }
    
    public void displayCircuit(){
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);

        List<Element> elements = stateManager.getCircuit().getElements();

        for (int i = 0; i < elements.size(); i++) {
            
        }
        
    }

    /**
     * This method will give you the coordinates
     * of a point on a 5x5 point-grid.
     * 
     * 0 1 2 3 4
     * 5 6 7 8 9
     * etc
     * 
     * @param index 
     * @return Point The coordinates
     */
    public Point getCanvasPosition(int index){
        int size = (int)canvas.getHeight();
        int spacing = (int)Math.round(size/6);
        
        return new Point((index*spacing)%size, (index%4 + 1) * spacing);
    }
    
    //for testing
    private void fillCircuit(){
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
