package ch.weylandinator.util;
import ch.weylandinator.util.token.Operator;
import ch.weylandinator.util.token.Token;
import ch.weylandinator.util.token.operands.Real;
import ch.weylandinator.util.token.operands.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
class ExpressionTest
{
    Expression expression;

    @BeforeEach
    void setUp(){
        
    }
    
    //region Dissolve-Expression
    
    @Test
    void dissolveAdd(){

        ExpressionDissolver expressionDissolver = new ExpressionDissolver();
        String expressionString = "4 + = 2 2 +";
        List<Token> tokensLeft  = new ArrayList<>();
        for(Token token : ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString.split("=")[0]))){
            Token t = null;
            if(token instanceof Operator){
                t = OperatorMap.INSTANCE.getFor(token.toString());
            }
            if(token instanceof Real){
                t = new Real(((Real) token).getValue());
            }
            if(token instanceof Variable){
                t = new Variable(((Variable) token).toString());
            }
            tokensLeft.add(t);
        }

        List<Token> tokensright  = new ArrayList<>();
        for(Token token : ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString.split("=")[1]))){
            Token t = null;
            if(token instanceof Operator){
                t = OperatorMap.INSTANCE.getFor(token.toString());
            }
            if(token instanceof Real){
                t = new Real(((Real) token).getValue());
            }
            if(token instanceof Variable){
                t = new Variable(((Variable) token).toString());
            }
            tokensright.add(t);
        }
        
        assertEquals("A = 1 2 +",expressionDissolver.dissolveExpression(tokensLeft, tokensright));
    }

    @Test
    void dissolveAdd2(){
        ExpressionDissolver expressionDissolver = new ExpressionDissolver();
        
        String expressionString = "1 = A + 2";
        List<Token> tokensLeft  = ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString.split("=")[0]));
        List<Token> tokensright  = ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString.split("=")[1]));

        assertEquals("A = 1 2 +",expressionDissolver.dissolveExpression(tokensLeft, tokensright));
    }
    
    @Test
    void Test(){

        ExpressionDissolver expressionDissolver = new ExpressionDissolver();

        List<Token>    tokensright = new ArrayList<>();
        tokensright.add(new Variable("x"));
        tokensright.add(new Real("4"));

        tokensright.add(OperatorMap.INSTANCE.getFor("+"));
        
        List<Token>    tokensLeft = new ArrayList<>();
        tokensLeft.add(new Real("4"));
        tokensLeft.add(new Real("5"));
        tokensLeft.add(OperatorMap.INSTANCE.getFor("+"));


        assertEquals("A = 1 2 +",expressionDissolver.dissolveExpression(tokensLeft, tokensright));
    }
    
    //endregion
    
    
    //region Basic-Calculation
    
    //region Operations
    @Test
    void add(){
        expression = new Expression("4 + 2");
        assertEquals(6.0,expression.evaluate().getValue());
    }

    @Test
    void subtract(){
        expression = new Expression("4 - 2");
        assertEquals(2.0,expression.evaluate().getValue());
    }
    
    @Test
    void divide(){
        expression = new Expression("6 / 2");
        assertEquals(3.0,expression.evaluate().getValue());
    }

    @Test
    void multiply(){
        expression = new Expression("2 * 2");
        assertEquals(4.0,expression.evaluate().getValue());
    }

    @Test
    void pow(){
        expression = new Expression("2^3");
        assertEquals(8.0,expression.evaluate().getValue());
    }
    //endregion

    //region Functions
    @Test
    void abs(){
        expression = new Expression("sqrt(64)");
        assertEquals(8.0,expression.evaluate().getValue());
    }

    @Test
    void arcSine(){
        expression = new Expression("arcsin(1)");
        assertEquals(Math.asin(1),expression.evaluate().getValue());
    }

    @Test
    void arcCosine(){
        expression = new Expression("arccos(1)");
        assertEquals(Math.acos(1),expression.evaluate().getValue());
    }
    
    @Test
    @Disabled
    void arcTangent(){
        expression = new Expression("arctan(0.5)");
        assertEquals(Math.atan(0.5),expression.evaluate().getValue());
    }

    @Test
    void cosine(){
        expression = new Expression("cos(0)");
        assertEquals(1.0,expression.evaluate().getValue());
    }

    @Test
    void exp(){
        expression = new Expression("exp(2)");
        assertEquals(Math.exp(2),expression.evaluate().getValue());
    }

    @Test
    void log(){
        expression = new Expression("ln(16)");
        assertEquals(Math.log(16),expression.evaluate().getValue());
    }

    @Test
    void sine(){
        expression = new Expression("sin(0)");
        assertEquals(0.0,expression.evaluate().getValue());
    }

    @Test
    void srqt(){
        expression = new Expression("sqrt(64)");
        assertEquals(8.0,expression.evaluate().getValue());
    }

    @Test
    void tangent(){
        expression = new Expression("tan(0)");
        assertEquals(0.0,expression.evaluate().getValue());
    }
    //endregion
    
    //endregion
    
}