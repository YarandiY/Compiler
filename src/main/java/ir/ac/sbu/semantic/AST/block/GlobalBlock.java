package ir.ac.sbu.semantic.AST.block;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.declaration.Declaration;
import ir.ac.sbu.semantic.AST.expression.FuncCall;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;


@Data
public class GlobalBlock implements Node {

    private List<Declaration> declarationList;
    private static GlobalBlock instance = new GlobalBlock();

    public static GlobalBlock getInstance(){
        return instance;
    }

    private GlobalBlock() {
        this.declarationList = new ArrayList<>();
    }

    public void addDeclaration(Declaration declaration){
        declarationList.add(declaration);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        declarationList.forEach(declaration -> declaration.codegen(mv, cw));
        new FuncCall("start",new ArrayList<>()).codegen(mv, cw);
    }
}
