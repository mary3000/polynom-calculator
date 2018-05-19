package org.stepic.is2018.feofanovamg;

/**
 * SyTokenPolynomialCalculator extends AbstractTokenPolynomialCalculator.
 * It's a final class, that solves the task.
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public class SyTokenPolynomialCalculator extends AbstractTokenPolynomialCalculator {
  public ExpressionHandler createExpressionHandler() {
    return new SyHandler();
  }
}
