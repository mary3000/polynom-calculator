package org.stepic.is2018.feofanovamg;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Realises polynomial via list of monomials
 * @see Monomial
 *
 * @author Mary Feofanova
 * @since 21.04.18
 */
public class Polynomial {

  public ArrayList<Monomial> monomialList;

  public Polynomial() {
    monomialList = new ArrayList<>();
  }

  public Polynomial(String str) {
    monomialList = new ArrayList<>();
    monomialList.add(new Monomial(str));
  }

  public Polynomial(Polynomial other) {
    monomialList = new ArrayList<>(other.monomialList);
  }

  public Polynomial(Monomial m) {
    monomialList = new ArrayList<>();
    monomialList.add(m);
  }

  public String convertToString() {
    collapse();
    StringBuilder answer = new StringBuilder();

    for (int i = 0; i < monomialList.size(); i++) {
      answer.append(monomialList.get(i).convertToString());
      if (i != monomialList.size() - 1 && monomialList.get(i + 1).coefficient > 0) {
        answer.append("+");
      }
    }

    return answer.toString();
  }

  public void addTo(Polynomial other) {
    monomialList.addAll(other.monomialList);
    collapse();
  }

  public void addMinus() {
    for (Monomial m : monomialList) {
      m.coefficient = -m.coefficient;
    }
  }

  public Polynomial multiple(Polynomial other) throws ParsingException {
    Polynomial answer = new Polynomial();

    for (Monomial m : other.monomialList) {
      answer.addTo(multiple(m));
    }

    return answer;
  }

  public Polynomial multiple(Monomial other) throws ParsingException {
    Polynomial answer = new Polynomial(this);

    for (int i = 0; i < answer.monomialList.size(); i++) {
      answer.monomialList.set(i, answer.monomialList.get(i).multiple(other));
    }

    return answer;
  }

  public void monomialSort() {
    Collections.sort(monomialList);
  }

  //Collapse "equal" and zero monomials
  public void collapse() {
    monomialSort();
    Monomial prev = monomialList.get(0);
    while (monomialList.get(0).varMap.values().remove(0));

    for (int i = 1; i < monomialList.size(); i++) {
      while (monomialList.get(i).varMap.values().remove(0));

      if (prev.compareTo(monomialList.get(i)) == 0) {
        monomialList.get(i).collapse(prev);
        monomialList.remove(i - 1);
        if (i == monomialList.size()) {
          break;
        }
      }
      if (monomialList.get(i).varMap.isEmpty() && monomialList.get(i).coefficient == 0) {
        monomialList.remove(i);
      }
      if (i == monomialList.size()) {
        break;
      }
      prev = monomialList.get(i);
    }

  }
}
