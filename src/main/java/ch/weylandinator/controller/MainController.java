package ch.weylandinator.controller;

import java.net.URL;
import java.util.ResourceBundle;

import ch.weylandinator.model.Element;
import ch.weylandinator.model.ElementType;
import ch.weylandinator.state.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController implements Initializable {
    private StateManager stateManager = StateManager.getInstance();

    @FXML
    private TextArea output = new TextArea();

    @FXML
    private ChoiceBox<ElementType> elementType = new ChoiceBox<>();

    @FXML
    private TextField name = new TextField();

    @FXML
    private TextField start = new TextField();

    @FXML
    private TextField end = new TextField();

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
    }
}
