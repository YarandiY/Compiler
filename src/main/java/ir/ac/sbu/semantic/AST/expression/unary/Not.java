package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.IXOR;
import static org.objectweb.asm.Opcodes.ICONST_M1;


public class Not extends Unary {
    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if(type != Type.INT_TYPE || type != Type.LONG_TYPE)
            throw new RuntimeException("It's not real or integer.so I can't not it!");
        operand.codegen(mv, cw);
        mv.visitInsn(ICONST_M1);
        mv.visitInsn(type.getOpcode(IXOR));
    }
}
