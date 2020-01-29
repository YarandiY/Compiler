package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;

abstract public class Unary extends Expression {
    protected Expression operand;

    public Unary(Expression operand){
        this.operand = operand;
    }

}
