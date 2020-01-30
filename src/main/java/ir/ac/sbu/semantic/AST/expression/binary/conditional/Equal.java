package ir.ac.sbu.semantic.AST.expression.binary.conditional;

import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;

public class Equal extends CondExp {

    public Equal(Expression firstop, Expression secondop) {
        super(firstop, secondop);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        cmp(IFNE, IF_ICMPNE, mv, cw);
    }
}
