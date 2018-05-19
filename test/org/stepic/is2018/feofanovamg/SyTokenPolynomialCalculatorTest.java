package org.stepic.is2018.feofanovamg;

/**
 * SyTokenPolynomialCalculatorTest extends AbstractPolynomialCalculatorTest. Tests my calculator.
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public class SyTokenPolynomialCalculatorTest extends AbstractPolynomialCalculatorTest {
    // define the fabric with certain calculator.
    protected PolynomialCalculator calc() {
    return new SyTokenPolynomialCalculator();
  }
}
