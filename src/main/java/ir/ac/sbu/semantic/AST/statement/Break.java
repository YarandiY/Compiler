package ir.ac.sbu.semantic.AST.statement;

import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTable;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GOTO;

public class Break extends Statement {
    public Break() {
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if (SymbolTableHandler.getInstance().canHaveBreak()) {
            int i = SymbolTableHandler.getInstance().getStackScopes().size() - 1;
            for (; i >= 0; i--) {
                SymbolTable scope = SymbolTableHandler.getInstance().getStackScopes().get(i);
                if (scope.getTypeOfScope() == Scope.LOOP) {
                    mv.visitJumpInsn(GOTO, SymbolTableHandler.getInstance().getInnerLoop().getEnd());
                    return;
                } else if (scope.getTypeOfScope() == Scope.SWITCH) {
                    mv.visitJumpInsn(GOTO, SymbolTableHandler.getInstance().getLastSwitch().getEnd());
                    return;
                }
            }
        } else
            throw new RuntimeException("This part is not switch nor Loop");
    }
}
