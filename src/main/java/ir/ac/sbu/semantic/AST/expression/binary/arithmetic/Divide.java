package ir.ac.sbu.semantic.AST.expression.binary.arithmetic;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.BinaryExp;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.IDIV;

public class Divide extends BinaryExp{

    public Divide(Expression firstop, Expression secondop) {
        super(firstop, secondop);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        firstop.codegen(mv, cw);
        secondop.codegen(mv, cw);
        mv.visitInsn(type.getOpcode(IDIV));
    }
}