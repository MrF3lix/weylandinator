package ch.weylandinator.util.token.functions;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * Wrapper for the Tangent function (tan).
 *
 * @author Subhomoy Haldar
 * @version 2017.04.20
 */
public class ArcTangent extends Operator {
    public static final ArcTangent INSTANCE = new ArcTangent();

    private ArcTangent() {
        super("arctan", 1, FUNCTIONAL, ArcTangent.INSTANCE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].arcTangent();
    }
}
