package ch.weylandinator.state;

import java.util.ArrayList;
import java.util.List;

import ch.weylandinator.model.Element;

public class StateManager {

    private static StateManager instance = null;

    public static StateManager getInstance() {
        if (instance == null)
            instance = new StateManager();

        return instance;
    }

    private List<CircuitObserver> observers = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    public StateManager() {
        notifyObservers();
    }

    public List<Element> getElements() {
        return elements;
    }

    public Element findElementByName(String name) {
        Element elementFound = null;

        for(Element element : this.elements) {
            if(name.equals(element.getName())) {
                elementFound = element;
                return elementFound;
            } else {
                elementFound = findElementByName(element.getChildElements(), name);
            }
        }

        return elementFound;
    }

    public Element findElementByName(List<Element> elements, String name) {
        Element elementFound = null;

        for(Element element : elements) {
            if(name.equals(element.getName())) {
                elementFound = element;
                return elementFound;
            } else {
                elementFound = findElementByName(element.getChildElements(), name);
            }
        }

        return elementFound;
    }

    public List<Element> getAllElements() {
        List<Element> collected = new ArrayList<Element>(this.elements);
        for(Element element : this.elements) {
            collected.addAll(element.getChildElements());
        }

        return collected;
    }

    public List<Element> getAllElements(List<Element> elements) {
        List<Element> collected = new ArrayList<Element>(elements);
        for(Element element : elements) {
            collected.addAll(element.getChildElements());
        }

        return collected;
    }

    public void addElementToCircuit(Element element) {
        elements.add(element);
        notifyObservers();
    }

    public void addElementToCircuit(Element parentElement, Element element) {
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
