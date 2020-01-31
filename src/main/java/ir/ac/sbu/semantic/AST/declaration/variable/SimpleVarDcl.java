package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.constant.ConstantExp;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class SimpleVarDcl extends VarDCL {


    private boolean constant = false;
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
        if (!type.equals("auto")) {
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

        // to fill DSCP and add to Symbol table
        declare();

        if(global){
            Object value = null;
            if(exp != null){
                executeGlobalExp(cw);
                value = ((ConstantExp) exp).getValue();
            }
            cw.visitField(ACC_STATIC, name, type.getDescriptor(),
                    null, value).visitEnd();
        }
        else if (exp != null) {
            exp.codegen(mv, cw);
            if (exp.getType() != type)
                throw new RuntimeException("the type of variable and expression doesn't match");
            LocalVarDSCP dscp = (LocalVarDSCP) SymbolTableHandler.getInstance().getDescriptor(name);
            mv.visitVarInsn(getType().getOpcode(ISTORE), dscp.getIndex());
        }
    }


    private void phonyExpExe() {
        TempMethodVisitor tempMV = new TempMethodVisitor();
        TempClassWriter tempCW = new TempClassWriter();
        exp.codegen(tempMV,tempCW);
        type = exp.getType();
    }

    private void executeGlobalExp(ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>",
                "()V", null, null);
        mv.visitCode();
        exp.codegen(mv, cw);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void declare() {
        DSCP dscp;
        if (!global)
            dscp = new LocalVarDSCP(type, exp != null,
                    SymbolTableHandler.getInstance().newIndex(), constant);
        else
            dscp = new GlobalVarDSCP(type, exp != null, constant);

        SymbolTableHandler.getInstance().addVariable(name, dscp);
    }
}

class TempMethodVisitor extends MethodVisitor {
    public TempMethodVisitor() {
        super(327680);
    }
}

class TempClassWriter extends ClassWriter{
    public TempClassWriter() { super(327680); }
}