package org.stepic.is2018.feofanovamg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Abstract tester
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public abstract class AbstractPolynomialCalculatorTest {

  protected abstract PolynomialCalculator calc();

  protected void test(String expression, String expected) throws ParsingException {
    String errorMessage = String
        .format("Bad result for expression '%s', '%s' expected", expression, expected);
    String actual = calc().calculate(expression).convertToString();
    assertEquals(expected, actual, errorMessage);
  }

  protected void testException(String expression, String message) throws ParsingException {
    Throwable exception = assertThrows(ParsingException.class, () -> calc().calculate(expression));
    assertEquals(message, exception.getMessage());
  }

  @Test
  public void testIntOps() throws ParsingException {
    test("3 + 4", "7");
    test("8 - 13", "-5");
    test("1 - 1", "0");
    test("(2)(2)", "4");
  }

  @Test
  public void testCorrectPolynoms() throws ParsingException {
    test("a", "a");
    test("a + b", "a+b");
    test("a^2 + 2ab + b^2", "a^2+2ab+b^2");
    test("a^4 + a^3 + a^2 + a + 1", "a^4+a^3+a^2+a+1");
    test("a^2 - 5a", "a^2-5a");
    test("a^2bcd^20eh", "a^2bcd^20eh");
  }

  @Test
  public void testSorting() throws ParsingException {
    test("c+b+a", "a+b+c");
    test("a-h+b", "a+b-h");
    test("-3+a-a^2-a^3+a^6", "a^6-a^3-a^2+a-3");
    test("2aabcb-10b2", "2a^2b^2c-20b");
    test("a^3+a^2+a^2b^2+a^2b+ab^2+b^2+b", "a^3+a^2+a^2b^2+a^2b+ab^2+b^2+b");
  }

  @Test
  public void testSimplePolynoms() throws ParsingException {
    test("(a+b)^2", "a^2+2ab+b^2");
    test("(a-1)^2", "a^2-2a+1");
    test("(a-b)^3", "a^3-3a^2b+3ab^2-b^3");
    test("(a+b-c)^2", "a^2+2ab-2ac+b^2-2bc+c^2");
    test("a-a", "0");
    test("0a+b^0", "1");
    test("(a+b)^0", "1");
    test("(a+b)(e+c)", "ac+ae+bc+be");
    test("(a+b)^2(c+d)", "a^2c+a^2d+2abc+2abd+b^2c+b^2d");
  }

  @Test
  public void testMultiple() throws ParsingException {
    test("2ab", "2ab");
    test("ab2", "2ab");
    test("2 a b", "2ab");
    test("(2a)b", "2ab");
    test("a2 b", "2ab");
  }

  @Test
  public void testCorrectFromTask() throws ParsingException {
    test("a", "a");
    test("-ab", "-ab");
    test("-2a3b", "-6ab");
    test("a^2+b^2-c^2", "a^2+b^2-c^2");
    test("a(b+c)(  d+ e    ^2+(f^9))^2", "abd^2+2abde^2+2abdf^9+abe^4+2abe^2f^9+abf^18+acd^2+2acde^2+2acdf^9+ace^4+2ace^2f^9+acf^18");
  }

  @Test
  public void testIncorrectPolynoms() throws ParsingException {
    testException("a^33", ParsingException.TOO_BIG);
    testException("a^30a^3", ParsingException.TOO_BIG);
    testException("5.0", ParsingException.INVALID);
    testException("6/2", ParsingException.INVALID);
    testException("a+G", ParsingException.INVALID);
    testException("2 2", ParsingException.INVALID);
    testException("(a+b)^c", ParsingException.INVALID);
    testException("(a+b)^(2+2)", ParsingException.INVALID);
    testException("+a", ParsingException.INVALID);
    testException("a)", ParsingException.INVALID);
    testException("(((a))", ParsingException.INVALID);
    testException("", ParsingException.INVALID);
    testException("(a+b+c", ParsingException.INVALID);
    testException("abcD", ParsingException.INVALID);
    testException("(a+b+)", ParsingException.INVALID);
    testException("2^3", ParsingException.INVALID);
    testException("(2+3-10)^2", ParsingException.INVALID);
  }

  @Test
  public void testFromWolfram() throws ParsingException {
    test("(c^2 - 33f)(a-c+c-k)^2", "a^2c^2-33a^2f-2ac^2k+66afk+c^2k^2-33fk^2");
    test("(x+b^2-ab^7c+q)(2uuu-x20u)^3(x-g)", "8ab^7cgu^9-240ab^7cgu^7x+2400ab^7cgu^5x^2-8000ab^7cgu^3x^3-8ab^7cu^9x+240ab^7cu^7x^2-2400ab^7cu^5x^3+8000ab^7cu^3x^4-8b^2gu^9+240b^2gu^7x-2400b^2gu^5x^2+8000b^2gu^3x^3+8b^2u^9x-240b^2u^7x^2+2400b^2u^5x^3-8000b^2u^3x^4-8gqu^9+240gqu^7x-2400gqu^5x^2+8000gqu^3x^3-8gu^9x+240gu^7x^2-2400gu^5x^3+8000gu^3x^4+8qu^9x-240qu^7x^2+2400qu^5x^3-8000qu^3x^4+8u^9x^2-240u^7x^3+2400u^5x^4-8000u^3x^5");
    test("((x-1)^2(x-2)^3(x^2-1))(x^3-x^2-4x+4)", "x^10-9x^9+28x^8-18x^7-95x^6+243x^5-190x^4-72x^3+224x^2-144x+32");
  }

}