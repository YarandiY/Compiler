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

    public FuncReturn(Expression expression,FunctionDcl funcDcl) {
        this.expression = expression;
        scope = SymbolTableHandler.getInstance().getLastScope();
        funcDcl.getReturns().forEach((r) -> {
            if(r.scope == scope) {
                throw new RuntimeException("more than one return in single scope -__-");
            }
        });
        funcDcl.addReturn(this);
        if((expression == null && !funcDcl.getType().equals(Type.VOID_TYPE)) ||
                (expression != null && (funcDcl.getType().equals(Type.VOID_TYPE) ||
                        !funcDcl.getType().equals(expression.getType()) )))
            throw new RuntimeException("Return type mismatch");
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        FunctionDcl functionDcl = SymbolTableHandler.getInstance().getLastFunction();
        if(expression == null)
            mv.visitInsn(RETURN);
        else {
            mv.visitInsn(Cast.getOpcode(expression.getType(),functionDcl.getType()));
            mv.visitInsn(expression.getType().getOpcode(IRETURN));
        }

    }
}
