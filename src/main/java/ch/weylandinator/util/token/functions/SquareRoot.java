package ch.weylandinator.util.token.functions;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.EvaluationException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;
/**
 * Wrapper for the square root function (sqrt).
 *
 * @author Subhomoy Haldar
 * @version 2017.04.22
 */
public class SquareRoot extends Operator
{
    public static final SquareRoot INSTANCE = new SquareRoot();

    private SquareRoot() {
        super("sqrt", 1, FUNCTIONAL);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException, EvaluationException
    {
        check(operands.length);
        return operands[0].sqrt();
    }
}
