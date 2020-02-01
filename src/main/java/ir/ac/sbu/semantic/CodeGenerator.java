package ir.ac.sbu.semantic;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.block.GlobalBlock;
import ir.ac.sbu.semantic.AST.declaration.Declaration;
import ir.ac.sbu.semantic.AST.declaration.function.FunctionDcl;
import ir.ac.sbu.semantic.AST.declaration.variable.SimpleVarDcl;
import ir.ac.sbu.semantic.AST.declaration.variable.VarDCL;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.arithmetic.*;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.*;
import ir.ac.sbu.semantic.AST.expression.constant.*;
import ir.ac.sbu.semantic.AST.expression.unary.*;
import ir.ac.sbu.semantic.AST.expression.variable.RecordVar;
import ir.ac.sbu.semantic.AST.expression.variable.SimpleVar;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.semantic.AST.statement.assignment.*;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import ir.ac.sbu.syntax.Lexical;
import org.objectweb.asm.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
            case "push": {
                semanticStack.push(lexical.currentToken().getValue());
                break;
            }
            case "pop": {
                semanticStack.pop();
                break;
            }
            case "checkConst": {
                Variable variable = (Variable) semanticStack.pop();
                checkConst(variable);
                semanticStack.push(variable);
                break;
            }
            /* --------------------- declarations --------------------- */
            case "mkFuncDCL": {
                Type type = SymbolTableHandler.getTypeFromName((String) semanticStack.pop());
                FunctionDcl functionDcl = new FunctionDcl(type,
                        (String) lexical.currentToken().getValue(), null, new ArrayList<>());
                semanticStack.push(functionDcl);
                break;
            }
            case "addParameter": {
                VarDCL varDCL = (VarDCL) semanticStack.pop();
                FunctionDcl function = (FunctionDcl) semanticStack.pop();
                function.addParameter(varDCL);
                semanticStack.push(function);
                break;
            }
            case "completeFuncDCL": {
                Object node = semanticStack.pop();
                if (node instanceof Block) {
                    Block block = (Block) node;
                    FunctionDcl function = (FunctionDcl) semanticStack.pop();
                    function.setBlock(block);
                    semanticStack.push(function);
                } else if (node instanceof FunctionDcl) {
                    FunctionDcl function = (FunctionDcl) node;
                    GlobalBlock.getInstance().addDeclaration(function);
                } else
                    throw new RuntimeException("something wrong in completeFuncDCL");
                break;
            }
            case "mkSimpleVarDCL": {
                String name = (String) lexical.currentToken().getValue();
                Type type = SymbolTableHandler.getTypeFromName((String) semanticStack.pop());
                SimpleVarDcl varDcl;
                if (semanticStack.peek() instanceof GlobalBlock)
                    varDcl = new SimpleVarDcl(name, type.getDescriptor(), false, true, null);
                else
                    varDcl = new SimpleVarDcl(name, type.getDescriptor(), false, false, null);
                semanticStack.push(varDcl);
                break;
            }
            case "constTrue": {
                Variable var = (Variable) semanticStack.pop();
                DSCP dscp = SymbolTableHandler.getInstance().getDescriptor(var.getName());
                if(dscp instanceof GlobalVarDSCP)
                    ((GlobalVarDSCP)dscp).setConstant(true);
                if(dscp instanceof LocalVarDSCP)
                    ((LocalVarDSCP)dscp).setConstant(true);
                break;
            }
            case "pushBlock": {  //begin
                semanticStack.push(new Block(new ArrayList<>()));
                break;
            }
            case "addBlock": { //fill function's block
                Operation operation = (Operation) semanticStack.pop();
                Block block = (Block) semanticStack.pop();
                block.addOperation(operation);
                semanticStack.push(block);
            }
            case "addGlobalBlock": {
                Declaration declaration = (Declaration) semanticStack.pop();
                if(declaration instanceof  FunctionDcl)
                    addFuncToGlobalBlock((FunctionDcl) declaration);
                else
                    GlobalBlock.getInstance().addDeclaration(declaration);
            }
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
                Variable var = (Variable) semanticStack.pop();
                checkConst(var);
                if (var instanceof RecordVar)
                    throw new RuntimeException("Undefined operand for record type");
                semanticStack.push(new PostMM(var));
                break;
            }
            case "postpp": {
                Variable var = (Variable) semanticStack.pop();
                checkConst(var);
                if (var instanceof RecordVar)
                    throw new RuntimeException("Undefined operand for record type");
                semanticStack.push(new PostPP(var));
                break;
            }
            case "premm": {
                Variable var = (Variable) semanticStack.pop();
                checkConst(var);
                if (var instanceof RecordVar)
                    throw new RuntimeException("Undefined operand for record type");
                semanticStack.push(new PreMM(var));
                break;
            }
            case "prepp": {
                Variable var = (Variable) semanticStack.pop();
                checkConst(var);
                if (var instanceof RecordVar)
                    throw new RuntimeException("Undefined operand for record type");
                semanticStack.push(new PrePP(var));
                break;
            }
            /* --------------------------Const  ---------------------------- */
            case "pushReal": {
                Object realNum = lexical.currentToken().getValue();
                if (realNum instanceof Float)
                    semanticStack.push(new FloatConst((Float) realNum));
                else
                    semanticStack.push(new DoubleConst((Double) realNum));
                break;
            }
            case "pushInt": {
                Object integerNum = lexical.currentToken().getValue();
                if (integerNum instanceof Integer)
                    semanticStack.push(new IntegerConst((Integer) integerNum));
                else
                    semanticStack.push(new LongConst((Long) integerNum));
                break;
            }
            case "sizeof": {
                String baseType = (String) semanticStack.pop();
                semanticStack.push(new Sizeof(baseType));
            }
            case "pushBool": {
                semanticStack.push(new BooleanConst((Boolean) lexical.currentToken().getValue()));
                break;
            }
            case "pushChar": {
                semanticStack.push(new CharConst((Character) lexical.currentToken().getValue()));
                break;
            }
            case "pushString": {
                semanticStack.push(new StringConst((String) lexical.currentToken().getValue()));
                break;
            }
            /* -------------------------- variable ---------------------------- */
            case "pushVar": {
                semanticStack.push(new SimpleVar((String) lexical.currentToken().getValue()));
                break;
            }
            /* ---------------------- Assignment ---------------------------- */
            case "assign": {
                Expression exp = (Expression) semanticStack.pop();
                Variable var = (Variable) semanticStack.pop();
                semanticStack.push(new Assign(exp, var));
                break;
            }
            case "sumAssign": {
                Expression exp = (Expression) semanticStack.pop();
                Variable var = (Variable) semanticStack.pop();
                semanticStack.push(new SumAssign(exp, var));
                break;
            }
            case "minAssign": {
                Expression exp = (Expression) semanticStack.pop();
                Variable var = (Variable) semanticStack.pop();
                semanticStack.push(new MinAssign(exp, var));
                break;
            }
            case "divAssign": {
                Expression exp = (Expression) semanticStack.pop();
                Variable var = (Variable) semanticStack.pop();
                semanticStack.push(new DivAssign(exp, var));
                break;
            }
            case "mulAssign": {
                Expression exp = (Expression) semanticStack.pop();
                Variable var = (Variable) semanticStack.pop();
                semanticStack.push(new MulAssign(exp, var));
                break;
            }
            case "rmnAssign": {
                Expression exp = (Expression) semanticStack.pop();
                Variable var = (Variable) semanticStack.pop();
                semanticStack.push(new RmnAssign(exp, var));
                break;
            }
            /* ----------------------  ---------------------------- */
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
            case "mkArrDSCP":
                break;
            case "addDimList":
                break;
            case "mkptr":
                break;
            case "adrCal":
                break;
            case "popST":
                break;
            case "mkJustArrDSCP":
                break;
            case "addDimNum":
                break;
            case "putInST":
                break;
            case "minDimNum":
                break;
            default:
                System.out.println("Illegal semantic function: " + sem);

        }
    }

    private void checkConst(Variable variable) {
        boolean isConst = false;
        if (variable instanceof SimpleVar) {
            DSCP dscp = SymbolTableHandler.getInstance().getDescriptor(variable.getName());
            isConst = (dscp instanceof GlobalVarDSCP) ? ((GlobalVarDSCP) dscp).isConstant() : ((LocalVarDSCP) dscp).isConstant();
        }
        if (isConst)
            throw new RuntimeException("Const variables can't assign");
    }

    private void addFuncToGlobalBlock(FunctionDcl function){
        if(GlobalBlock.getInstance().getDeclarationList().contains(function)){
            int index = GlobalBlock.getInstance().getDeclarationList().indexOf(function);
            FunctionDcl lastFunc = (FunctionDcl) GlobalBlock.getInstance().getDeclarationList().get(index);
            if(lastFunc.getBlock() == null && function.getBlock() != null){
                GlobalBlock.getInstance().getDeclarationList().remove(lastFunc);
                GlobalBlock.getInstance().addDeclaration(function);
            }else if(lastFunc.getBlock() != null && lastFunc.getBlock() == null){ }
            else
                throw new RuntimeException("the function is duplicate!!!");
        }else{
            GlobalBlock.getInstance().addDeclaration(function);
        }

    }
}