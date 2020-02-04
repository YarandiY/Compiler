package ir.ac.sbu.semantic.symbolTable;

import ir.ac.sbu.semantic.AST.declaration.function.FunctionDcl;
import ir.ac.sbu.semantic.AST.declaration.record.RecordDcl;
import ir.ac.sbu.semantic.AST.statement.Condition.Switch;
import ir.ac.sbu.semantic.AST.statement.loop.Loop;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalDSCP;
import lombok.Data;
import org.objectweb.asm.Opcodes;
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
        globalSymTbl.setTypeOfScope(Scope.GLOBAL);
        stackScopes.add(globalSymTbl);
    }
    public static SymbolTableHandler getInstance() {
        return instance;
    }

    private FunctionDcl LastFunction;
    private Loop innerLoop;
    private Switch lastSwitch;

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
            case "I":
                type = Type.INT_TYPE;
                break;
            case "long":
            case "Long":
            case "J":
                type = Type.LONG_TYPE;
                break;
            case "char":
            case "Character":
            case "C":
                type = Type.CHAR_TYPE;
                break;
            case "bool":
            case "Boolean":
            case "Z":
                type = Type.BOOLEAN_TYPE;
                break;
            case "double":
            case "Double":
            case "D":
                type = Type.DOUBLE_TYPE;
                break;
            case "float":
            case "Float":
            case "F":
                type = Type.FLOAT_TYPE;
                break;
            case "string":
            case "String":
            case "Ljava/lang/String;":
                type = Type.getType("Ljava/lang/String;");
                break;
            case "void":
            case "V":
                type = Type.VOID_TYPE;
                break;
            default:
                type = Type.getType("L" + varType + ";");

        }
        return type;
    }

    public static int getTType(Type type){
        if(type == Type.INT_TYPE)
            return Opcodes.T_INT;
        else if(type == Type.LONG_TYPE)
            return Opcodes.T_LONG;
        else if(type == Type.DOUBLE_TYPE)
            return Opcodes.T_DOUBLE;
        else if(type == Type.CHAR_TYPE)
            return Opcodes.T_CHAR;
        else if(type == Type.BOOLEAN_TYPE)
            return Opcodes.T_BOOLEAN;
        else if(type == Type.FLOAT_TYPE)
            return Opcodes.T_FLOAT;
        else
            throw new RuntimeException(type + " is not correct");
    }

    public Set<String> getFuncNames() {
        return funcDcls.keySet();
    }

    public void popScope() {
        stackScopes.remove(stackScopes.size() - 1);
    }

    public void addScope(Scope typeOfScope) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.setTypeOfScope(typeOfScope);
        if (typeOfScope != Scope.FUNCTION)
            symbolTable.setIndex(getLastScope().getIndex());
        stackScopes.add(symbolTable);
    }

    public SymbolTable getLastScope() {
        if (stackScopes.size() == 0)
            throw new RuntimeException("Something Goes Wrong");

        return stackScopes.get(stackScopes.size() - 1);
    }


    //To declare a function add it to funcDcls
    public void addFunction(FunctionDcl funcDcl) {
        if (funcDcls.containsKey(funcDcl.getName())) {
            if(funcDcls.get(funcDcl.getName()).contains(funcDcl)){
                int index = funcDcls.get(funcDcl.getName()).indexOf(funcDcl);
                FunctionDcl lastfunc = funcDcls.get(funcDcl.getName()).get(index);
                if((lastfunc.getBlock() != null && funcDcl.getBlock() != null) ||
                        (lastfunc.getBlock() == null && funcDcl.getBlock() == null) )
                    throw new RuntimeException("the function is duplicate!!!");

            }else{
                funcDcls.get(funcDcl.getName()).add(funcDcl);
            }
        } else {
            ArrayList<FunctionDcl> funcDclList = new ArrayList<>();
            funcDclList.add(funcDcl);
            funcDcls.put(funcDcl.getName(), funcDclList);
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
            throw new RuntimeException("function " + name  + " with inputs " + inputs + " wasn't found");
        } else {
            throw new RuntimeException("function " + name  + " with inputs " + inputs + " wasn't found");
        }
    }

    //declare a variable to the last symbol table
    public void addVariable(String name, DSCP dscp) {
        if (getLastScope().containsKey(name)) {
            throw new RuntimeException("the variable declare previously");
        }
        if (dscp instanceof LocalDSCP) {
            getLastScope().put(name, dscp);
            getLastScope().addIndex(dscp.getType().getSize() - 1);
        }
        else
            stackScopes.get(0).put(name, dscp);
    }

    public DSCP getDescriptor(String name) {
        int symbolTbl = stackScopes.size() - 1;
        while (symbolTbl >= 0) {
            if (stackScopes.get(symbolTbl).containsKey(name))
                return stackScopes.get(symbolTbl).get(name);
            symbolTbl--;
        }
        throw new RuntimeException("the name " + name +" didn't initial");
    }

    public boolean canHaveBreak() {
        return (lastSwitch != null || innerLoop != null);
    }

    public void addRecord(RecordDcl record) {
        if (recordDcls.containsKey(record.getName()))
            throw new RuntimeException("The record was declared early!");
        recordDcls.put(record.getName(), record);
    }


    private RecordDcl getRecord(String name) {
        if (recordDcls.containsKey(name))
            throw new RuntimeException("Record not Found");

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

    public int getIndex() {
        return getLastScope().getIndex();
    }


}
