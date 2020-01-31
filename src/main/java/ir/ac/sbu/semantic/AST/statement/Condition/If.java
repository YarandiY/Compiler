package ir.ac.sbu.semantic.AST.statement.Condition;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.NotEqual;
import ir.ac.sbu.semantic.AST.expression.constant.IntegerConst;
import ir.ac.sbu.semantic.AST.statement.Statement;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class If extends Statement {

    private Expression expression;
    private Block ifBlock, elseBlock;
    private Label startElse = new Label();
    private Label endElse = new Label();

    public If(Expression expression, Block ifBlock, Block elseBlock) {
        this.expression = expression;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        SymbolTableHandler.getInstance().addScope(Scope.IF);
        NotEqual notEqual = new NotEqual(expression, new IntegerConst(0));
        notEqual.codegen(mv, cw);
        mv.visitJumpInsn(IFEQ, startElse);
        ifBlock.codegen(mv, cw);
        mv.visitJumpInsn(GOTO, endElse);
        SymbolTableHandler.getInstance().popScope();
        if (elseBlock != null) {
            SymbolTableHandler.getInstance().addScope(Scope.IF);
            mv.visitLabel(startElse);
            elseBlock.codegen(mv, cw);
            SymbolTableHandler.getInstance().popScope();
        }
        else
            mv.visitLabel(startElse);
        mv.visitLabel(endElse);
    }
}
