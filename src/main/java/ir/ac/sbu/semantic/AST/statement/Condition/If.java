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

    private Expression exp;
    private Block ifBlock, elseBlock;

    public If(Expression exp, Block ifBlock, Block elseBlock) {
        this.exp = exp;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        SymbolTableHandler.getInstance().addScope(Scope.IF);
        NotEqual notEqual = new NotEqual(exp, new IntegerConst(0));
        notEqual.codegen(mv, cw);
        Label startElse = new Label();
        Label endElse = new Label();
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
