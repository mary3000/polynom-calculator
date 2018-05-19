package org.stepic.is2018.feofanovamg;

/**
 * Failed to parse the expression
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public class ParsingException extends Exception {

  public static final String TOO_BIG = "ERROR: TOO BIG";
  public static final String INVALID = "ERROR: INVALID";

  public ParsingException(String message) {
    super(message);
  }
}
