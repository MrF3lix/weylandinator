package ch.weylandinator.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Circuit {
    private ArrayList<CircuitElement> elements = new ArrayList<>();

    public Circuit() {}

    public void addElementToCircuit(CircuitElement element) {
        elements.add(element);
    }

    public void removeElementToCircuit(String elementName) {
        Iterator<CircuitElement> it = elements.iterator();

        while(it.hasNext()) {
            if(it.next().getName() == elementName) {
                it.remove();
                return;
            }
        }
    }

    public CircuitElement getElementByName(String elementName) {
        Iterator<CircuitElement> it = elements.iterator();

        while(it.hasNext()) {
            CircuitElement element = it.next();
            if(element.getName() == elementName) {
                return element;
            }
        }

        return null;
    }

    public int getStartNumber() {
        int lowestStartNumber = Integer.MAX_VALUE;
        for(CircuitElement element : elements) {
            if(lowestStartNumber > element.getStartPosition()) {
                lowestStartNumber = element.getStartPosition();
            }
        }

        return lowestStartNumber;
    }

    public int getEndNumber() {
        int highestEndNumber = -1;
        for(CircuitElement element : elements) {
            if(highestEndNumber < element.getEndPosition()) {
                highestEndNumber = element.getEndPosition();
            }
        }

        return highestEndNumber;
    }
}