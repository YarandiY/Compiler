package ir.ac.sbu.semantic.AST.expression;

import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.AST.expression.constant.IntegerConst;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class Sizeof extends Expression implements Operation {

    private Integer value;

    public Sizeof(String baseType){
       value = SymbolTableHandler.getSize(baseType);
        type = Type.INT_TYPE;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        IntegerConst.storeIntValue(mv,value);
    }
}
