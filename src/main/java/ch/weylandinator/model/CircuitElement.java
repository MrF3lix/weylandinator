package ch.weylandinator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircuitElement {
    private CircuitElementType type;
    private String name;

    // Volt
    private double voltage;

    // Amper
    private double current;

    // Ohm
    private double resistance;

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

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    } 

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    } 

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
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
