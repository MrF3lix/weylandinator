package ch.weylandinator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.model.CircuitElementType;

public class ResistanceCalculator {
    private CircuitElement rootElement;

    public ResistanceCalculator(CircuitElement rootElement) {
        this.rootElement = rootElement;
    }

    public double getTotalResistance() {
        return calculateTotalResistance();
    }

    private double calculateTotalResistance() {
        CircuitElement root = new CircuitElement(rootElement);
        long numberOfResistors = 0;
        double resistance = 0;

        do {
            resistance += calculateSerialResistance(root);
            resistance += calculateParallelResistance(root);

            List<CircuitElement> totalElements = CircuitElementHelper.getFlatList(root.getChildElements());
            numberOfResistors = getNumberOfResistors(totalElements);
        } while (numberOfResistors > 1);

        return resistance;
    }

    private double calculateSerialResistance(CircuitElement root) {
        // check for serial resistors (2 or more resistors that only have 1 child
        // element)

        double resistance = 0;
        List<CircuitElement> serialPositionElements = getElementInSerial(root);
        if (serialPositionElements.size() > 1) {
            resistance = getSumOfResistors(serialPositionElements);

            CircuitElement replacement = new CircuitElement(
                    serialPositionElements.get(serialPositionElements.size() - 1));
            replacement.setName("RS#1");
            replacement.setResistance(getSumOfResistors(serialPositionElements));

            root.replaceChildElement(serialPositionElements.get(0), replacement);

            for (CircuitElement serialElement : serialPositionElements) {
                root.deleteChildElement(serialElement.getName());
            }
        }

        return resistance;
    }

    private double calculateParallelResistance(CircuitElement root) {
        double resistance = 0;
        List<CircuitElement> parallelElements = getElementInParallel(root);

        if (parallelElements.size() > 1) {

            CircuitElement first = parallelElements.get(0);
            CircuitElement second = parallelElements.get(1);

            resistance = Math.pow(first.getResistance(), -1d) + Math.pow(second.getResistance(), -1d);
            resistance = Math.pow(resistance, -1d);

            CircuitElement replacement = new CircuitElement(parallelElements.get(parallelElements.size() - 1));
            replacement.setName("RP#1");
            replacement.setResistance(getSumOfResistors(parallelElements));

            root.replaceChildElement(parallelElements.get(0), replacement);

            for (CircuitElement serialElement : parallelElements) {
                root.deleteChildElement(serialElement.getName());
            }
        }

        return resistance;
    }

    /**
     * Two elements are serial if they both have exactly one child element.
     * 
     * @param element parent element.
     * @return a pair of element that is serial.
     */
    private List<CircuitElement> getElementInSerial(CircuitElement element) {
        List<CircuitElement> directChildren = element.getChildElements();
        List<CircuitElement> grandChildren = element.getChildElements().stream()
                .filter(o -> o.getType() == CircuitElementType.RESISTOR)
                .flatMap(o -> o.getChildElements().stream().map(oo -> oo)).collect(Collectors.toList());

        if (directChildren.size() == 1 && CircuitElementType.RESISTOR.equals(directChildren.get(0).getType())
                && grandChildren.size() == 1) {
            return Arrays.asList(directChildren.get(0), grandChildren.get(0));
        } else {
            for (CircuitElement directChild : directChildren) {
                return getElementInSerial(directChild);
            }
        }

        return new ArrayList<>();
    }

    /**
     * Two elements are parallel if they have the same parent and the same child
     * element.
     * 
     * @return a pair of parallel elements
     */
    private List<CircuitElement> getElementInParallel(CircuitElement element) {
        List<CircuitElement> directChildren = element.getChildElements();
        if (directChildren.size() == 2 && getNumberOfResistors(directChildren) == 2) {
            CircuitElement first = directChildren.get(0);
            CircuitElement second = directChildren.get(1);

            if (first.getChildElements().size() == 1 && second.getChildElements().size() == 1) {
                CircuitElement firstFirstChild = first.getChildElements().get(0);
                CircuitElement secondFirstChild = second.getChildElements().get(0);

                if (firstFirstChild.getName().equals(secondFirstChild.getName())) {
                    return Arrays.asList(first, second);
                }
            }

            if(first.getChildElements().size() == 0 && second.getChildElements().size() == 0) {
                return Arrays.asList(first, second);
            }
        }

        for (CircuitElement directChild : directChildren) {
            return getElementInParallel(directChild);
        }

        return new ArrayList<>();
    }

    private long getNumberOfResistors(List<CircuitElement> elements) {
        return elements.stream().filter(o -> o.getType().equals(CircuitElementType.RESISTOR)).count();
    }

    private double getSumOfResistors(List<CircuitElement> elements) {
        return elements.stream().mapToDouble(CircuitElement::getResistance).sum();
    }

    private boolean hasOneOrLessChild(CircuitElement element) {
        return element.getChildElements().size() <= 1;
    }

}
