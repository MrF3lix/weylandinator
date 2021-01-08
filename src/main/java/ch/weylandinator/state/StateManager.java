package ch.weylandinator.state;

import java.util.ArrayList;
import java.util.Iterator;
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

    private Element rootElement = new Element("#ROOT");

    public StateManager() {
        notifyObservers();
    }

    public Element findElementByName(String name) {
        return findElementByName(rootElement.getChildElements(), name);
    }

    private Element findElementByName(List<Element> elements, String name) {
        Element elementFound = null;

        for(Element element : elements) {
            if(name.equals(element.getName())) {
                elementFound = element;
            } else {
                elementFound = findElementByName(element.getChildElements(), name);
            }
        }

        return elementFound;
    }

    public List<Element> getAllElements() {
        return getAllElements(rootElement.getChildElements());
    }

    private List<Element> getAllElements(List<Element> elements) {
        List<Element> collected = new ArrayList<Element>(elements);

        for(Element element : elements) {
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

    public void addElementToCircuit(Element element) {
        rootElement.addChildElement(element);
        notifyObservers();
    }

    public void addElementToCircuit(String parentElementName, Element element) {
        Element parentElement = findElementByName(parentElementName);
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
