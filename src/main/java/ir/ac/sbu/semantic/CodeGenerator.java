package ir.ac.sbu.semantic;

import ir.ac.sbu.semantic.AST.Node;
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
            case "push" : semanticStack.push(lexical.nextToken());
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
            case "sum" : break;
            case "minus" : break;
            case "and" : break;
            case "andBit" : break;
            case "addAssign" : break;
            case "minAssign" : break;
            case "divAssign" : break;
            case "multAssign" : break;
            case "rmnAssign" : break;
            case "break" : break;
            case "continue" : break;
            case "lessThan" : break;
            case "lessAndEqual" : break;
            case "biggerThan" : break;
            case "biggerAndEqual" : break;
            case "createIfST" : break;
            case "createElseST" : break;
            case "createSwitchST" : break;
            case "createNewCase" : break;
            case "createLookupTB" : break;
            case "createLookupTBDefault" : break;
            case "equal" : break;
            case "notEqual" : break;
            case "or" : break;
            case "not" : break;
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
            case "mult" : break;
            case "div" : break;
            case "rmn" : break;
            case "notBit" : break;
            case "orBit" : break;
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
                System.out.println("the " + sem + " isn't here!!");

        }
    }
}