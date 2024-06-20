package org.sample.parser;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

  static final String JSON_INPUT = "{\"menu\": {\n" +
      "  \"id\": \"file\",\n" +
      "  \"value\": \"File\",\n" +
      "  \"popup\": {\n" +
      "    \"menuitem\": [\n" +
      "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
      "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
      "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
      "    ]\n" +
      "  }\n" +
      "}}";


  public static void main(String[] args) {
    runParser();
    runParserAst();
    runParserCombinators();
  }

  private static void runParser() {
    Parser parser = new Parser(JSON_INPUT);
    log.info("Parser : ");
    if (parser.parseValue()) {
      log.info("Value correct");
    } else {
      log.info("Value wrong");
    }
  }

  private static void runParserAst() {
    ParserAST parserAST = new ParserAST(JSON_INPUT);
    log.info("ParserAST : ");
    if (parserAST.parseValue()) {
      log.info("Value correct");
    } else {
      log.info("Value wrong");
    }
  }

  private static void runParserCombinators() {
    ParserCombinators parserCombinators = new ParserCombinators(JSON_INPUT);
    log.info("ParserCombinators : ");
    if (parserCombinators.parseValue()) {
      log.info("Value correct");
    } else {
      log.info("Value wrong");
    }
  }
}
