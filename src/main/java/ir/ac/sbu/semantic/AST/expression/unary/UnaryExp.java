package ir.ac.sbu.semantic.AST.expression.unary;

import ir.ac.sbu.semantic.AST.expression.Expression;

abstract public class UnaryExp extends Expression {
    protected Expression operand;

    UnaryExp(Expression operand){
        this.operand = operand;
    }

}
