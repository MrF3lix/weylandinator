package ch.weylandinator.util.token.operations;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.EvaluationException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class RaisingToPower extends Operator {
    public static final RaisingToPower INSTANCE = new RaisingToPower();

    private RaisingToPower() {
        super("^", 2, EXPONENTIAL);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].pow(operands[1]);
    }
}
