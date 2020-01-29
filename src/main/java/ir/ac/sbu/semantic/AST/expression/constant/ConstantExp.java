package ir.ac.sbu.semantic.AST.expression.constant;

import ir.ac.sbu.semantic.AST.expression.Expression;

public abstract class ConstantExp extends Expression {
    public abstract Object getValue();
}
