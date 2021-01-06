package ch.weylandinator.state;

import ch.weylandinator.model.Circuit;
import ch.weylandinator.model.Element;

public class StateManager {

    private static StateManager instance = null;
    
    public static StateManager getInstance() 
    { 
        if (instance == null) 
        instance = new StateManager(); 
  
        return instance; 
    } 

    private Circuit circuit;

    public StateManager() {
        circuit = new Circuit();
    }

    public void removeElementFromCircuit(String elementName) {
        circuit.removeElementFromCircuit(elementName);
    }

    public void addElementToCircuit(Element element) {
        circuit.addElementToCircuit(element);
    }

    public String getState() {
        return circuit.toString();
    }
    
    public Circuit getCircuit(){return circuit;};
}
