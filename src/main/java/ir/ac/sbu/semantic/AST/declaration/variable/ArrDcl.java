package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.constant.IntegerConst;
import ir.ac.sbu.semantic.symbolTable.DSCPs.*;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

@Data
public class ArrDcl extends VarDCL {

    private List<Expression> dimensions;
    private int dimNum;

    public ArrDcl(String name, Type type, boolean global, int dimNum) {
        this.name = name;
        this.type = type;
        this.global = global;
        dimensions = new ArrayList<>(dimNum);
        this.dimNum = dimNum;
    }

    public ArrDcl(String name, String stringType, boolean global, Integer dimNum, Type type, List<Expression> dimensions) {
        this.name = name;
        if (!stringType.equals("auto")) {
            if (!SymbolTableHandler.getTypeFromName(stringType).equals(type))
                throw new RuntimeException("the types of array doesn't match");
        } else if (dimensions == null)
            throw new RuntimeException("auto variables must have been initialized");
        if (dimNum != null) {
            if (dimNum != dimensions.size())
                throw new RuntimeException("dimensions are't correct");
            this.dimNum = dimNum;
        }
        this.type = type;
        this.global = global;
        this.dimensions = dimensions;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if (global){
            executeGlobalExp(cw);
            //TODO what is it?
            String repeatedArray = new String(new char[dimensions.size()]).replace("\0", "[");
            Type arrayType = Type.getType(repeatedArray + type.getDescriptor());
            cw.visitField(ACC_STATIC, name, arrayType.getDescriptor(), null, null).visitEnd();
        }
        else {
            for (Expression dim :
                    dimensions ) {
                dim.codegen(mv, cw);
            }
            if(dimensions.size() == 0){
                new IntegerConst(1000).codegen(mv, cw);
            }
            if (dimNum == 1) {
                if (type.getDescriptor().endsWith(";"))
                    mv.visitTypeInsn(ANEWARRAY, getType().getElementType().getInternalName());
                else
                    mv.visitIntInsn(NEWARRAY, SymbolTableHandler.getTType(getType().getElementType()));
            } else
                mv.visitMultiANewArrayInsn(type.getDescriptor(), dimensions.size());
            mv.visitVarInsn(ASTORE, SymbolTableHandler.getInstance().getIndex());
        }
    }

    private void executeGlobalExp(ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>",
                "()V", null, null);
        mv.visitCode();
        for (Expression dim :
                dimensions) {
            dim.codegen(mv,cw);
        }
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    public static void declare(String name,Type type,List<Expression> dimensions,int dimNum,boolean global) {
        DSCP dscp;
        if (!global)
            dscp = new LocalArrDSCP(type, true, SymbolTableHandler.getInstance().getIndex(), dimensions, dimNum);
        else
            dscp = new GlobalArrDSCP(type, true, dimensions, dimNum);
        SymbolTableHandler.getInstance().addVariable(name, dscp);

    }
}
