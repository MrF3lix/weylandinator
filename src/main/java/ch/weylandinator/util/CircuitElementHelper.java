package ch.weylandinator.util;

import java.util.ArrayList;
import java.util.List;

import ch.weylandinator.model.CircuitElement;

public class CircuitElementHelper {
    public static List<CircuitElement> getFlatList(List<CircuitElement> elements) {
        List<CircuitElement> collected = new ArrayList<>();
        collected.addAll(elements);

        for(CircuitElement child : elements) {
            collected.addAll(getFlatList(child.getChildElements()));
        }

        return collected;
    }
}
