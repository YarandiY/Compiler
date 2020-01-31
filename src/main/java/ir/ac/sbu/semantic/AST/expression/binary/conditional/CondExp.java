package ir.ac.sbu.semantic.AST.expression.binary.conditional;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.BinaryExp;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

abstract class CondExp extends BinaryExp {

    CondExp(Expression firstop, Expression secondop) {
        super(firstop, secondop);
    }


    void cmp(int notIntOpcode, int intOpcode, MethodVisitor mv, ClassWriter cw) {

        firstop.codegen(mv, cw);
        secondop.codegen(mv, cw);
        Type type = getType();
        int opcode;

        if (type == Type.FLOAT_TYPE){
            mv.visitInsn(Opcodes.FCMPG);
            opcode = notIntOpcode;
        }
        else if (type == Type.DOUBLE_TYPE){
            mv.visitInsn(Opcodes.DCMPG);
            opcode = notIntOpcode;
        }
        else if(type == Type.LONG_TYPE) {
            mv.visitInsn(Opcodes.LCMP);
            opcode = notIntOpcode;
        }
        else {
            opcode = intOpcode;
        }

        /*
        jnz opcode, label1 (if this is true, the main exp will be false)
        code for TRUE
        GOTO label2
        label1: code for FALSE
        label2: ...
        */
        Label label1 = new Label();
        Label label2 = new Label();
        mv.visitJumpInsn(opcode, label1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, label2);
        mv.visitLabel(label1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(label2);
    }

    void AndOr(boolean isAnd, MethodVisitor mv, ClassWriter cw) {

        Label label = new Label();
        Label EndLabel = new Label();


        // handled short circuit
        // for AND if one operand is zero(false) we should jp to code for false(ICONST_0)that is label
        // for OR if one operand is not zero(true) we should jp to code for true(ICONST_1)that is label, too
        // for AND if the result will be true, we should do the code for ICONST_1 and then GOTO out(EndLabel)
        // for OR if the result will be false, we should do the code for ICONST_0 and then GOTO out(EndLabel)
        firstop.codegen(mv, cw);
        mv.visitJumpInsn(isAnd? IFEQ : IFNE, label);
        secondop.codegen(mv, cw);
        mv.visitJumpInsn(isAnd? IFEQ : IFNE, label);
        mv.visitInsn(isAnd? ICONST_1 : ICONST_0);
        mv.visitJumpInsn(GOTO, EndLabel);
        mv.visitLabel(label);
        mv.visitInsn(isAnd? ICONST_0 : ICONST_1);
        mv.visitLabel(EndLabel);

        Type type = getType();

        if (type != Type.BOOLEAN_TYPE && type != Type.INT_TYPE)
            throw new RuntimeException("Only boolean and int Types Can Be Operands Of Conditional And");
    }
}
