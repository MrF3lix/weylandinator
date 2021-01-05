package ch.weylandinator.model;

public class CircuitElement {
    private CircuitElementType type;
    private String name;
    private int startPosition;
    private int endPosition;

    public CircuitElement() {}

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
}
