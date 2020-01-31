package ir.ac.sbu.semantic.AST.expression.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.Type;

public abstract class Variable extends Expression {

    String name;

    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return getDSCP().getType();
    }

    public DSCP getDSCP() {
        return SymbolTableHandler.getInstance().getDescriptor(name);
    }
}
