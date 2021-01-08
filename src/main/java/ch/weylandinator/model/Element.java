package ch.weylandinator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Element {
    private ElementType type;
    private String name;

    // Volt
    private int voltage;

    // Amper
    private int current;

    // Ohm
    private int resistance;

    private List<Element> childElements = new ArrayList<>();

    public Element() {}

    public Element(String name) {
        this.name = name;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    } 

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    } 

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    } 

    public void addChildElement(Element element) {
        childElements.add(element);
    }

    public void deleteChildElement(String elementName) {
        for (Element childElement : childElements) {
            childElement.deleteChildElement(elementName);
        }

        Iterator<Element> it = childElements.iterator();
        while(it.hasNext()) {
            Element next = it.next();

            if(elementName.equals(next.getName())) {
                it.remove();
            }
        }
    }

    public List<Element> getChildElements() {
        return childElements;
    }
}
