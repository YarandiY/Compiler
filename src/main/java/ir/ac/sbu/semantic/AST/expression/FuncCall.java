package ir.ac.sbu.semantic.AST.expression;

import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.AST.declaration.function.FunctionDcl;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import java.util.ArrayList;
import java.util.List;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class FuncCall extends Expression implements Operation {

    private String id;
    private List<Expression> parameters;

    private FunctionDcl func;

    public FuncCall(String id, ArrayList<Expression> parameters) {
        this.id = id;
        this.parameters = parameters;
    }

    public void addParam(Expression exp){
        if(parameters == null)
            parameters = new ArrayList<>();
        parameters.add(exp);

    }


    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        for (Expression parameter : parameters) {
            parameter.codegen(mv, cw);
        }
        ArrayList<Type> paramTypes = new ArrayList<>();
        for (Expression exp :
                parameters) {
            paramTypes.add(exp.getType());
        }
        this.func = SymbolTableHandler.getInstance().getFunction(id, paramTypes);
        this.type = func.getType();
        if (parameters.size() != func.getParameters().size())
            throw new RuntimeException("error in func parameter");
        mv.visitMethodInsn(INVOKESTATIC, "$Main", func.getName(), func.getSignature(), false);
    }
}
