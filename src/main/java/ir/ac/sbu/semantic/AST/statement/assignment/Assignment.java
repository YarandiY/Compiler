package ir.ac.sbu.semantic.AST.statement.assignment;

import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.variable.Variable;
import ir.ac.sbu.semantic.AST.statement.Statement;
import ir.ac.sbu.semantic.AST.statement.loop.InitExp;
import ir.ac.sbu.semantic.AST.statement.loop.StepExp;


public abstract class Assignment extends Statement implements InitExp, StepExp{
    protected Expression expression;
    protected Variable variable;

    Assignment(Expression expression, Variable variable) {
        this.expression = expression;
        this.variable = variable;
    }
}
