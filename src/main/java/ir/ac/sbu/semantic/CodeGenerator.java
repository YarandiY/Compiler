package ir.ac.sbu.semantic;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.arithmetic.*;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.*;
import ir.ac.sbu.syntax.Lexical;

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
        switch (sem){
            case "mkFuncDSCP" : break;
            case "addFuncToMain" : break;
            case "pop" : semanticStack.pop();
                break;
            case "push" :
                //semanticStack.push(new IntegerConst((int) lexical.currentToken().getValue()));
                semanticStack.push(lexical.nextToken());
                break;
            case "mkArrDSCP" : break;
            case "addDimList" : break;
            case "pushReal" : semanticStack.push(lexical.nextToken());
                break;
            case "pushInt" : break;
            case "premm" : break;
            case "prepp" : break;
            case "mkptr" : break;
            case "adrCal" : break;
            case "postpp" : break;
            case "postmm" : break;
            case "checkConst" : break;
            case "popST" : break;
            case "mkSDSCP" : break;
            case "mkJustArrDSCP" : break;
            case "addDimNum" : break;
            case "putInST" : break;
            case "assign" : break;
            case "minDimNum" : break;
            /* --------------------- binary expressions --------------------- */
            /* ---------------------- Arithmetic ---------------------------- */
            case "div" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Divide(first, second));
                break;
            }
            case "minus" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Minus(first, second));
                break;
            }
            case "mult" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Multiply(first, second));
                break;
            }
            case "rmn" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Remainder(first, second));
                break;
            }
            case "sum" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Sum(first, second));
                break;
            }
            /* ---------------------- conditional ------------------------- */
            case "and" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new And(first, second));
                break;
            }
            case "andBit" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new AndBit(first, second));
                break;
            }
            case "biggerAndEqual" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new BiggerEqual(first, second));
                break;
            }
            case "biggerThan" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new BiggerThan(first, second));
                break;
            }
            case "equal" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new Equal(first, second));
                break;
            }
            case "lessAndEqual" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new LessEqual(first, second));
                break;
            }
            case "lessThan" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new LessThan(first, second));
                break;
            }
            case "notEqual" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new NotEqual(first, second));
                break;
            }
            case "or" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new OR(first, second));
                break;
            }
            case "orBit" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new ORBit(first, second));
                break;
            }
            case "XORBit" : {
                Expression second = (Expression) semanticStack.pop();
                Expression first = (Expression) semanticStack.pop();
                semanticStack.push(new XORBit(first, second));
                break;
            }
            /* --------------------------    ---------------------------- */
            case "not" : break;
            case "addAssign" : break;
            case "minAssign" : break;
            case "divAssign" : break;
            case "multAssign" : break;
            case "rmnAssign" : break;
            case "break" : break;
            case "continue" : break;
            case "createIfST" : break;
            case "createElseST" : break;
            case "createSwitchST" : break;
            case "createNewCase" : break;
            case "createLookupTB" : break;
            case "createLookupTBDefault" : break;
            case "tilda" : break;
            case "doNot" : break;
            case "cast" : break;
            case "inputLine" : break;
            case "input" : break;
            case "len" : break;
            case "JZRepeat" : break;
            case "createFlags" : break;
            case "trueInitFlag" : break;
            case "trueStepFlag" : break;
            case "JZFor" : break;
            case "JZForeach" : break;
            case "print" : break;
            case "mkRecDSCP" : break;
            case "addDCLsList" : break;
            case "voidReturn" : break;
            case "return" : break;
            case "check2Type" : break;
            case "ifZDimNum" : break;
            case "pushSize" : break;
            case "checkRec" : break;
            case "mkDSCP" : break;
            //case "assign" : break;
            //case "mkSDSCP" : break;
            case "constTrue" : break;
            default:
                System.out.println("Illegal semantic function: " + sem);

        }
    }
}