package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.constant.ConstantExp;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

@Data
public class SimpleVarDcl extends VarDcl {

    private Expression exp;
    private String stringType;

    public SimpleVarDcl(String varName, Type type, boolean constant, boolean global) {
        name = varName;
        this.type = type;
        this.constant = constant;
        this.global = global;
    }

    public SimpleVarDcl(String varName, String type, boolean constant, boolean global, Expression exp) {
        name = varName;
        stringType = type;
        if (type != "auto") {
            this.type = SymbolTableHandler.getTypeFromName(type);
        } else {
            if (exp == null)
                throw new RuntimeException("the auto variable must be have expression");
        }
        this.constant = constant;
        this.global = global;
        this.exp = exp;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if (type == null)
            phonyExpExe();
        declare();
        if(global){
            int access = ACC_PUBLIC + ACC_STATIC;
            access += constant ? ACC_FINAL : 0;
            Object value = null;
            if(exp != null){
                executeGlobalExp(cw);
                value = ((ConstantExp) exp).getValue();
            }
            FieldVisitor fv = cw.visitField(access, name, type.getDescriptor(), null, value);
            fv.visitEnd();
        }else if (exp != null && !global) {
            exp.codegen(mv, cw);
            if (exp.getType() != type)
                throw new RuntimeException("the type of variable and expression doesn't match");
            //TODO: it depends on DSCPs
            // mv.visitVarInsn(getType().getOpcode(ISTORE), dscpDynamic.getIndex());

        }
    }


    //TODO : I doubt it!
    private void phonyExpExe() {
        TempMethodVisitor tempMV = new TempMethodVisitor(0);
        ClassWriter tempCW = new ClassWriter(0);
        exp.codegen(tempMV,tempCW);
        type = exp.getType();
    }

    //TODO
    private void executeGlobalExp(ClassWriter cw){
        // MethodVisitor mv = cw.visitMethod(ACC_STATIC,"<clinit>",())
    }

    private void declare() {
        if (name == null || type == null)
            throw new RuntimeException("the name or type for the var dcl is null");
        DSCP dscp = new DSCP();
        SymbolTableHandler.getInstance().addVariable(dscp, name);
        //TODO global variables --> needs DSCPs
    }
}

class TempMethodVisitor extends MethodVisitor {
    public TempMethodVisitor(int i) {
        super(327680);
    }
}