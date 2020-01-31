package ir.ac.sbu.semantic.AST.expression.binary.conditional;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.BinaryExp;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.IXOR;

public class XORBit extends BinaryExp { //^

    public XORBit(Expression firstop, Expression secondop) {
        super(firstop, secondop);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        firstop.codegen(mv, cw);
        secondop.codegen(mv, cw);
        mv.visitInsn(type.getOpcode(IXOR));
    }
}
