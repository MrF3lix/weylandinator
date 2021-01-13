package ch.weylandinator.util.token.operations;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.EvaluationException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;
import ch.weylandinator.util.token.VariableMap;
import ch.weylandinator.util.token.operands.Variable;

/**
 * @author Subhomoy Haldar
 * @version 2017.04.22
 */
public class Assignment extends Operator {
    public static final Assignment INSTANCE = new Assignment();

    private Assignment() {
        super("=", 2, ASSIGNMENT, null);
    }

    @Override
    public Operand evaluate(Operand... operands) throws
            ArityException,
            EvaluationException {
        check(operands.length);
        // Make sure that the operand being assigned to
        // is a variable.
        if (!(operands[0] instanceof Variable)) {
            throw new EvaluationException("Assignment only works for variables.");
        }
        VariableMap.INSTANCE.bind((Variable) operands[0], operands[1]);
        return operands[1];
    }
}
