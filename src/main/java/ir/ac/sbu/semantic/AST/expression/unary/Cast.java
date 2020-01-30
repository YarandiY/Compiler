package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


public class Cast extends UnaryExp {

    private Type to;

    public Cast(Expression operand,Type to) {
        super(operand);
        this.to = to;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        operand.codegen(mv, cw);
        Type from = type;
        if(from == to)
            return;
        if(to != Type.INT_TYPE || to != Type.LONG_TYPE || to != Type.DOUBLE_TYPE || to != Type.FLOAT_TYPE)
            throw new RuntimeException("the cast is wrong!!");
        mv.visitInsn(getOpcode(from,to));
        type = to;
    }


    //Just for int and long and double and float
    private int getOpcode(Type from,Type to){
        int opcode;
        if(from == Type.INT_TYPE){
            if(to == Type.LONG_TYPE)
                opcode = Opcodes.I2L;
            else if(to == Type.FLOAT_TYPE)
                opcode = Opcodes.I2F;
            else
                opcode = Opcodes.I2D;
        }
        else if(from == Type.LONG_TYPE){
            if(to == Type.INT_TYPE)
                opcode = Opcodes.L2I;
            else if(to == Type.DOUBLE_TYPE)
                opcode = Opcodes.L2D;
            else
                opcode = Opcodes.L2F;

        }
        else if(from == Type.DOUBLE_TYPE){
            if(to == Type.INT_TYPE)
                opcode = Opcodes.D2I;
            else if(to == Type.LONG_TYPE)
                opcode = Opcodes.D2L;
            else
                opcode = Opcodes.D2F;
        }
        else{
            if(to == Type.LONG_TYPE)
                opcode = Opcodes.F2L;
            else if (to == Type.INT_TYPE)
                opcode = Opcodes.F2I;
            else
                opcode = Opcodes.F2D;
        }
        return opcode;
    }
}
