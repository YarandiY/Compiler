package ir.ac.sbu.semantic.AST.expression.binary.conditional;

import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;

public class NotEqual extends CondExp {

    public NotEqual(Expression firstop, Expression secondop) {
        super(firstop, secondop);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        cmp(IFEQ, IF_ICMPEQ, mv, cw);
    }
}
