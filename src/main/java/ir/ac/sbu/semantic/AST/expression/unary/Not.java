package ir.ac.sbu.semantic.AST.expression.unary;


import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.constant.ConstantExp;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;

public class Not extends UnaryExp { //not
    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        operand.codegen(mv,cw);
        if(type != Type.INT_TYPE && type != Type.LONG_TYPE && type != Type.BOOLEAN_TYPE)
            throw new RuntimeException("It's not real or integer or bool.so I can't not it!");
        Object res = ((ConstantExp)operand).getValue();
        if(res instanceof Boolean)
            mv.visitInsn(((Boolean)res) ? ICONST_1:ICONST_0);
        if(res instanceof Integer)
            mv.visitInsn(((Integer)res) != 0 ? ICONST_1:ICONST_0);
        if(res instanceof Long)
            mv.visitInsn(((Long)res) != 0 ? ICONST_1:ICONST_0);
    }
}
