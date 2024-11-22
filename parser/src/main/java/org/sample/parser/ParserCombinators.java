package org.sample.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;


public class ParserCombinators {

  private final String input;
  private final Deque<Object> stack = new ArrayDeque<>();
  private int pos = 0;

  public ParserCombinators(String input) {
    this.input = input;
  }


  private void skipWhitespace() {
    while (pos < input.length() && (input.charAt(pos) == ' ' || input.charAt(pos) == '\n')) {
      ++pos;
    }
  }

  private boolean parseStringLit() {
    if (pos >= input.length()) {
      return false;
    }
    if (input.charAt(pos) != '"') {
      return false;
    }
    int last = input.substring(pos + 1).indexOf('"');
    if (last < 0) {
      return false;
    }
    stack.push(input.substring(pos + 1, pos + last + 1));
    pos += last + 2;
    skipWhitespace();
    return true;
  }

  private boolean parseNumber() {
    if (pos >= input.length()) {
      return false;
    }
    Scanner scanner = new Scanner(input.substring(pos));
    String num = scanner.useDelimiter("[^0-9]").next();
    if (num.length() > 0) {
      pos += num.length();
      stack.push(Integer.parseInt(num));
      return true;
    }
    return false;
  }

  private boolean parseChar(char c) {
    if (pos >= input.length()) {
      return false;
    }
    boolean success = input.charAt(pos) == c;
    if (!success) {
      return false;
    }
    ++pos;
    skipWhitespace();
    return true;
  }

  // === PARSER ==================================================================================

    /*
    VALUE   ::= STRINGLIT / NUMBER / OBJECT / ARRAY
    OBJECT  ::= "{" (PAIR ("," PAIR)* )? "}"
    PAIR    ::= STRINGLIT ":" VALUE
    ARRAY   ::= "[" (VALUE ("," VALUE)* )? "]"
     */

  // VALUE ::= STRINGLIT / NUMBER / OBJECT / ARRAY
  public boolean parseValue() {
    return parseStringLit() || parseNumber() || parseObject() || parseArray();
  }

  // OBJECT ::= "{" (PAIR ("," PAIR)* )? "}"
  private boolean parseObject() {
    int pos0 = pos;
    int stack0 = stack.size();
    boolean success = parseChar('{') && parsePairs() && parseChar('}');
    if (!success) {
      pos = pos0;
      return false;
    }
    HashMap<String, Object> object = new HashMap<>();
    while (stack.size() > stack0) {
      Object value = stack.pop();
      String string = (String) stack.pop();
      object.put(string, value);
    }
    stack.push(object);
    return true;
  }

  // (PAIR ("," PAIR)* )?
  private boolean parsePairs() {
    if (parsePair()) {
      parsePairTails();
    }
    return true;
  }

  // PAIR  ::= STRINGLIT ":" VALUE
  private boolean parsePair() {
    int pos0 = pos;
    boolean success = parseStringLit() && parseChar(':') && parseValue();
    if (!success) {
      pos = pos0;
    }
    return success;
  }

  // ("," PAIR)*
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

  // ARRAY ::= "[" (VALUE ("," VALUE)* )? "]"
  private boolean parseArray() {
    int pos0 = pos;
    int stack0 = stack.size();
    boolean success = parseChar('[') && parseValues() && parseChar(']');
    if (!success) {
      pos = pos0;
      return false;
    }
    ArrayList<Object> array = new ArrayList<>();
    while (stack.size() > stack0) {
      array.add(stack.pop());
    }
    Collections.reverse(array);
    stack.push(array);
    return true;
  }

  // (VALUE ("," VALUE)* )?
  private boolean parseValues() {
    if (parseValue()) {
      parseValueTails();
    }
    return true;
  }

  // ("," VALUE)*
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

  //========================================================================================
  //Parsers

  private interface Parser {

    boolean parse();
  }

  private final class StringParser implements Parser {

    @Override
    public boolean parse() {
      return parseStringLit();
    }
  }

  private final class NumberParser implements Parser {

    @Override
    public boolean parse() {
      return parseNumber();
    }
  }

  private final class CharParser implements Parser {

    private final char c;

    CharParser(char c) {
      this.c = c;
    }

    @Override
    public boolean parse() {
      return parseChar(c);
    }
  }

  private class SequenceParser implements Parser {

    private final Parser[] parsers;

    SequenceParser(Parser... parsers) {
      this.parsers = parsers;
    }

    @Override
    public boolean parse() {
      for (Parser child : parsers) {
        int initPos = pos;
        if (!child.parse()) {
          pos = initPos;
          return false;
        }
      }
      return false;
    }
  }

  private class ForwardReference implements Parser {

    private final Supplier<Parser> supplier;

    public ForwardReference(Supplier<Parser> supplier) {
      this.supplier = supplier;
    }

    @Override
    public boolean parse() {
      return supplier.get().parse();
    }
  }

  private class RepetitionParser implements Parser {

    private final Parser child;

    private RepetitionParser(Parser child) {
      this.child = child;
    }

    @Override
    public boolean parse() {
      while (child.parse())
        ;
      return true;
    }
  }

  private class OptionalParser implements Parser {

    private final Parser child;

    OptionalParser(Parser child) {
      this.child = child;
    }

    @Override
    public boolean parse() {
      child.parse();
      return false;
    }
  }

  private class ChoiceParser implements Parser {

    private final Parser[] parsers;

    ChoiceParser(Parser... parsers) {
      this.parsers = parsers;
    }

    @Override
    public boolean parse() {
      for (Parser var : parsers) {
        if (var.parse()) {
          return true;
        }
      }
      return false;
    }
  }


  private class ComposeObjectParser implements Parser {

    private final Parser child;

    ComposeObjectParser(Parser child) {
      this.child = child;
    }

    @Override
    public boolean parse() {
      int stack0 = stack.size();
      boolean success = child.parse();
      if (!success) {
        return false;
      }
      Map<String, Object> map = new HashMap<>();
      while (stack.size() > stack0) {
        Object value = stack.pop();
        String key = (String) stack.pop();
        map.put(key, value);
      }
      stack.push(map);
      return false;
    }
  }

  private class ComposeArrayParser implements Parser {

    private final Parser child;

    ComposeArrayParser(Parser child) {
      this.child = child;
    }

    @Override
    public boolean parse() {
      int stack0 = stack.size();
      boolean success = child.parse();
      if (!success) {
        return false;
      }
      List<Object> list = new ArrayList<>();
      while (stack.size() > stack0) {
        list.add(stack.pop());
      }
      Collections.reverse(list);
      stack.push(list);
      return false;
    }
  }


  private final Parser pair = new SequenceParser(
      new StringParser(),
      new CharParser(':'),
      new ForwardReference(() -> this.value)
  );

  private final Parser pairTails = new RepetitionParser(new SequenceParser(
      new StringParser(),
      new CharParser(':'),
      new ForwardReference(() -> this.value)
  ));

  private final Parser pairs = new OptionalParser(
      new SequenceParser(pair, pairTails));

  private final Parser object = new ComposeObjectParser(new SequenceParser(
      new CharParser('{'),
      pairs,
      new CharParser('{')

  ));

  private final Parser valueTails = new RepetitionParser(
      new SequenceParser(new CharParser(','), new ForwardReference(() -> this.value))
  );

  private final Parser values = new OptionalParser(
      new SequenceParser(new ForwardReference(() -> this.value), valueTails));


  private final Parser array = new ComposeArrayParser(new SequenceParser(
      new CharParser('[')
      , values
      , new CharParser(']')
  ));

  private final Parser value = new ChoiceParser(
      new StringParser(),
      new NumberParser(),
      object,
      array
  );
}
