package org.sample.parser;


public class Application {

    static String JSON_INPUT = "{\"menu\": {\n" +
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
        System.out.println("Parser : ");
        if (parser.parseValue()) {
            System.out.println("Value correct");
        } else {
            System.out.println("Value wrong");
        }
    }

    private static void runParserAst() {
        ParserAST parserAST = new ParserAST(JSON_INPUT);
        System.out.println("ParserAST : ");
        if (parserAST.parseValue()) {
            System.out.println("Value correct");
        } else {
            System.out.println("Value wrong");
        }
    }

    private static void runParserCombinators() {
        ParserCombinators parserCombinators = new ParserCombinators(JSON_INPUT);
        System.out.println("ParserCombinators : ");
        if (parserCombinators.parseValue()) {
            System.out.println("Value correct");
        } else {
            System.out.println("Value wrong");
        }
    }
}
