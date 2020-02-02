package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.variable.SimpleVar;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;

abstract public class UnaryExp extends Expression {
    protected Expression operand;

    UnaryExp(Expression operand){
        this.operand = operand;
    }

    //This is just for postpp,prepp,...
    protected void checkConst(Variable variable) {
        boolean isConst = false;
        if (variable instanceof SimpleVar) {
            DSCP dscp = SymbolTableHandler.getInstance().getDescriptor(variable.getName());
            isConst = (dscp instanceof GlobalVarDSCP) ? ((GlobalVarDSCP) dscp).isConstant() : ((LocalVarDSCP) dscp).isConstant();
        }
        if (isConst)
            throw new RuntimeException("Const variables can't assign");
    }
}
