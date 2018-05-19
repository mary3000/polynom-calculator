package org.stepic.is2018.feofanovamg;

import java.util.Stack;

/**
 * SyHandler (means shunting-yard hanlder) implements ExpressionHandler. Based on specific handler, that
 * uses shunting-yard algorithm with 2 stacks: for numbers and for operators.
 * @see <a href="https://en.wikipedia.org/wiki/Shunting-yard_algorithm">Algorithm in wiki</a> for more information.
 * We give for each operator the priority and calculate expressions in stacks when it's possible.
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public class SyHandler implements ExpressionHandler {

  public static final String OPERATOR_LIST = "+-()^";
  public static final String SYMBOL_LIST = "abcdefghijklmnopqrstuvwxyz";
  public static final String PASS_LIST = " \\\t\n";

  private Stack<Polynomial> polynomialStack;
  private Stack<Character> functionStack;
  private boolean isPrevPolynomial;
  private boolean isPrevCloseBrace;
  private String lastString = "";

  public SyHandler() {
    polynomialStack = new Stack<>();
    functionStack = new Stack<>();
    //Remember type of the previous token
    //Both can be true at the end of the expression like ...+a)
    isPrevPolynomial = false;
    isPrevCloseBrace = false;
  }

  public void pushMonom(String str) throws ParsingException {
    if (functionStack.peek() == '^') {
      functionStack.pop();
      power(str);
    } else {
      checkNeighbourNumbers(str);
      isPrevCloseBrace = false;
      lastString = str;

      if (isPrevPolynomial) {
        Polynomial p = polynomialStack.pop().multiple(new Monomial(str));
        polynomialStack.push(p);
      } else {
        polynomialStack.push(new Polynomial(str));
      }

    }
    isPrevPolynomial = true;
  }

  public void pushOperator(String str) throws ParsingException {
    char operator = str.charAt(0);

    //Incorrect unary plus
    if (operator == '+' && !isPrevPolynomial && !isPrevCloseBrace) {
      throw new ParsingException(ParsingException.INVALID);
    }

    //Handle unary minus
    if (operator == '-' && !isPrevPolynomial) {
      switch (functionStack.peek()) {
        case '(':
          polynomialStack.push(new Polynomial(new Monomial(0)));
          break;
        case '*':
          polynomialStack.peek().addMinus();
          return;
        default:
          // other situations are not allowed.
          throw new ParsingException(ParsingException.INVALID);
      }
    }

    if (operator == ')') {
      handleBraces();
    } else if (operator == '^') {
      handlePower();
    } else {
      handleOperator(operator);
    }
  }

  private void handleBraces() throws ParsingException {
    //If it's a close brace, we should calculate all expressions in stack until we meet the open brace.
    while (!functionStack.empty() && functionStack.peek() != '(') {
      operate();
    }
    if (functionStack.empty()) {
      throw new ParsingException(ParsingException.INVALID);
    }
    functionStack.pop(); //Pop open brace
    isPrevCloseBrace = true;
  }

  private void handlePower() throws ParsingException {
    //Check if the last expression was a number.
    if (polynomialStack.peek().monomialList.size() == 1 && polynomialStack.peek().monomialList.get(0).varMap
        .isEmpty()) {
      throw new ParsingException(ParsingException.INVALID);
    }
    functionStack.push('^');
  }

  private void handleOperator(char operator) throws ParsingException {
    //Operate while it's possible.
    while (functionStack.size() != 0 && canPop(functionStack.peek(), operator)) {
      operate();
    }
    //Don't forget to multiply later.
    if (operator == '(' && isPrevPolynomial) {
      functionStack.push('*');
    }
    functionStack.push(operator);
    isPrevPolynomial = false;
    isPrevCloseBrace = false;
  }

  public boolean isMonom(String str) {
    return str.length() == 1 && SYMBOL_LIST.contains(str) || tryToInteger(str) != -1;
  }

  public boolean isOperator(String str) {
    return str.length() == 1 && OPERATOR_LIST.contains(str);
  }

  public boolean isSymbolToIgnore(String str) {
    return PASS_LIST.contains(str);
  }

  public String getCorrectSymbols() {
    return SYMBOL_LIST + OPERATOR_LIST + PASS_LIST;
  }

  public Polynomial getAnswer() throws ParsingException {
    if (polynomialStack.size() != 1 || !functionStack.empty()) {
      throw new ParsingException(ParsingException.INVALID);
    }
    return polynomialStack.pop();
  }

  //Returns priority for operator.
  private int getPriority(char operator) throws ParsingException {
    switch (operator) {
      case '+':
      case '-':
        return 3;
      case '*':
        return 2;
      case '^':
        return 1;
      case '(':
        return 0;
      default:
        throw new ParsingException(ParsingException.INVALID);
    }
  }

  //Says if it possible to pop the previous operator.
  //Its priority should be less, and it cannot be the open brace.
  private boolean canPop(char firstOperator, char secondOperator) throws ParsingException {
    int firstPriority = getPriority(firstOperator);
    int secondPriority = getPriority(secondOperator);
    return firstPriority > 0 && secondPriority > 0 && firstPriority <= secondPriority;
  }

  //Do an action with last 2 polynomials and operator.
  private void operate() throws ParsingException {
    char operator = functionStack.pop();

    if (polynomialStack.size() < 2) {
      throw new ParsingException(ParsingException.INVALID);
    }

    Polynomial secondPolynomial = polynomialStack.pop();
    Polynomial firstPolynomial = polynomialStack.pop();
    switch (operator) {
      case '+':
        firstPolynomial.addTo(secondPolynomial);
        polynomialStack.push(firstPolynomial);
        break;
      case '-':
        secondPolynomial.addMinus();
        firstPolynomial.addTo(secondPolynomial);
        polynomialStack.push(firstPolynomial);
        break;
      case '*':
        firstPolynomial = firstPolynomial.multiple(secondPolynomial);
        polynomialStack.push(firstPolynomial);
        break;
      default:
        throw new ParsingException(ParsingException.INVALID);
    }
  }

  private void power(String str) throws ParsingException {
    int val = tryToInteger(str);

    if (val > 32) {
      throw new ParsingException(ParsingException.TOO_BIG);
    } else if (val < 0) {
      throw new ParsingException(ParsingException.INVALID);
    }

    //The first variant is to power the previous symbol.
    if (!isPrevCloseBrace) {
      Monomial m = new Monomial(lastString);

      if (m.varMap.isEmpty()) {
        throw new ParsingException(ParsingException.INVALID);
      }
      m.varMap.put(m.varMap.firstKey(), val - 1);
      polynomialStack.push(polynomialStack.pop().multiple(m));

    } else {
      //The second variant is to power the whole polynomial.
      polynomialStack.push(quickPower(val, polynomialStack.pop()));
    }
  }

  //Power with the help of binary representation.
  private Polynomial quickPower(int val, Polynomial tmp) throws ParsingException {
    Polynomial polynomial = new Polynomial(new Monomial(1));

    int indicator = val & 1;
    if (indicator == 1) {
      polynomial = tmp;
    }
    val >>= 1;
    indicator = val & 1;

    while (val > 0) {
      tmp = tmp.multiple(tmp);
      if (indicator == 1) {
        polynomial = polynomial.multiple(tmp);
      }
      val >>= 1;
      indicator = val & 1;
    }
    return polynomial;
  }

  //Checks if there were two consecutive numbers.
  private void checkNeighbourNumbers(String str) throws ParsingException {
    if (!isPrevCloseBrace && isPrevPolynomial
        && tryToInteger(lastString) != -1 && tryToInteger(str) != -1) {
      throw new ParsingException(ParsingException.INVALID);
    }
  }

  public static int tryToInteger(String token) {
    try {
      return Integer.parseInt(token);
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}
