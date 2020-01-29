package ir.ac.sbu.semantic.AST.expression.unary;


import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.INEG;

public class Negative extends Unary {


    public Negative(Expression operand) {
        super(operand);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        operand.codegen(mv, cw);
        mv.visitInsn(type.getOpcode(INEG));
    }
}
