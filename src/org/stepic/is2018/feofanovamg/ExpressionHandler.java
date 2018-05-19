package org.stepic.is2018.feofanovamg;

/**
 * ExpressionHandler is an interface, that somehow handles symbols and operators, and give the
 * answer in the end.
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public interface ExpressionHandler {

  void pushMonom(String str) throws ParsingException;
  void pushOperator(String str) throws ParsingException;

  boolean isMonom(String str);
  boolean isOperator(String str);

  boolean isSymbolToIgnore(String str);

  String getCorrectSymbols();

  Polynomial getAnswer() throws ParsingException;
}
