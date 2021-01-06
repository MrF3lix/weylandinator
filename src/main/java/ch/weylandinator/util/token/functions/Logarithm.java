package ch.weylandinator.util.token.functions;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.EvaluationException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * @author Subhomoy Haldar
 * @version 2017.05.12
 */
public class Logarithm extends Operator {
    public static final Logarithm INSTANCE = new Logarithm();

    private Logarithm() {
        super("log", 1, FUNCTIONAL);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].log();
    }
}
