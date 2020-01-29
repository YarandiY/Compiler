package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;

abstract public class UnaryExp extends Expression {
    protected Expression operand;

    public UnaryExp(Expression operand){
        this.operand = operand;
    }

}
