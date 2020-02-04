package ir.ac.sbu.semantic.AST.declaration.function;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.declaration.Declaration;
import ir.ac.sbu.semantic.AST.statement.FuncReturn;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalArrDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.*;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

@Data
public class FunctionDcl implements Declaration {

    private Type type;
    private String name;
    private List<ParamPair> parameters = new ArrayList<>();
    private List<Type> paramTypes = new ArrayList<>();
    private String signature;
    private Block block;

    private List<FuncReturn> returns = new ArrayList<>();

    public void addReturn(FuncReturn funcReturn) {
        returns.add(funcReturn);
    }


    public void addParameter(String name,LocalDSCP dscp) {
        ParamPair param = new ParamPair(name,dscp);
        parameters.add(param);
        if (dscp instanceof LocalVarDSCP)
            paramTypes.add(dscp.getType());
        else if (dscp instanceof LocalArrDSCP)
            paramTypes.add(Type.getType("[" + dscp.getType()));
    }


    public FunctionDcl(Type type, String name, Block block, List<ParamPair> parameters) {
        this.type = type;
        this.name = name;
        this.block = block;
        this.parameters = parameters;

        // to fill paramTypes and make signature
        setSig();
    }

    public FunctionDcl(String name, String signature, Block block) {
        this.signature = signature;
        paramTypes = Arrays.asList(Type.getArgumentTypes(signature));
        this.type = Type.getType(signature.substring(signature.indexOf(')') + 1));
        this.name = name;
        this.block = block;

    }

    public void declare() {
        SymbolTableHandler.getInstance().addFunction(this);
    }


    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        setSig();
        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                name, this.signature, null, null);
        //Add current function's symbol table to stackScope
        SymbolTableHandler.getInstance().addScope(Scope.FUNCTION);
        SymbolTableHandler.getInstance().setLastFunction(this);
        methodVisitor.visitCode();
        block.codegen(methodVisitor, cw);
        if (returns.size() == 0)
            throw new RuntimeException("You must use at least one return statement in function!");
//        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        SymbolTableHandler.getInstance().popScope();
        SymbolTableHandler.getInstance().setLastFunction(null);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FunctionDcl && checkIfEqual(((FunctionDcl) o).name, ((FunctionDcl) o).paramTypes);
    }

    // check if two functions are the same
    public boolean checkIfEqual(String name, List<Type> paramTypes) {
        if (!this.name.equals(name))
            return false;
        if (paramTypes.size() != this.paramTypes.size())
            return false;
        for (int i = 0; i < paramTypes.size(); i++) {
            if (!this.paramTypes.get(i).equals(paramTypes.get(i)))
                return false;
        }

        return true;
    }

    private void setSig() {
        paramTypes = new ArrayList<>();
        // to fill paramTypes and make signature
        StringBuilder signature = new StringBuilder("(");
        for (ParamPair param:
             parameters) {
            Type type = param.dscp.getType();
            if (param.dscp instanceof LocalArrDSCP)
                type = Type.getType("[" + param.dscp.getType());
            paramTypes.add(type);
            signature.append(type);
        }
        signature.append(")");
        signature.append(type.toString());
        this.signature = signature.toString();
    }
}

class ParamPair{
    String name;
    LocalDSCP dscp;

    public ParamPair(String name, LocalDSCP dscp) {
        this.name = name;
        this.dscp = dscp;
    }
}