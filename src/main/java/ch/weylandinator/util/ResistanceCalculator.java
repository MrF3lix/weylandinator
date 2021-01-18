package ch.weylandinator.util;

import java.util.List;
import ch.weylandinator.model.CircuitElement;
import ch.weylandinator.model.CircuitElementType;

public class ResistanceCalculator {
    private CircuitElement rootElement;
    private int index;

    public ResistanceCalculator(CircuitElement rootElement) {
        this.rootElement = rootElement;
    }

    public double getTotalResistance() {
        return calculateTotalResistance();
    }

    private double calculateTotalResistance() {
        CircuitElement root = new CircuitElement(rootElement);
        long numberOfResistors = 0;
        index = 0;

        do {
            calculateSerialResistance(root);
            calculateParallelResistance(root);

            List<CircuitElement> totalElements = CircuitElementHelper.getFlatList(root.getChildElements());
            numberOfResistors = getNumberOfResistors(totalElements);

            index++;
        } while (numberOfResistors > 1 && index < 9999);

        if(index == 9999) {
            throw new RuntimeException("Something went wrong, the total resistance couldn't be calculated");
        }

        return getSumOfResistors(CircuitElementHelper.getFlatList(root.getChildElements()));
    }

    private double calculateSerialResistance(CircuitElement root) {
        double resistance = 0;
        List<CircuitElement> serialPositionElements = CircuitElementHelper.getElementInSerial(root);
        if (serialPositionElements.size() > 1) {
            resistance = getSumOfResistors(serialPositionElements);

            CircuitElement replacement = new CircuitElement(serialPositionElements.get(serialPositionElements.size() - 1));
            replacement.setName("RS#"+index);
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
        List<CircuitElement> parallelElements = CircuitElementHelper.getElementInParallel(root);

        if (parallelElements.size() > 1) {

            CircuitElement first = parallelElements.get(0);
            CircuitElement second = parallelElements.get(1);

            resistance = Math.pow(first.getResistance(), -1d) + Math.pow(second.getResistance(), -1d);
            resistance = Math.pow(resistance, -1d);

            CircuitElement replacement = new CircuitElement(first);
            replacement.setName("RP#"+index);
            replacement.setResistance(resistance);

            root.replaceChildElement(first, replacement);
            
            root.deleteChildElement(first.getName());
            root.deleteChildElement(second.getName());
        }

        return resistance;
    }

    private long getNumberOfResistors(List<CircuitElement> elements) {
        return elements.stream().filter(o -> o.getType().equals(CircuitElementType.RESISTOR)).count();
    }

    private double getSumOfResistors(List<CircuitElement> elements) {
        return elements.stream().mapToDouble(CircuitElement::getResistance).sum();
    }
}
