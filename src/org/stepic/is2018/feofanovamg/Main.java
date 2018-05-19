package org.stepic.is2018.feofanovamg;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    PolynomialCalculator calc = new SyTokenPolynomialCalculator();

    try {
      System.out.println(calc.calculate(input).convertToString());
    } catch (ParsingException e) {
      e.printStackTrace();
    }

  }
}
