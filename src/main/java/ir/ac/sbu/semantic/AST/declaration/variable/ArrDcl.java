package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.symbolTable.DSCPs.*;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;


public class ArrDcl extends VarDCL {

    private List<Expression> dimensions;

    //for !auto and don't have assignment
    public ArrDcl(String name, Type type, boolean global, int dimSize) {
        this.name = name;
        this.type = type;
        this.global = global;
        dimensions = new ArrayList<>(dimSize);
    }

    //for have assignment
    public ArrDcl(String name, String stringType, boolean global, Integer dimSize, Type type, List<Expression> dimensions) {
        this.name = name;
        if (!stringType.equals("auto")) {
            if (!SymbolTableHandler.getTypeFromName(stringType).equals(type))
                throw new RuntimeException("the types of array doesn't match");
        } else if (dimensions == null)
            throw new RuntimeException("auto variables must have been initialized");
        if (dimSize != null)
            if (dimSize != dimensions.size())
                throw new RuntimeException("dimensions are't correct");
        this.type = type;
        this.global = global;
        this.dimensions = dimensions;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if (global){
            executeGlobalExp(cw);
            declare();
            //TODO what is it?
            String repeatedArray = new String(new char[dimensions.size()]).replace("\0", "[");
            Type arrayType = Type.getType(repeatedArray + type.getDescriptor());
            cw.visitField(ACC_STATIC, name, arrayType.getDescriptor(), null, null).visitEnd();
        }
        else {
            dimensions.forEach((dim) -> dim.codegen(mv, cw));
            declare();
            if (dimensions.size() == 1) {
                if (type.getDescriptor().endsWith(";"))
                    mv.visitTypeInsn(ANEWARRAY, getType().getElementType().getInternalName());
                else
                    mv.visitIntInsn(NEWARRAY, SymbolTableHandler.getTType(getType().getElementType()));
            } else
                mv.visitMultiANewArrayInsn(type.getDescriptor(), dimensions.size());
            mv.visitVarInsn(ASTORE, ((LocalDSCP) SymbolTableHandler.getInstance().getDescriptor(name)).getIndex());
        }
    }

    private void executeGlobalExp(ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>",
                "()V", null, null);
        mv.visitCode();
        dimensions.forEach((dim) -> dim.codegen(mv, cw));
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void declare() {
        DSCP dscp;
        if (!global)
            dscp = new LocalArrDSCP(type, true, SymbolTableHandler.getInstance().newIndex(), dimensions);
        else
            dscp = new GlobalArrDSCP(type, true, dimensions);
        SymbolTableHandler.getInstance().addVariable(name, dscp);

    }
}
