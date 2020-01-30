package ir.ac.sbu.semantic.AST.expression.unary;


import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.INEG;

public class Negative extends UnaryExp { //-


    public Negative(Expression operand) {
        super(operand);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        operand.codegen(mv, cw);
        if(type != Type.BOOLEAN_TYPE)
            throw new RuntimeException("It's not number!");
        mv.visitInsn(type.getOpcode(INEG));
    }
}


