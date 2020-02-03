package ir.ac.sbu.semantic.AST.expression.constant;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class CharConst extends ConstantExp {

    private Character value;

    public CharConst(Character value){
        this.value = value;
        type = Type.CHAR_TYPE;
    }


    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        //IntegerConst.storeIntValue(mv, Integer.valueOf(value));
        mv.visitLdcInsn(value);
    }
}
