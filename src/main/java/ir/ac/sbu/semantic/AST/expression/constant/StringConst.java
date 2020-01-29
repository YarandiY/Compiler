package ir.ac.sbu.semantic.AST.expression.constant;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class StringConst extends Constant {

    private String value;

    public StringConst(String value){
        this.value = value;
        type = Type.getType(String.class);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        mv.visitLdcInsn(value);
    }
}
