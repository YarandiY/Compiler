package ir.ac.sbu.semantic.AST.statement.Condition;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.statement.Statement;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.GOTO;

public class Switch extends Statement{
    private Expression expression;
    private ArrayList <Case> cases;
    private Block defaultBlock;
    private Label defaultLabel = new Label();
    private Label lookUpTable = new Label();
    private Label end = new Label();

    public Switch(Expression expression, ArrayList<Case> cases, Block defaultBlock){
        this.expression = expression;
        this.cases = cases;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        SymbolTableHandler.getInstance().addScope(Scope.SWITCH);
        Label [] labels = new Label[cases.size()];
        int [] keys = new int[cases.size()];
        int i = 0 ;
        expression.codegen(mv, cw);
        mv.visitJumpInsn(GOTO, lookUpTable);
        for(Case c : cases){
            c.jump = end;
            c.codegen(mv, cw);
            labels[i] = c.StartCase;
            keys[i++] = (int) c.exp.getValue();
        }
        mv.visitLabel(defaultLabel);
        if (defaultBlock != null) {
            SymbolTableHandler.getInstance().addScope(Scope.SWITCH);
            defaultBlock.codegen(mv, cw);
            SymbolTableHandler.getInstance().popScope();
        }
        mv.visitJumpInsn(GOTO, end);
        mv.visitLabel(lookUpTable);
        mv.visitLookupSwitchInsn(defaultLabel, keys, labels);
        mv.visitLabel(end);
        SymbolTableHandler.getInstance().popScope();
    }
}
