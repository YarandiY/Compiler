package ir.ac.sbu.semantic.AST.statement.loop;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.NotEqual;
import ir.ac.sbu.semantic.AST.expression.constant.IntegerConst;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.IFNE;

public class Repeat extends Loop{
    private Expression expression;
    private Label startLoop = new Label();

    public Repeat(Block block, Expression expression) {
        super(block);
        this.expression = expression;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        SymbolTableHandler.getInstance().addScope(Scope.LOOP);
        mv.visitLabel(startLoop);
        block.codegen(mv, cw);
        NotEqual notEqual = new NotEqual(expression, new IntegerConst(0));
        notEqual.codegen(mv, cw);
        mv.visitJumpInsn(IFNE, startLoop);
        SymbolTableHandler.getInstance().popScope();
    }
}
