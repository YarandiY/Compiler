package ir.ac.sbu.syntax;

import ir.ac.sbu.lexical.MySymbol;

public interface Lexical {
    String nextToken();
    MySymbol currentToken();
}