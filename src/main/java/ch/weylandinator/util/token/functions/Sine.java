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
public class Sine extends Operator {
    public static final Sine INSTANCE = new Sine();

    private Sine() {
        super("sin", 1, FUNCTIONAL, ArcSine.INSTANCE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].sine();
    }
}
