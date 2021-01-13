package ch.weylandinator.util.token.functions;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * Wrapper for the Sine function (sin).
 *
 * @author Subhomoy Haldar
 * @version 2017.04.20
 */
public class ArcSine extends Operator {
    public static final ArcSine INSTANCE = new ArcSine();

    private ArcSine() {
        super("arcsin", 1, FUNCTIONAL, Sine.INSTANCE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].arcSine();
    }
}
