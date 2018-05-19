package org.stepic.is2018.feofanovamg;

import static org.stepic.is2018.feofanovamg.SyHandler.tryToInteger;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Realises monomial via TreeMap (symbol is a key, and power of it is a value).
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public class Monomial implements Comparable<Monomial> {

  public TreeMap<Character, Integer> varMap;
  public int coefficient;

  public Monomial(String str) {
    varMap = new TreeMap<>();
    coefficient = tryToInteger(str);

    if (coefficient == -1) {
      coefficient = 1;
      assert(str.length() == 1);
      char letter = str.charAt(0);
      varMap.put(letter, 1);
    }

  }

  public Monomial(int value) {
    varMap = new TreeMap<>();
    coefficient = value;
  }

  public Monomial(Monomial other) {
    varMap = new TreeMap<>(other.varMap);
    coefficient = other.coefficient;
  }

  public String convertToString() {
    StringBuilder answer = new StringBuilder();
    if (coefficient == -1) {
      answer.append("-");
    } else if (coefficient != 1 || varMap.isEmpty()) {
      answer.append(Integer.toString(coefficient));
    }

    for (Map.Entry e : varMap.entrySet()) {
      answer.append(e.getKey());
      if ((Integer)e.getValue() != 1) {
        answer.append("^").append(e.getValue());
      }
    }

    return answer.toString();
  }

  public int compareTo(Monomial other) {
    Map.Entry element = varMap.firstEntry();
    Map.Entry otherElement = other.varMap.firstEntry();

    //for numbers compare is oppositely
    if (element == null && otherElement == null) {
      return 0;
    }
    if (element == null) {
      return 1;
    } else if (otherElement == null) {
      return -1;
    }

    //Find first elements that differ
    while (element != null && otherElement != null && element.equals(otherElement)) {
      element = varMap.higherEntry((Character)element.getKey());
      otherElement = other.varMap.higherEntry((Character)otherElement.getKey());
    }

    //Difference in the number of symbols
    if (element == null && otherElement == null) {
      return 0;
    }
    if (element == null) {
      return -1;
    }
    if (otherElement == null) {
      return 1;
    }

    //Difference in the symbols
    if ((Character)element.getKey() > (Character)otherElement.getKey()) {
      return 1;
    }
    if ((Character)element.getKey() < (Character)otherElement.getKey()) {
      return -1;
    }

    //Difference in the power
    if ((Integer)element.getValue() > (Integer)otherElement.getValue()) {
      return -1;
    }

    return 1;
  }

  public void collapse(Monomial other) {
    coefficient += other.coefficient;
    if (coefficient == 0) {
      varMap.clear();
    }
  }

  public Monomial multiple(Monomial other) throws ParsingException {
    Monomial ans = new Monomial(this);
    ans.coefficient *= other.coefficient;
    if (ans.coefficient == 0) {
      ans.varMap.clear();
      return ans;
    }

    other.varMap.forEach((key, value) -> ans.varMap.merge(key, value, (a, b) -> a + b));
    
    Collection values = ans.varMap.values();
    for (Object value : values) {
      if ((Integer)value > 32) {
        throw new ParsingException(ParsingException.TOO_BIG);
      }
    }
    
    //ans.coefficient *= other.coefficient;
    return ans;
  }
}
