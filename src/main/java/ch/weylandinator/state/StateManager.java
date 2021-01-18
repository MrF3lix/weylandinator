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
        if(name == null) {
            return null;
        }

        return findElementByName(rootElement, name);
    }

    private CircuitElement findElementByName(CircuitElement element, String name) {
        CircuitElement searchElement = null;

        if(name.equals(element.getName())) {
            searchElement = element;
        }

        for(CircuitElement child : element.getChildElements()) {
            CircuitElement foundElement = findElementByName(child, name);
            if(foundElement != null) {
                searchElement = foundElement;
            }
        }

        return searchElement;
    }

    public List<CircuitElement> getAllElements() {
        return CircuitElementHelper.getFlatList(rootElement.getChildElements());
    }

    public void deleteElementByName(String elementName) {
        rootElement.deleteChildElement(elementName);
        notifyObservers();
    }

    public void addElementToCircuit(CircuitElement element) {
        CircuitElement parent = findElementByName(element.getParentName());
        if(parent != null) {
            parent.addChildElement(element);
        } else {
            rootElement.addChildElement(element);
        }

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
