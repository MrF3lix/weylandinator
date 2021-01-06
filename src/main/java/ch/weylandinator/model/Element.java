package ch.weylandinator.model;

public class Element {
    private ElementType type;
    private String name;
    private int startPosition;
    private int endPosition;

    // Volt
    private int voltage;

    // Amper
    private int current;

    // Ohm
    private int resistance;

    public Element() {}

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

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    } 

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
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
}
