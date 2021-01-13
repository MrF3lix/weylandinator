package ch.weylandinator.util.token.functions;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;
import ch.weylandinator.util.token.operations.RaisingToPower;

/**
 * Wrapper for the Exponential function (exp).
 *
 * @author Subhomoy Haldar
 * @version 2017.04.20
 */
public class Exponential extends Operator {
    public static final Exponential INSTANCE = new Exponential();

    private Exponential() {
        super("exp", 1, FUNCTIONAL, RaisingToPower.INSTANCE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].exp();
    }
}
