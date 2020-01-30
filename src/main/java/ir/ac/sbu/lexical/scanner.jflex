import java.io.IOException;
 /*user codes */
%%
/*options and decleration */
%class Scanner
%line
%column
%unicode
%function next_token
%type MySymbol

%{
private boolean flag = false;
private HashSet<String> records = new HashSet<String>();
private MySymbol symbol(String token)
{
    System.err.println("Obtain token " + token + " \"" + yytext() + "\"" );
    return new MySymbol(token, yytext());
}
private MySymbol symbol(String token, Object val) {
    System.err.println( "Obtain token " + token + " \"" + yytext() + "\"" );
    return new MySymbol(token, val);
}
 StringBuilder string = new StringBuilder();
%}

/* VARIABLES */
id = {letter}({letter}|{digit}|"_")*
letter = [A-Za-z]

/* INTEGER NUMBERS */
IntLiteral  = ("-")?{Dec_numbers}
Dec_numbers = (0 | {digitWithoutZero}{digit}*)("l" | "L")?
HexLiteral  = ("-")?{Hex_numbers}
Hex_numbers = 0x[\da-fA-F]{1, 4}

/* REAL NUMBERS */
RealLiteral  = ("-")?{Float}
Float    =  ({digit}*({digit} \. | \. {digit}){digit}*)("f" | "F")?
ScientificLiteral = ("-")?{Scientific_notation}
Scientific_notation  = ({Float} | {digit})+ ("e" | "E") ("+" | "-")? {digit}+

digit = [0-9]
digitWithoutZero = [1-9]

/* WHITESPACE */
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

/* String character can be any character except those listed */
StringCharacter = [^\t\r\n\"\'\\]

SpecialCharacter = \\ ([trn\"\'\\])

/* single comment characters can be any character except newline and EOF */
SingleCommentCharacter = [^\n]

/* multiple comment characters can be any thing except "#/" */
MultilpleCommentCharacter = [^\t\r\n#]

/* define states */
%state CHARACTER
%state STRING
%state SINGLE_COMMENT
%state MULT_COMMENT

%%

/*lexical rules*/

<YYINITIAL> {

    /* SYMBOLS */
    "=="    { return symbol("==");}
    "!="    { return symbol("!=");}
    "<="    { return symbol("<=");}
    "<"     { return symbol("<");}
    ">"     { return symbol(">");}
    ">="    { return symbol(">=");}
    "="     { return symbol("=");}
    "~"     { return symbol("~");}
    "&"     { return symbol("&");}
    "|"     { return symbol("|");}
    "^"     { return symbol("^");}
    "and"   { return symbol("and");}
    "or"    { return symbol("or");}
    "not"   { return symbol("not");}
    "*"     { return symbol("*");}
    "+"     { return symbol("+");}
    "+="    { return symbol("+=");}
    "%="    { return symbol("%=");}
    "-="    { return symbol("-=");}
    "*="    { return symbol("*=");}
    "/="    { return symbol("/=");}
    "/"     { return symbol("/");}
    "%"     { return symbol("%");}
    "begin" { return symbol("begin");}
    "end"   { return symbol("end");}
    "("     { return symbol("(");}
    ")"     { return symbol(")");}
    "."     { return symbol(".");}
    ","     { return symbol(",");}
    ":"     { return symbol(":");}
    ";"     { return symbol(";");}
    "["     { return symbol("[");}
    "]"     { return symbol("]");}
    "]["     { return symbol(",");}
    "++"    { return symbol("++");}
    "--"    { return symbol("--");}
    "-"     { return symbol("-");}
    /* CHARACTER */
    "'"     { yybegin(CHARACTER); return symbol("char", yytext());}
    /* STRINGS */
    \"      { yybegin(STRING); string.setLength(0); string.append("\"");}
     /* SINGLE LINE COMMENTS */
    "##"    { yybegin(SINGLE_COMMENT); string.setLength(0); string.append("##");}
    /* MULTIPLE LINE COMMENTS */
    "/#"    {yybegin(MULT_COMMENT); string.setLength(0); string.append("/#");}
    /* KEYWORDS */
    "start"       { return symbol("start");}
    "function"    { return symbol("function");}
    "void"        { return symbol("void");}
    "record"      { flag = true; return symbol("record");}
    "const"       { return symbol("const");}
    "auto"        { return symbol("auto");}
    "return"      { return symbol("return");}
    "break"       { return symbol("break");}
    "continue"    { return symbol("continue");}
    "len"         { return symbol("len");}
    "if"          { return symbol("if");}
    "else"        { return symbol("else");}
    "switch"      { return symbol("switch");}
    "of"          { return symbol("of");}
    "case"        { return symbol("case");}
    "default"     { return symbol("default");}
    "for"         { return symbol("for");}
    "repeat"      { return symbol("repeat");}
    "until"       { return symbol("until");}
    "foreach"     { return symbol("foreach");}
    "in"          { return symbol("in");}
    "sizeof"      { return symbol("sizeof");}
    "int"         { return symbol("base_type");}
    "long"        { return symbol("base_type");}
    "double"      { return symbol("base_type");}
    "float"       { return symbol("base_type");}
    "char"        { return symbol("base_type");}
    "string"      { return symbol("base_type");}
    "bool"        { return symbol("base_type");}
    "new"         { return symbol("new");}
    "println"     { return symbol("println");}
    "input"       { return symbol("input");}
    "true"        { return symbol("true");}
    "false"       { return symbol("false");}
    /* VARIABLES */
    {id}                {   String temp = yytext();
                            if(records.contains(temp)){
                                flag = false;
                                return symbol("rec_id",temp);
                            } if(flag){
                                flag = false;
                                records.add(temp);
                                return symbol("rec_id",temp);
                            }
                            return symbol("id",temp);}
    /* NUMBERS */
    {IntLiteral}        {return symbol("int_const", Integer.valueOf(yytext()));}
    {HexLiteral}        {return symbol("int_const", yytext());}
    {RealLiteral}       {return symbol("real_const", Double.valueOf(yytext()));}
    {ScientificLiteral} {return symbol("real_const", yytext());}
    /* WHITESPACE */
    {WhiteSpace}        {/* skip */}
}

<CHARACTER>{
    "'"                 {yybegin(YYINITIAL); string.append("'"); return symbol("char", string);}
    {StringCharacter}   {string.append(yytext());}
    {SpecialCharacter}  {string.append(yytext());}
}

<STRING>{
    \"                  {yybegin(YYINITIAL); string.append("\""); StringBuilder temp = string; string = new StringBuilder(); return symbol("string", temp.toString());}
    {StringCharacter}+  {string.append(yytext());}
    {SpecialCharacter}+ {string.append(yytext());}
}

<SINGLE_COMMENT>{
    \n                              {yybegin(YYINITIAL);}
    {SingleCommentCharacter}+       {}
}

<MULT_COMMENT>{
    "#/"                            {yybegin(YYINITIAL);}
    "#"                             {}
    \n                              {}
    \r\n                            {}
    "	"                           {}
    {MultilpleCommentCharacter}+    {}
}

[^]        { throw new RuntimeException("Illegal character \""+yytext()+
                                        "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>    {return symbol("$");}