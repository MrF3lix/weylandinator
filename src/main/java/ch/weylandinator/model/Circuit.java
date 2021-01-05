package ch.weylandinator.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Circuit {
    private ArrayList<Element> elements = new ArrayList<>();

    public Circuit() {}

    public void addElementToCircuit(Element element) {
        elements.add(element);
    }

    public void removeElementToCircuit(String elementName) {
        Iterator<Element> it = elements.iterator();

        while(it.hasNext()) {
            if(it.next().getName() == elementName) {
                it.remove();
                return;
            }
        }
    }

    public Element getElementByName(String elementName) {
        Iterator<Element> it = elements.iterator();

        while(it.hasNext()) {
            Element element = it.next();
            if(element.getName() == elementName) {
                return element;
            }
        }

        return null;
    }

    public int getStartNumber() {
        int lowestStartNumber = Integer.MAX_VALUE;
        for(Element element : elements) {
            if(lowestStartNumber > element.getStartPosition()) {
                lowestStartNumber = element.getStartPosition();
            }
        }

        return lowestStartNumber;
    }

    public int getEndNumber() {
        int highestEndNumber = -1;
        for(Element element : elements) {
            if(highestEndNumber < element.getEndPosition()) {
                highestEndNumber = element.getEndPosition();
            }
        }

        return highestEndNumber;
    }
}