package ch.weylandinator.model;

public class Element {
    private ElementType type;
    private String name;
    private int startPosition;
    private int endPosition;
    private int value;

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    } 
}
