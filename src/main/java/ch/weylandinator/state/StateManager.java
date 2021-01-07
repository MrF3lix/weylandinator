package ch.weylandinator.state;

import java.util.ArrayList;
import java.util.List;

import ch.weylandinator.model.Circuit;
import ch.weylandinator.model.Element;

public class StateManager {

    private static StateManager instance = null;

    public static StateManager getInstance() {
        if (instance == null)
            instance = new StateManager();

        return instance;
    }

    private Circuit circuit;
    private List<CircuitObserver> observers = new ArrayList<>();

    public StateManager() {
        circuit = new Circuit();
        notifyObservers();
    }

    public void removeElementFromCircuit(String elementName) {
        circuit.removeElementFromCircuit(elementName);
        notifyObservers();
    }

    public void addElementToCircuit(Element element) {
        circuit.addElementToCircuit(element);
        notifyObservers();
    }

    public String getState() {
        return circuit.toString();
    }

    public Circuit getCircuit() {
        return circuit;
    };

    public void addObserver(CircuitObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CircuitObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for(CircuitObserver observer : observers) {
            observer.updated();
        }
    }
}
