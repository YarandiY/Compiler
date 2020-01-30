package ir.ac.sbu.semantic.AST.expression.variable;

import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;

@Data
public class SimpleVar extends Variable{

    public SimpleVar(String name){
        this.name = name;
    }


    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        DSCP dscp = getDSCP();
        //TODO --> depends on DSCPs
//        if (dscp instanceof DSCP_DYNAMIC) {
//            int index = ((DSCP_DYNAMIC) dscp).getIndex();
//            mv.visitVarInsn(type.getOpcode(ILOAD), index);
//        } else {
//            mv.visitFieldInsn(GETSTATIC, cw.toString(), name, type.getDescriptor());
//        }
    }
}
