package ir.ac.sbu.semantic.AST.expression;

import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.AST.expression.variable.ArrayVar;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Len extends Expression implements Operation{

    private Expression expression;

    public Len(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        expression.codegen(mv, cw);
        type = expression.getType();
        if (expression instanceof ArrayVar) {
            mv.visitInsn(ARRAYLENGTH);
        }
        else
            throw new RuntimeException("input of len function is not iterable");
    }
}
