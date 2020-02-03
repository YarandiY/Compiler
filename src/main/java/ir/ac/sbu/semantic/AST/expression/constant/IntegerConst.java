package ir.ac.sbu.semantic.AST.expression.constant;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class IntegerConst extends ConstantExp {

    private Integer value;

    public IntegerConst(Integer value){
        type = Type.INT_TYPE;
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        storeIntValue(mv,value);
    }

    public static void storeIntValue(MethodVisitor mv,Integer value){

            mv.visitLdcInsn(value);

    }
}
