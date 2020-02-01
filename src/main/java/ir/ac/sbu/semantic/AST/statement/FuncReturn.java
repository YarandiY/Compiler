package ir.ac.sbu.semantic.AST.statement;

import ir.ac.sbu.semantic.AST.declaration.function.FunctionDcl;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.unary.Cast;
import ir.ac.sbu.semantic.symbolTable.SymbolTable;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class FuncReturn extends Statement {

    private Expression expression;
    private SymbolTable scope;

    public FuncReturn(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        scope = SymbolTableHandler.getInstance().getLastScope();
        FunctionDcl functionDcl = SymbolTableHandler.getInstance().getLastFunction();
        functionDcl.getReturns().forEach((r) -> {
            if(r.scope == scope)
                throw new RuntimeException("more than one return in single scope -__-");
        });
        functionDcl.addReturn(this);
        if((expression == null && !functionDcl.getType().equals(Type.VOID_TYPE)) ||
                (expression != null && (functionDcl.getType().equals(Type.VOID_TYPE) ||
                        !functionDcl.getType().equals(expression.getType()) )))
            throw new RuntimeException("Return type mismatch");
        if(expression == null)
            mv.visitInsn(RETURN);
        else {
            mv.visitInsn(Cast.getOpcode(expression.getType(),functionDcl.getType()));
            mv.visitInsn(expression.getType().getOpcode(IRETURN));
        }

    }
}
