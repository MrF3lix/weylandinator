package ch.weylandinator.util;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.ImproperParenthesesException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Token;

import java.util.Collections;
import java.util.List;

/**
 * @author Subhomoy Haldar
 * @version 2017.04.21
 */
public class Expression
{
    private final List<Token> tokens;

    public Expression(final String expressionString) throws ExpressionConverter.ConversionException, ImproperParenthesesException, ExpressionTokenizer.UnrecognizedCharacterException, ExpressionTokenizer.UnrecognizedOperatorException
    {
        tokens =
            Collections.unmodifiableList(ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString)));
    }

    public Operand evaluate() throws ArityException, ExpressionConverter.ConversionException
    {
        return ExpressionEvaluator.evaluate(tokens);
    }
}
