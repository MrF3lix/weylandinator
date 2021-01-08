package ch.weylandinator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircuitElement {
    private CircuitElementType type;
    private String name;

    // Volt
    private int voltage;

    // Amper
    private int current;

    // Ohm
    private int resistance;

    private List<CircuitElement> childElements = new ArrayList<>();

    public CircuitElement() {}

    public CircuitElement(String name) {
        this.name = name;
    }

    public CircuitElementType getType() {
        return type;
    }

    public void setType(CircuitElementType type) {
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

    public void addChildElement(CircuitElement element) {
        childElements.add(element);
    }

    public void deleteChildElement(String elementName) {
        for (CircuitElement childElement : childElements) {
            childElement.deleteChildElement(elementName);
        }

        Iterator<CircuitElement> it = childElements.iterator();
        while(it.hasNext()) {
            CircuitElement next = it.next();

            if(elementName.equals(next.getName())) {
                it.remove();
            }
        }
    }

    public List<CircuitElement> getChildElements() {
        return childElements;
    }
}
