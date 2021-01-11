package ch.weylandinator.state;

import java.util.ArrayList;
import java.util.List;

import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.util.CircuitElementHelper;

public class StateManager {

    private static StateManager instance = null;

    public static StateManager getInstance() {
        if (instance == null)
            instance = new StateManager();

        return instance;
    }

    private List<CircuitObserver> observers = new ArrayList<>();

    private CircuitElement rootElement = new CircuitElement("#ROOT");

    public StateManager() {
        notifyObservers();
    }

    public CircuitElement findElementByName(String name) {
        return findElementByName(rootElement, name);
    }

    private CircuitElement findElementByName(CircuitElement element, String name) {

        if(name.equals(element.getName())) {
            return element;
        }

        for(CircuitElement child : element.getChildElements()) {
            if(name.equals(child.getName())) {
                return child;
            }
        }

        for(CircuitElement child : element.getChildElements()) {
            return findElementByName(child, name);
        }
        
        return null;
    }

    public List<CircuitElement> getAllElements() {
        return CircuitElementHelper.getFlatList(rootElement.getChildElements());
    }

    public void deleteElementByName(String elementName) {
        rootElement.deleteChildElement(elementName);
        notifyObservers();
    }

    public void addElementToCircuit(CircuitElement element) {
        rootElement.addChildElement(element);
        notifyObservers();
    }

    public void addElementToCircuit(String parentElementName, CircuitElement element) {
        CircuitElement parentElement = findElementByName(parentElementName);
        parentElement.addChildElement(element);

        notifyObservers();
    }

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

    public CircuitElement getRootElement() {
        return rootElement;
    }
}
