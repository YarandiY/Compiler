package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;



//TODO
public class PPPrefix extends UnaryExp {

    public PPPrefix(Expression operand) {
        super(operand);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {

        if (!(operand instanceof Variable) || (type != Type.INT_TYPE && type != Type.DOUBLE_TYPE && type != Type.LONG_TYPE && type != Type.FLOAT_TYPE))
            throw new RuntimeException("the operand is wrong");
        mv.visitInsn(type.getOpcode(ICONST_1));

        Variable variable = (Variable) operand;
        //int index =
        variable.codegen(mv, cw); //to load the var in stack
        mv.visitInsn(type.getOpcode(IADD));
        //mv.visitVarInsn(type.getOpcode(ISTORE),index);
        variable.codegen(mv, cw);

    }
}
