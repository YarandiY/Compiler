package ir.ac.sbu.semantic.AST.expression.variable;

import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;

public class SimpleVar extends Variable{

    public SimpleVar(String name, Type type){
        this.type = type;
        this.name = name;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        DSCP dscp = getDSCP();
        if(!dscp.isValid())
            throw new RuntimeException("you should set initial value to variable");
        if (dscp instanceof LocalVarDSCP) {
            int index = ((LocalVarDSCP) dscp).getIndex();
            mv.visitVarInsn(type.getOpcode(ILOAD), index);
        } else {
            mv.visitFieldInsn(GETSTATIC,"Main" , name, type.getDescriptor());
        }
    }
}
