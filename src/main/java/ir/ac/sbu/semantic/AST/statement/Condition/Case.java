package ir.ac.sbu.semantic.AST.statement.Condition;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.expression.constant.IntegerConst;
import ir.ac.sbu.semantic.AST.statement.Statement;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;

public class Case extends Statement {
    IntegerConst exp;
    private Block block;
    Label StartCase = new Label();
    Label jump;
    public Case(IntegerConst exp, Block block){
        this.exp = exp;
        this.block = block;
    }
    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        mv.visitLabel(StartCase);
        SymbolTableHandler.getInstance().addScope(Scope.SWITCH);
        block.codegen(mv, cw);
        SymbolTableHandler.getInstance().popScope();
        mv.visitJumpInsn(GOTO,jump);
    }
}
