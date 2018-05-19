package org.stepic.is2018.feofanovamg;

/**
 * Interface of calculator, that turns polynomial to the canonical form.
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public interface PolynomialCalculator {
  Polynomial calculate(String expression) throws ParsingException;
}
