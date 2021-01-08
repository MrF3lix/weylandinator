package ch.weylandinator.state;

import java.util.ArrayList;
import java.util.List;

import ch.weylandinator.model.CircuitElement;

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
        return findElementByName(rootElement.getChildElements(), name);
    }

    private CircuitElement findElementByName(List<CircuitElement> elements, String name) {
        CircuitElement elementFound = null;

        for(CircuitElement element : elements) {
            if(name.equals(element.getName())) {
                elementFound = element;
            } else {
                elementFound = findElementByName(element.getChildElements(), name);
            }
        }

        return elementFound;
    }

    public List<CircuitElement> getAllElements() {
        return getAllElements(rootElement.getChildElements());
    }

    private List<CircuitElement> getAllElements(List<CircuitElement> elements) {
        List<CircuitElement> collected = new ArrayList<CircuitElement>(elements);

        for(CircuitElement element : elements) {
            if(element.getChildElements().size() > 0) {
                collected.addAll(getAllElements(element.getChildElements()));
            }
        }

        return collected;
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
}
