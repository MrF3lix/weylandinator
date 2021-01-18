package ch.weylandinator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.model.CircuitElementType;

public class CircuitElementHelper {
    public static List<CircuitElement> getFlatList(List<CircuitElement> elements) {
        List<CircuitElement> collected = new ArrayList<>();
        collected.addAll(elements);

        for (CircuitElement child : elements) {
            collected.addAll(getFlatList(child.getChildElements()));
        }

        return collected;
    }

    /**
     * Two elements are parallel if they have the same parent and the same child
     * element.
     * 
     * @return a pair of parallel elements
     */
    public static List<CircuitElement> getElementInParallel(CircuitElement element) {
        List<CircuitElement> directChildren = element.getChildElements();
        if (directChildren.size() >= 2 && getNumberOfResistors(directChildren) >= 2) {
            CircuitElement first = directChildren.get(0);
            CircuitElement second = directChildren.get(1);

            if (first.getChildElements().size() == 1 && second.getChildElements().size() == 1) {
                CircuitElement firstFirstChild = first.getChildElements().get(0);
                CircuitElement secondFirstChild = second.getChildElements().get(0);

                if (firstFirstChild.getName().equals(secondFirstChild.getName())) {
                    return Arrays.asList(first, second);
                }
            }

            if (first.getChildElements().size() == 0 && second.getChildElements().size() == 0) {
                return Arrays.asList(first, second);
            }
        }

        for (CircuitElement directChild : directChildren) {
            return getElementInParallel(directChild);
        }

        return new ArrayList<>();
    }

    /**
     * Two elements are serial if they both have exactly one child element.
     * 
     * @param element parent element.
     * @return a pair of element that is serial.
     */
    public static List<CircuitElement> getElementInSerial(CircuitElement element) {
        List<CircuitElement> directChildren = element.getChildElements();
        List<CircuitElement> grandChildren = getGrandChildElements(element);
        List<CircuitElement> elements = new ArrayList<>();

        if (directChildren.size() == 1 &&
        CircuitElementType.RESISTOR.equals(directChildren.get(0).getType()) && CircuitElementType.RESISTOR.equals(element.getType())
                && (grandChildren.size() == 1 || grandChildren.size() == 0)) {
            return Arrays.asList(element, directChildren.get(0));
        }

        for (CircuitElement directChild : directChildren) {
            elements.addAll(getElementInSerial(directChild));
        }

        return elements;
    }

    private static List<CircuitElement> getGrandChildElements(CircuitElement element) {
        List<CircuitElement> grandChildren = new ArrayList<>();

        for (CircuitElement child : element.getChildElements()) {
            grandChildren.addAll(child.getChildElements());
        }

        return grandChildren;
    }

    private static long getNumberOfResistors(List<CircuitElement> elements) {
        return elements.stream().filter(o -> o.getType().equals(CircuitElementType.RESISTOR)).count();
    }
}
