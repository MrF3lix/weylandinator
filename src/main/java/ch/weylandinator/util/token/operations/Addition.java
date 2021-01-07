package ch.weylandinator.util.token.operations;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.EvaluationException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Addition extends Operator {
    public static final Addition INSTANCE = new Addition();

    private Addition() {
        super("+", 2, ADDITIVE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].add(operands[1]);
    }
}
