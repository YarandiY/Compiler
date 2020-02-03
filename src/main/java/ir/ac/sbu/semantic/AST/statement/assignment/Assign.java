package ir.ac.sbu.semantic.AST.statement.assignment;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalDSCP;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.PUTSTATIC;

public class Assign extends Assignment {

    public Assign(Expression expression, Variable variable) {
        super(expression, variable);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        checkConst();
        DSCP dscp = variable.getDSCP();
        expression.codegen(mv, cw);

        if (variable.getType() != expression.getType())
            throw new RuntimeException("you should cast expression!");
        if(dscp instanceof LocalDSCP) {
            int index = ((LocalDSCP) dscp).getIndex();
            mv.visitVarInsn(variable.getType().getOpcode(ISTORE), index);
        }
        else
            mv.visitFieldInsn(PUTSTATIC, "$Main", variable.getName(), dscp.getType().toString());
        dscp.setValid(true);
    }
}
