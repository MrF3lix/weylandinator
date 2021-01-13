package ch.weylandinator.util.token.functions;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * Wrapper for the Cosine function (cos).
 *
 * @author Subhomoy Haldar
 * @version 2017.04.20
 */
public class ArcCosine extends Operator {
    public static final ArcCosine INSTANCE = new ArcCosine();

    private ArcCosine() {
        super("arccos", 1, FUNCTIONAL, Cosine.INSTANCE);
    }
    
    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].arcCosine();
    }
}
