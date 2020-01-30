package ir.ac.sbu.semantic.AST.expression.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Data
public abstract class Variable extends Expression {

    protected String name;

    @Override
    public Type getType() {
        return getDSCP().getType();
    }

    public DSCP getDSCP() {
        return SymbolTableHandler.getInstance().getDescriptor(name);
    }
}
