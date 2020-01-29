package ir.ac.sbu.semantic.AST.expression.constant;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class BooleanConst extends Constant {

    private Boolean value;

    public BooleanConst(Boolean value){
        this.value = value;
        type = Type.BOOLEAN_TYPE;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        mv.visitInsn(value ? ICONST_1:ICONST_0);
    }
}
