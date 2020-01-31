package ir.ac.sbu.semantic;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.arithmetic.*;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.*;
import ir.ac.sbu.semantic.AST.expression.constant.*;
import ir.ac.sbu.semantic.AST.expression.unary.*;
import ir.ac.sbu.semantic.AST.expression.variable.SimpleVar;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.syntax.Lexical;
import org.objectweb.asm.Type;

import java.util.ArrayDeque;
import java.util.Deque;

public class CodeGenerator implements ir.ac.sbu.syntax.CodeGenerator {
    private Lexical lexical;
    private Deque<Object> semanticStack;

    public CodeGenerator(Lexical lexical) {
        this.lexical = lexical;
        semanticStack = new ArrayDeque<>();
    }

    public Node getResult() {
        return (Node) semanticStack.getFirst();
    }

    public void doSemantic(String sem) {
        switch (sem) {
            /* --------------------- global --------------------- */
            case "pushType":{
                semanticStack.push(lexical.currentToken().getValue()); //its string!!
                break;
            }
            /* ---------------------       --------------------- */
            case "mkFuncDSCP":
                break;
            case "addFuncToMain":
                break;
            case "pop":
                semanticStack.pop();
                break;
            case "mkArrDSCP":
                break;
            case "addDimList":
                break;
            case "mkptr":
                break;
            case "adrCal":
                break;
            case "checkConst":
                break;
            case "popST":
                break;
            case "mkSDSCP":
                break;
            case "mkJustArrDSCP":
                break;
            case "addDimNum":
                break;
            case "putInST":
                break;
            case "assign":
                break;
            case "minDimNum":
                break;
            /* --------------------- binary expressions --------------------- */
            /* ---------------------- Arithmetic ---------------------------- */
            case "div": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Divide(first, second));
                break;
            }
            case "minus": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Minus(first, second));
                break;
            }
            case "mult": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Multiply(first, second));
                break;
            }
            case "rmn": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Remainder(first, second));
                break;
            }
            case "sum": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Sum(first, second));
                break;
            }
            /* ---------------------- conditional ------------------------- */
            case "and": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new And(first, second));
                break;
            }
            case "andBit": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new AndBit(first, second));
                break;
            }
            case "biggerAndEqual": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new BiggerEqual(first, second));
                break;
            }
            case "biggerThan": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new BiggerThan(first, second));
                break;
            }
            case "equal": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Equal(first, second));
                break;
            }
            case "lessAndEqual": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new LessEqual(first, second));
                break;
            }
            case "lessThan": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new LessThan(first, second));
                break;
            }
            case "notEqual": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new NotEqual(first, second));
                break;
            }
            case "or": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new OR(first, second));
                break;
            }
            case "orBit": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new ORBit(first, second));
                break;
            }
            case "xorBit": {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new XORBit(first, second));
                break;
            }
            /* -------------------------- Unary   ---------------------------- */
            case "bitwiseNot": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new BitwiseNot(exp));
                break;
            }
            case "cast": {
                Expression exp = (Expression) semanticStack.pop();
                Type newType = (Type) semanticStack.pop();
                semanticStack.push(new Cast(exp, newType));
                break;
            }
            case "negative": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new Negative(exp));
                break;
            }
            case "not": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new Not(exp));
                break;
            }
            case "postmm": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new PostMM(exp));
                break;
            }
            case "postpp": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new PostPP(exp));
                break;
            }
            case "premm": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new PreMM(exp));
                break;
            }
            case "prepp": {
                Expression exp = (Expression) semanticStack.pop();
                semanticStack.push(new PrePP(exp));
                break;
            }
            /* --------------------------Const  ---------------------------- */
            case "pushReal": {
                Object realNum = lexical.currentToken().getValue();
                if (realNum instanceof Float)
                    semanticStack.push(new FloatConst((Float)realNum));
                else
                    semanticStack.push(new DoubleConst((Double)realNum));
                break;
            }
            case "pushInt": {
                Object integerNum = lexical.currentToken().getValue();
                if (integerNum instanceof Integer)
                    semanticStack.push(new IntegerConst((Integer)integerNum));
                else
                    semanticStack.push(new LongConst((Long)integerNum));
                break;
            }
            case "sizeof":{
                String baseType = (String)semanticStack.pop();
                semanticStack.push(new Sizeof(baseType));
            }
            //TODO --> add these to the graphs
            case "pushBool":{
                semanticStack.push(new BooleanConst((Boolean)lexical.currentToken().getValue()));
                break;
            }
            case "pushChar":{
                semanticStack.push(new CharConst((Character)lexical.currentToken().getValue()));
                break;
            }
            case "pushString":{
                semanticStack.push(new StringConst((String)lexical.currentToken().getValue()));
                break;
            }
            /* -------------------------- variable ---------------------------- */
            case "push":
                semanticStack.push(new SimpleVar((String) lexical.currentToken().getValue()));
                break;


            case "addAssign":
                break;
            case "minAssign":
                break;
            case "divAssign":
                break;
            case "multAssign":
                break;
            case "rmnAssign":
                break;
            case "break":
                break;
            case "continue":
                break;
            case "createIfST":
                break;
            case "createElseST":
                break;
            case "createSwitchST":
                break;
            case "createNewCase":
                break;
            case "createLookupTB":
                break;
            case "createLookupTBDefault":
                break;
            case "inputLine":
                break;
            case "input":
                break;
            case "len":
                break;
            case "JZRepeat":
                break;
            case "createFlags":
                break;
            case "trueInitFlag":
                break;
            case "trueStepFlag":
                break;
            case "JZFor":
                break;
            case "JZForeach":
                break;
            case "print":
                break;
            case "mkRecDSCP":
                break;
            case "addDCLsList":
                break;
            case "voidReturn":
                break;
            case "return":
                break;
            case "check2Type":
                break;
            case "ifZDimNum":
                break;
            case "pushSize":
                break;
            case "checkRec":
                break;
            case "mkDSCP":
                break;
            //case "assign" : break;
            //case "mkSDSCP" : break;
            case "constTrue":
                break;
            default:
                System.out.println("Illegal semantic function: " + sem);

        }
    }
}