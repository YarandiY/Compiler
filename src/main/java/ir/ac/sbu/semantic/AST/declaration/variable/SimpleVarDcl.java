package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.constant.ConstantExp;
import ir.ac.sbu.semantic.AST.expression.variable.SimpleVar;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.semantic.AST.statement.assignment.Assign;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

@Data
public class SimpleVarDcl extends VarDCL {


    private boolean constant;
    private Expression exp;
    private String stringType;

    public void setExp(Expression exp) {
        this.exp = exp;
        SymbolTableHandler.getInstance().getDescriptor(name).setValid(true);
    }


    public SimpleVarDcl(String varName, Type type, boolean constant, boolean global) {
        name = varName;
        this.type = type;
        this.constant = constant;
        this.global = global;
    }

    public SimpleVarDcl(String varName, String type, boolean constant, boolean global, Expression exp) {
        name = varName;
        stringType = type;
        if (!type.equals("auto"))
            this.type = SymbolTableHandler.getTypeFromName(type);
        else
            this.type = null;
        this.constant = constant;
        this.global = global;
        this.exp = exp;
        if (this.type == null)
            if (exp == null)
                throw new RuntimeException("the auto variable must be have expression");
            else
                phonyExpExe();
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if (global) {
            Expression value = null;
            int access = ACC_STATIC;
            access += constant ? ACC_FINAL : 0;
            cw.visitField(access, name, type.getDescriptor(),
                    null, value).visitEnd();
            if (exp != null) {
                executeGlobalExp(cw,mv);
            }
        } else if (exp != null) {
            exp.codegen(mv, cw);
            if (!exp.getType().equals(type))
                throw new RuntimeException("the type of variable and expression doesn't match" +
                        "   " + "the type of var " + type + "   " + "the type of exp " + exp.getType());
            LocalVarDSCP dscp = (LocalVarDSCP) SymbolTableHandler.getInstance().getDescriptor(name);
            mv.visitVarInsn(type.getOpcode(ISTORE), dscp.getIndex());
        }
    }


    private void phonyExpExe() {
        TempMethodVisitor tempMV = new TempMethodVisitor();
        TempClassWriter tempCW = new TempClassWriter();
        exp.codegen(tempMV, tempCW);
        type = exp.getType();
    }

    private void executeGlobalExp(ClassWriter cw,MethodVisitor mv) {
        assign(new SimpleVar(name, type), exp, mv, cw);
    }

    private void declare() {
        DSCP dscp;
        if (!global)
            dscp = new LocalVarDSCP(type, exp != null,
                    SymbolTableHandler.getInstance().getIndex(), constant);
        else
            dscp = new GlobalVarDSCP(type, exp != null, constant);

        SymbolTableHandler.getInstance().addVariable(name, dscp);
    }

    private void assign(Variable variable, Expression expression,
                        MethodVisitor mv, ClassWriter cw) {
        DSCP dscp = variable.getDSCP();
        expression.codegen(mv, cw);

        if (variable.getType() != expression.getType())
            throw new RuntimeException("you should cast expression!");
        if (dscp instanceof LocalDSCP) {
            int index = ((LocalDSCP) dscp).getIndex();
            mv.visitVarInsn(variable.getType().getOpcode(ISTORE), index);
        } else
            mv.visitFieldInsn(PUTSTATIC, "Test", variable.getName(), dscp.getType().toString());
        dscp.setValid(true);
    }
}


class TempMethodVisitor extends MethodVisitor {
    public TempMethodVisitor() {
        super(327680);
    }
}

class TempClassWriter extends ClassWriter {
    public TempClassWriter() {
        super(327680);
    }
}