package ir.ac.sbu.semantic.symbolTable;

import ir.ac.sbu.semantic.AST.declaration.function.FunctionDcl;
import ir.ac.sbu.semantic.AST.declaration.record.RecordDcl;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import lombok.Data;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Data
public class SymbolTableHandler {

    private static SymbolTableHandler instance = new SymbolTableHandler();
    private SymbolTableHandler() {
        SymbolTable globalSymTbl = new SymbolTable();
        globalSymTbl.setIndex(1);
        stackScopes.add(globalSymTbl);
    }
    public static SymbolTableHandler getInstance() {
        return instance;
    }

    private static FunctionDcl LastSeenFunction;
    private static boolean inLoop = false;

    private ArrayList<SymbolTable> stackScopes = new ArrayList<>();
    private HashMap<String, ArrayList<FunctionDcl>> funcDcls = new HashMap<>();
    private HashMap<String, RecordDcl> recordDcls = new HashMap<>();


    public static int getSize(String name) {
        int size;
        switch (name) {
            case "int":
            case "Integer":
                size = Integer.SIZE;
                break;
            case "long":
            case "Long":
                size = Long.SIZE;
                break;
            case "char":
            case "Character":
                size = Character.SIZE;
                break;
            case "bool":
            case "Boolean":
                size = 1;
                break;
            case "double":
            case "Double":
                size = Double.SIZE;
                break;
            case "float":
            case "Float":
                size = Float.SIZE;
                break;
            case "string":
            case "String":
                size = Integer.SIZE;
                break;
            default:
                throw new IllegalArgumentException("Type is not Valid");

        }
        return size;
    }
    public static Type getTypeFromName(String varType) {
        Type type;
        switch (varType) {
            case "int":
            case "Integer":
                type = Type.INT_TYPE;
                break;
            case "long":
            case "Long":
                type = Type.LONG_TYPE;
                break;
            case "char":
            case "Character":
                type = Type.CHAR_TYPE;
                break;
            case "bool":
            case "Boolean":
                type = Type.BOOLEAN_TYPE;
                break;
            case "double":
            case "Double":
                type = Type.DOUBLE_TYPE;
                break;
            case "float":
            case "Float":
                type = Type.FLOAT_TYPE;
                break;
            case "string":
            case "String":
                type = Type.getType(String.class);
                break;
            case "void":
                type = Type.VOID_TYPE;
                break;
            default:
                type = Type.getType("L" + varType + ";");

        }
        return type;
    }

    public Set<String> getFuncNames() {
        return funcDcls.keySet();
    }
    public void popScope() {
        stackScopes.remove(stackScopes.size() - 1);
    }


    //To declare a function add it to funcDcls
    public void addFunction(FunctionDcl funcDcl) {
        if (funcDcls.containsKey(funcDcl.getName())) {
            if(funcDcls.get(funcDcl.getName()).contains(funcDcl))
                throw new RuntimeException("the function is duplicate!!!");
            funcDcls.get(funcDcl.getName()).add(funcDcl);
        } else {
            ArrayList<FunctionDcl> funcDclMapper = new ArrayList<>();
            funcDclMapper.add(funcDcl);
            funcDcls.put(funcDcl.getName(), funcDclMapper);
        }
    }

    public FunctionDcl getFunction(String name, ArrayList<Type> inputs) {
        if (funcDcls.containsKey(name)) {
            ArrayList<FunctionDcl> funcDclMapper = funcDcls.get(name);
            for (FunctionDcl f : funcDclMapper) {
                if (f.checkIfEqual(name, inputs)) {
                    return f;
                }
            }
            throw new RuntimeException("no such function was found");
        } else {
            throw new RuntimeException("no such function was found");
        }
    }

    //TODO global variable
    //declare a variable to the last symbol table
    public void addVariable(DSCP dscp, String name) {
        if (getLastFrame().containsKey(name)) {
            throw new RuntimeException("the variable declare previously");
        }
        //TODO what about the global variables??? --> static fields
        getLastFrame().put(name, dscp);
        //if (dscp instanceof DSCP_DYNAMIC)
          //  getLastFrame().addIndex(dscp.getType().getSize() - 1);
    }

    public DSCP getDescriptor(String name) {
        int from = stackScopes.size();
        while (from != 0) {
            from--;
            if (stackScopes.get(from).containsKey(name)) {
                return stackScopes.get(from).get(name);
            }
        }
        throw new RuntimeException("the name doesn't exist");
    }

    public void addScope(Scope typeOfScope) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.setTypeOfScope(typeOfScope);
        if (typeOfScope != Scope.FUNCTION)
            symbolTable.setIndex(getLastFrame().getIndex());
        stackScopes.add(symbolTable);
    }

    //TODO check it!
    public boolean canHaveBreak() {
        if(getLastFrame().getTypeOfScope() == Scope.SWITCH)
            return true;
        return inLoop;
    }

    public void addRecord(RecordDcl record) {
        if (recordDcls.containsKey(record.getName()))
            throw new RuntimeException("The record was declared early!");
        recordDcls.put(record.getName(), record);
    }


    public RecordDcl getRecord(String name) {
        if (recordDcls.containsKey(name))
            throw new RuntimeException("Record BitwiseNot Found");

        return recordDcls.get(name);
    }

    public boolean isRecordDefined(String name){
        try{
            getRecord(name);
            return true;
        }catch (RuntimeException e){
            return false;
        }
    }


    //TODO --> koja estefade mishe khob?
    /*public int returnNewIndex() {
        return getLastFrame().getAndAddIndex();
    }*/


    public SymbolTable getLastFrame() {
        if (stackScopes.size() == 0)
            throw new RuntimeException("Something Goes Wrong");

        return stackScopes.get(stackScopes.size() - 1);
    }
}
