package ir.ac.sbu.semantic.AST.statement.assignment;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.variable.SimpleVar;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.semantic.AST.statement.Statement;
import ir.ac.sbu.semantic.AST.statement.loop.InitExp;
import ir.ac.sbu.semantic.AST.statement.loop.StepExp;
import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.GlobalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.DSCPs.LocalVarDSCP;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;


public abstract class Assignment extends Statement implements InitExp, StepExp {
    protected Expression expression;
    protected Variable variable;

    Assignment(Expression expression, Variable variable) {
        this.expression = expression;
        this.variable = variable;
    }

    protected void checkConst() {
        boolean isConst = false;
        if (variable instanceof SimpleVar) {
            DSCP dscp = SymbolTableHandler.getInstance().getDescriptor(variable.getName());
            isConst = (dscp instanceof GlobalVarDSCP) ? ((GlobalVarDSCP) dscp).isConstant() : ((LocalVarDSCP) dscp).isConstant();
        }
        if (isConst)
            throw new RuntimeException("Const variables can't assign");
    }

}
