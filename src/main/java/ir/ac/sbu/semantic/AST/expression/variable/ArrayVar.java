package ir.ac.sbu.semantic.AST.expression.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.BiggerEqual;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalArrDSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;


@Data
public class ArrayVar extends Variable {

    private List<Expression> dimensions;

    public ArrayVar(String name, List<Expression> dimensions, Type type) {
        this.name = name;
        this.dimensions = dimensions;
        this.type = type;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {

        new SimpleVar(name,type).codegen(mv, cw);
        Label exceptionLabel = new Label();
        Label endLabel = new Label();

        for (int i = 0; i < dimensions.size() - 1; i++) {
            dimensions.get(i).codegen(mv, cw);
            DSCP dscp = SymbolTableHandler.getInstance().getDescriptor(name);
            if (dscp instanceof GlobalArrDSCP){
                BiggerEqual biggerEqual = new BiggerEqual(dimensions.get(i), ((GlobalArrDSCP) dscp).getDimList().get(i));
                biggerEqual.codegen(mv, cw);
                mv.visitJumpInsn(IFGE, exceptionLabel);
            }
            if (dimensions.get(i).getType().equals(Type.INT_TYPE))
                throw new RuntimeException("Index should be an integer number");
            mv.visitInsn(AALOAD);
        }
        // must load the last index separately
        dimensions.get(dimensions.size() - 1).codegen(mv, cw);
        if(type.getDescriptor().endsWith(";")) // we have array of records
            mv.visitInsn(AALOAD);
        else
            mv.visitInsn(type.getOpcode(IALOAD));

        mv.visitJumpInsn(GOTO, endLabel);
        mv.visitLabel(exceptionLabel);
        mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "()V", false);
        mv.visitInsn(ATHROW);
        mv.visitLabel(endLabel);
    }
}
