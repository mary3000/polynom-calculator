package org.stepic.is2018.feofanovamg;

import java.util.StringTokenizer;

/**
 * AbstractTokenPolynomialCalculator implements interface PolynomialCalculator from base. This class realises expression
 * parsing using StringTokenizer. Also it uses interface ExpressionHandler, that calculate
 * expressions.
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public abstract class AbstractTokenPolynomialCalculator implements PolynomialCalculator {

  public abstract ExpressionHandler createExpressionHandler();

  public Polynomial calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException(ParsingException.INVALID);
    }

    ExpressionHandler handler = createExpressionHandler();

    //Expression must be wrapped in braces for correct work of some handler.
    //We parse expression on operators, symbols and braces, and also we should skip
    //special symbols like \t or \n.
    StringTokenizer stringTokenizer =
        new StringTokenizer('(' + expression + ")", handler.getCorrectSymbols(), true);

    while (stringTokenizer.hasMoreTokens()) {
      String token = stringTokenizer.nextToken();

      if (handler.isOperator(token)) {
        handler.pushOperator(token);
      } else if (handler.isMonom(token)) {
        handler.pushMonom(token);
      } else if (!handler.isSymbolToIgnore(token)) {
        throw new ParsingException(ParsingException.INVALID);
      }
    }

    return handler.getAnswer();

  }
}

