package ir.ac.sbu.semantic.AST.statement.Condition;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.expression.Expression;
import ir.ac.sbu.semantic.AST.expression.binary.conditional.NotEqual;
import ir.ac.sbu.semantic.AST.statement.Statement;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTable;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class If extends Statement {

    private Expression exp;
    private Block block1, block2;

    // block2 can be empty (if we don't have an else)

    public If(Expression exp, Block block1, Block block2){
        this.exp = exp;
        this.block1 = block1;
        this.block2 = block2;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
//        SymbolTableHandler.getInstance().addScope(Scope.IF);
//        NotEqual notEqual = new NotEqual();
//        notEqual.setBinaryExp(exp,new IntConstExp (0));
//        notEqual.codegen(mv,cw);
//        Label startElse = new Label();
//        Label endElse = new Label();
////      if 0 : false : else
//        mv.visitJumpInsn(Opcodes.IFEQ,startElse);
////        mv.visitLabel(SymbolTable.getInstance().getLabelStart());
//        block1.codegen(mv,cw);
//        mv.visitJumpInsn(Opcodes.GOTO,endElse);
////        mv.visitLabel(SymbolTable.getInstance().getLabelLast());
//        if(block2!=null){
//            SymbolTableHandler.getInstance().popScope();
//            SymbolTableHandler.getInstance().addScope(SymbolTable.COND_OTHER_THAN_SWITCH);
//            SymbolTableHandler.getInstance().setLabelFirst(startElse);
//            SymbolTableHandler.getInstance().setLabelLast(endElse);
//            mv.visitLabel(startElse);
//            block2.codegen(mv,cw);
//            mv.visitLabel(endElse);
//        }else{
//            mv.visitLabel(startElse);
//            mv.visitLabel(endElse);
//        }
    }
}
