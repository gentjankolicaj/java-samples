package org.sample.parser;

import java.util.Scanner;


public class Parser {

  private final String input;
  private int pos;

  public Parser(String input) {
    this.input = input;
  }

  private void skipWhitespace() {
      while (pos < input.length() && (input.charAt(pos) == ' ' || input.charAt(pos) == '\n')) {
          ++pos;
      }
  }

  private boolean parseString() {
      if (input.charAt(pos) != '"') {
          return false;
      }
    int last = input.substring(pos + 1).indexOf('"');
      if (last < 0) {
          return false;
      }

    pos = pos + last + 2;
    skipWhitespace();
    return true;
  }

  private boolean parseNumber() {
    Scanner scanner = new Scanner(input.substring(pos));
    String num = scanner.useDelimiter("[^0-9]").next();
    pos += num.length();
    return num.length() > 0;
  }

  private boolean parseChar(char c) {
    boolean success = input.charAt(pos) == c;
      if (!success) {
          return false;
      }
    ++pos;
    skipWhitespace();
    return true;
  }

  boolean parseValue() {
    return parseString() || parseNumber() || parseObject() || parseArray();
  }

  private boolean parseObject() {
    int pos0 = pos;
    boolean success = parseChar('{') && parsePairs() && parseChar('}');
      if (!success) {
          pos = pos0;
      }
    return success;
  }

  private boolean parseArray() {
    int pos0 = pos;
    boolean success = parseChar('[') && parseValues() && parseChar(']');
      if (!success) {
          pos = pos0;
      }
    return success;
  }

  private boolean parseValues() {
      if (parseValue()) {
          parseValueTails();
      }
    return true;
  }

  private boolean parseValueTails() {
    while (true) {
      int pos0 = pos;
      boolean success = parseChar(',') && parseValue();
      if (!success) {
        pos = pos0;
        return true;
      }
    }
  }

  private boolean parsePairs() {
      if (parsePair()) {
          parsePairTails();
      }
    return true;
  }

  private boolean parsePairTails() {
    while (true) {
      int pos0 = pos;
      boolean success = parseChar(',') && parsePair();
      if (!success) {
        pos = pos0;
        return true;
      }
    }
  }

  private boolean parsePair() {
    int pos0 = pos;
    boolean success = parseString() && parseChar(':') && parseValue();
      if (!success) {
          pos = pos0;
      }
    return success;
  }
}
