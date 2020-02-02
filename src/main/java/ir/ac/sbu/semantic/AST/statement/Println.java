package ir.ac.sbu.semantic.AST.statement;

import ir.ac.sbu.semantic.AST.expression.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Println extends Statement {

    private Expression expression;

    public Println(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if(expression == null){

        }else{

        }

    }
}
