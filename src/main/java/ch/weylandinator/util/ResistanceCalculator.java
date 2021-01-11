package ch.weylandinator.util;

import java.util.List;

import ch.weylandinator.model.CircuitElement;

public class ResistanceCalculator {
    private CircuitElement rootElement;

    public ResistanceCalculator(CircuitElement rootElement) {
        this.rootElement = rootElement;
    }

    public double getTotalResistance() {

        if(checkSeriality(rootElement)) {
            return getSumOfResistors(CircuitElementHelper.getFlatList(rootElement.getChildElements()));
        }

        return 0;
    }

    private double getSumOfResistors(List<CircuitElement> elements) {
        return elements.stream().mapToDouble(CircuitElement::getResistance).sum();
    }

    private boolean checkSeriality(CircuitElement element) {
        boolean isSerial = true;

        for (CircuitElement child : element.getChildElements()) {
            if(!hasOneOrLessChild(child)) {
                isSerial = false;
            } else {
                return checkSeriality(child);
            }
        }

        return isSerial;
    }

    private boolean hasOneOrLessChild(CircuitElement element) {
        return element.getChildElements().size() <= 1;
    }

}
